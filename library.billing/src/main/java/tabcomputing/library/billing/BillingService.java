package tabcomputing.library.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Interface to Google billing service. This class also use BillOfSale class to save
 * purchases to file, presently in plain text, but eventually we should encode that
 * to help prevent any piracy.
 */
public class BillingService implements ServiceConnection {

    //private MainActivity mContext;

    private Context appContext;

    private IInAppBillingService mService;

    private final Handler mHandler = new Handler();

    //private boolean ready = false;

    private Runnable loadCallback;

    private BillOfSale billOfSale;

    /* Products that have been purchased. */
    ArrayList<PurchasedProduct> purchasedProducts = new ArrayList<>();

    /* Products that are available for purchase. */
    ArrayList<AvailableProduct> availableProducts;

    // store current transactions for sale verification
    HashMap<String, String> transactions = new HashMap<>();

    private String[] skus;

    /**
     * Powerful is the constructor that takes a callback!
     *
     * @param context  activity context
     * @param callback callback to run when products are finished loading
     */
    public BillingService(Context context, String[] skus, Runnable callback) {
        this.appContext   = context.getApplicationContext();
        this.loadCallback = callback;

        //skus = appContext.getResources().getStringArray(R.array.productIds);
        this.skus = skus;

        bindService();

        billOfSale = BillOfSale.getInstance(appContext);  // new BillOfsale(context);
        billOfSale.readBillOfSale();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IInAppBillingService.Stub.asInterface(service);
        Log.d("BillingService", "Billing Connected!");
        load();
    }

    //public IInAppBillingService getService() {
    //    return mService;
    //}

    public void bindService() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        appContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mService != null) {
            appContext.unbindService(this);
        }
    }

    /**
     * Is billing supported?
     *
     * @return true or false
     */
    public boolean isSupported() {
        if (mService == null) {
            return false;
        }
        try {
            int result = mService.isBillingSupported(3, appContext.getPackageName(), "inapp");
            if (result == 0) {
                return true;
            }
        } catch (RemoteException e) {
            Log.d("isSupported", e.toString());
            return false;
        }
        return false;
    }

    class AvailableProduct {
        String sku;
        String price;

        public AvailableProduct(String sku, String price) {
            this.sku = sku;
            this.price = price;
        }
    }

    private void loadSkuDetails() {
        ArrayList<AvailableProduct> products = new ArrayList<>();
        ArrayList<String> skuList = new ArrayList<>(Arrays.asList(skus));

        Bundle querySkus = new Bundle();

        // kind of odd to load sku details we have to already know the skus
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle skuDetails;

        try {
            skuDetails = mService.getSkuDetails(3, appContext.getPackageName(), "inapp", querySkus);

            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == RESPONSE_OK) {
                ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
                if (responseList != null) {
                    for (String thisResponse : responseList) {
                        try {
                            JSONObject object = new JSONObject(thisResponse);
                            String sku = object.getString("productId");
                            String price = object.getString("price");
                            products.add(new AvailableProduct(sku, price));
                        } catch (JSONException e) {
                            reportVerificationError(VERIFY_DATA_PARSE_FAILURE);
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                reportResponseError(response);
            }
        } catch (RemoteException e) {
            reportResponseError(RESPONSE_ERROR);
            e.printStackTrace();
        }
        availableProducts = products;
    }

    class PurchasedProduct {
        String sku;
        String data;
        String signature;

        public PurchasedProduct(String sku, String data, String signature) {
            this.sku = sku;
            this.data = data;
            this.signature = signature;
        }
    }

    private final String FAKE_TOKEN = "---";


    private void loadPurchasedItems() {
        String continuationToken = FAKE_TOKEN;
        ArrayList<PurchasedProduct> products = new ArrayList<>();

        while (continuationToken != null) {
            Bundle ownedItems = queryOwnedItems(continuationToken);

            if (ownedItems == null) {
                reportResponseError(RESPONSE_ITEM_NOT_OWNED);
                return;
            }

            int responseCode = ownedItems.getInt("RESPONSE_CODE");

            if (responseCode == RESPONSE_OK) {
                ArrayList<String> skus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> data = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> sigs = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");

                continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < data.size(); ++i) {
                    products.add(new PurchasedProduct(skus.get(i), data.get(i), sigs.get(i)));
                }
            } else {
                reportResponseError(responseCode);
                return;
            }
        }

        purchasedProducts = products;
    }

    /**
     * Make the call to billing service to get purchase data.
     *
     * @param token continuation token (if not null there is more to load)
     * @return bundle with info about owned items
     */
    private Bundle queryOwnedItems(String token) {
        Bundle ownedItems;
        if (token.equals(FAKE_TOKEN)) {
            token = null;
        }

        try {
            ownedItems = mService.getPurchases(3, appContext.getPackageName(), "inapp", token);
        } catch (RemoteException e) {
            return null;
        }

        return ownedItems;
    }

    /**
     * Threaded call to load purchase data.
     */
    private final Runnable loadRunner = new Runnable() {
        @Override
        public void run() {
            loadPurchasedItems();
            loadSkuDetails();
            updateBillOfSale();
            updateActivity();
        }
    };

    // Update bill of sale.
    //private Runnable updateRunner = new Runnable() {
    //    public void run() {
    //        updateBillOfSale();
    //        //refreshUI();  // TODO: do we need this any more?
    //    }
    //};

    public void load() {
        mHandler.post(loadRunner);
    }

    /**
     * If the list of purchased items reported by Google Play differs from the bill-of-sale,
     * rewrite the bill-of-sale.
     */
    protected void updateBillOfSale() {
        boolean needSave = false;
        for(String sku : getPurchasedSkus()) {
            if (! billOfSale.isOwned(sku)) {
                billOfSale.add(sku);
                needSave = true;
            }
        }
        if (needSave) {
            billOfSale.writeBillOfSale();
        }
    }

    private void updateActivity() {
        mHandler.post(loadCallback);
    }

    /*
    abstract class BillingRunnable implements Runnable {
        private List<AvailableProduct> availableProducts;
        private List<PurchasedProduct> purchasedProducts;

        public BillingRunnable(List<AvailableProduct> availableProducts, List<PurchasedProduct> purchasedProducts) {
            this.availableProducts = availableProducts;
            this.purchasedProducts = purchasedProducts;
        }
    }
    */

    /**
     * Initiate payment flow.
     *
     * @param sku      product id
     * @param activity activity
     */
    public void buyProduct(String sku, Activity activity) {
        Bundle buyIntentBundle;
        IntentSender intentSender;

        if (mService == null) {
            reportResponseError(RESPONSE_BILLING_UNAVAILABLE);
            return;
        }

        String developerPayload = generateDeveloperPayload(sku);
        //String dataSignature = "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ";

        try {
            buyIntentBundle = mService.getBuyIntent(3, appContext.getPackageName(), sku, "inapp", developerPayload);
        } catch (RemoteException e) {
            reportResponseError(RESPONSE_ERROR);
            e.printStackTrace();
            return;
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

        if (pendingIntent == null) {
            // FIXME: item might already be owned?
            reportResponseError(RESPONSE_ITEM_UNAVAILABLE);
            return;
        }

        int responseCode = buyIntentBundle.getInt("RESPONSE_CODE");
        if (responseCode != RESPONSE_OK) {
            reportResponseError(responseCode);
            return;
        }

        try {
            intentSender = pendingIntent.getIntentSender();
            activity.startIntentSenderForResult(intentSender, BUY_REQUEST_CODE, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch (IntentSender.SendIntentException e) {
            reportResponseError(RESPONSE_ERROR);
            e.printStackTrace();
            return;
        }

        // add dev payload and sku to pending transactions
        transactions.put(developerPayload, sku);
    }

    public static final int BUY_REQUEST_CODE = 1001;

    final int RESPONSE_OK = 0;                   // success
    final int RESPONSE_USER_CANCELED = 1;        // user pressed back or canceled a dialog
    final int RESPONSE_BILLING_UNAVAILABLE = 3;  // this billing API version is not supported for the type requested
    final int RESPONSE_ITEM_UNAVAILABLE = 4;     // requested SKU is not available for purchase
    final int RESPONSE_DEVELOPER_ERROR = 5;      // invalid arguments provided to the API
    final int RESPONSE_ERROR = 6;                // Fatal error during an API action
    final int RESPONSE_ITEM_ALREADY_OWNED = 7;   // Failure to purchase since item is already owned
    final int RESPONSE_ITEM_NOT_OWNED = 8;       // Failure to consume since item is not owned

    final int VERIFY_INVALID_PRODUCT_ID = 9;
    final int VERIFY_INVALID_CODE = 10;
    final int VERIFY_DATA_PARSE_FAILURE = 11;
    final int VERIFY_NO_PURCHASE_DATA = 12;

    /**
     * TODO: Confused on what the resultCode is as opposed to the responseCode.
     */
    public boolean buyResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BUY_REQUEST_CODE) { return false; }

        if (resultCode != Activity.RESULT_OK) {
            reportResultError(resultCode);
            return false;
        }

        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");  // TODO: Use this to further verify purchase?

        boolean success = false;

        if (responseCode == RESPONSE_OK) {
            if (purchaseData == null) {
                reportVerificationError(VERIFY_NO_PURCHASE_DATA);
                return false;
            }

            try {
                JSONObject jo = new JSONObject(purchaseData);
                String sku = jo.getString("productId");
                String dp = jo.getString("developerPayload");
                String token = jo.getString("purchaseToken");

                // verify purchase
                if (transactions.containsKey(dp)) {
                    String id = transactions.get(dp);
                    if (!id.equals(sku)) {
                        reportVerificationError(VERIFY_INVALID_PRODUCT_ID);
                    }
                    savePurchase(sku);
                    transactions.remove(dp);
                    //transactions.put(token, trans);
                    //consumeProduct(token);
                    success = true;
                } else {
                    reportVerificationError(VERIFY_INVALID_CODE);
                }
            } catch (JSONException e) {
                reportVerificationError(VERIFY_DATA_PARSE_FAILURE);
                e.printStackTrace();
            }
        } else {
            reportResponseError(responseCode);
        }
        return success;
    }

    /**
     * Lots of possible errors when handling purchase transactions.
     *
     * @param errorCode     error reference number
     */
    private void reportResponseError(int errorCode) {
        int resId = -1;

        switch(errorCode) {
            case RESPONSE_USER_CANCELED:
                resId = R.string.RESPONSE_USER_CANCELED;
                break;
            case RESPONSE_BILLING_UNAVAILABLE:
                resId = R.string.RESPONSE_BILLING_UNAVAILABLE;
                break;
            case RESPONSE_ITEM_UNAVAILABLE:
                resId = R.string.RESPONSE_ITEM_UNAVAILABLE;
                break;
            case RESPONSE_ITEM_ALREADY_OWNED:
                resId = R.string.RESPONSE_ITEM_ALREADY_OWNED;
                break;
            case RESPONSE_ITEM_NOT_OWNED:
                resId = R.string.RESPONSE_ITEM_NOT_OWNED;
                break;
            case RESPONSE_DEVELOPER_ERROR:
                resId = R.string.RESPONSE_DEVELOPER_ERROR;
                break;
            default:
                resId = R.string.RESPONSE_ERROR;
                break;
        }

        if (resId != -1) {
            alert(appContext.getResources().getString(resId));
        }
    }

    /**
     * Lots of possible errors when handling purchase transactions.
     *
     * @param errorCode     error reference number
     */
    private void reportVerificationError(int errorCode) {
        int resId = -1;
        switch(errorCode) {
            case VERIFY_INVALID_PRODUCT_ID:
                resId = R.string.VERIFY_INVALID_PRODUCT_ID;
                break;
            case VERIFY_INVALID_CODE:
                resId = R.string.VERIFY_INVALID_CODE;
                break;
            case VERIFY_DATA_PARSE_FAILURE:
                resId = R.string.VERIFY_DATA_PARSE_FAILURE;
                break;
            case VERIFY_NO_PURCHASE_DATA:
                resId = R.string.VERIFY_NO_PURCHASE_DATA;
                break;
        }
        if (resId != -1) {
            alert(appContext.getResources().getString(resId));
        }
    }

    /**
     * Lots of possible errors when handling purchase transactions.
     *
     * @param errorCode     error reference number
     */
    private void reportResultError(int errorCode) {
        int resId = -1;
        switch(errorCode) {
            case Activity.RESULT_CANCELED:
                resId = R.string.RESULT_CANCELED;
                break;
            default:
                resId = R.string.RESULT_ERROR;
                break;
        }
        if (resId != -1) {
            alert(appContext.getResources().getString(resId));
        }
    }

    /**
     * Flash message.
     *
     * @param msg message to give to user
     */
    public void alert(String msg) {
        Toast toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    // TODO: encrypt the sku
    private String generateDeveloperPayload(String sku) {
        return (sku + (int) (Math.random() * 10000));
    }

    /**
     * Get the set of purchased skus.
     *
     * @return set of purchased skus
     */
    public HashSet<String> getPurchasedSkus() {
        HashSet<String> skus = new HashSet<>();
        for (PurchasedProduct product : purchasedProducts) {
            skus.add(product.sku);
        }
        return skus;
    }

    public boolean isOwned(String sku) {
        return billOfSale.isOwned(sku);
    }

    /**
     * Hash a given product been purchased?
     *
     * @param sku       product id
     * @return          true if product had been purchased
     */
    public boolean isBought(String sku) {
        for(PurchasedProduct product : purchasedProducts) {
            if (product.sku.equals(sku)) {
                return true;
            }
        }
        return false;
    }


    public String getPrice(String sku) {
        for(AvailableProduct product : availableProducts) {
            if (product.sku.equals(sku)) {
                return product.price;
            }
        }
        return null;
    }

    /**
     * Add product to bill-of-sale and save.
     *
     * @param sku       product id
     */
    private void savePurchase(String sku) {
        billOfSale.add(sku);
        billOfSale.writeBillOfSale();
        //hideBuyButton();
    }

    //LinkedList<String> tokens = new LinkedList<>();

    /**
     * Consume product.
     *
     * @param token     purchase token
     *
    public void consumeProduct(String token) {
        tokens.add(token);
        consumptionRunner.run();
    }

    Runnable consumptionRunner = new Runnable() {
        public void run() {
            String token = tokens.remove();

            try {
                mService.consumePurchase(3, mContext.getPackageName(), token);
                mContext.onConsumption(token);
            } catch (RemoteException e) {
                Log.d("purchaseRunner", e.toString());
            }
        }
    };
    */

}
