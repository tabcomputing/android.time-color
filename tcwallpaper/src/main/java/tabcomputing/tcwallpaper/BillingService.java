package tabcomputing.tcwallpaper;

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

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Interface to Google billing service.
 */
public class BillingService implements ServiceConnection {

    private BrowserActivity mContext;
    //private Context mContext;

    private IInAppBillingService mService;

    private final Handler mHandler = new Handler();

    private boolean ready = false;

    private Runnable updateRunner;

    /* Products that have been purchased. */
    ArrayList<PurchasedProduct> purchasedProducts;

    /* Products that are available for purchase. */
    ArrayList<AvailableProduct> availableProducts;

    /**
     * Powerful is the constructor that takes a callback!
     *
     * @param context       activity context
     * @param callback      callback to run when products are finished loading
     */
    public BillingService(BrowserActivity context, Runnable callback) {
        this.mContext = context;
        this.updateRunner = callback;

        bindService();
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
        mContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mService != null) {
            mContext.unbindService(this);
        }
    }

    final int RESULT_OK = 0;                   // success
    final int RESULT_USER_CANCELED = 1;        // user pressed back or canceled a dialog
    final int RESULT_BILLING_UNAVAILABLE = 3;  // this billing API version is not supported for the type requested
    final int RESULT_ITEM_UNAVAILABLE = 4;     // requested SKU is not available for purchase
    final int RESULT_DEVELOPER_ERROR = 5;      // invalid arguments provided to the API
    final int RESULT_ERROR = 6;                // Fatal error during the API action
    final int RESULT_ITEM_ALREADY_OWNED = 7;   // Failure to purchase since item is already owned
    final int RESULT_ITEM_NOT_OWNED = 8;       // Failure to consume since item is not owned

    /**
     * Is billing supported?
     *
     * @return      true or false
     */
    public boolean isSupported() {
        if (mService == null) { return false; }
        try {
            int result = mService.isBillingSupported(3, mContext.getPackageName(), "inapp");
            if (result == 0) { return true; }
        } catch (RemoteException e) {
            Log.d("isSupported", e.toString());
            return false;
        }
        return false;
    }

    // TODO: define these else where and pass them in
    // list of product skus
    final String[] skus = {
            "binary",
            "caterpillar",
            "echo",
            "gradient",
            "horizons",
            "lotus",
            "mondrian",
            "orb",
            "pieslice",
            "plaid",
            "mondrian",
            "radial",
            "solid",
            "squares",
            "stripes"
    };

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

        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle skuDetails;

        try {
            skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), "inapp", querySkus);

            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
                if (responseList != null) {
                    for (String thisResponse : responseList) {
                        try {
                            JSONObject object = new JSONObject(thisResponse);
                            String sku = object.getString("productId");
                            String price = object.getString("price");
                            products.add(new AvailableProduct(sku, price));
                        } catch (JSONException e) {
                            Log.d("loadSkuDetails", e.toString());
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            Log.d("loadSkuDetails", e.toString());
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

        while(continuationToken != null) {
            Bundle ownedItems = queryOwnedItems(continuationToken);

            if (ownedItems == null) { return; }

            int response = ownedItems.getInt("RESPONSE_CODE");

            if (response == 0) {
                ArrayList<String> skus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> data = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> sigs = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");

                continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < data.size(); ++i) {
                    products.add(new PurchasedProduct(skus.get(i), data.get(i), sigs.get(i)));
                }
            }
        }

        purchasedProducts = products;
    }

    /**
     * Make the call to billing service to get purchase data.
     *
     * @param token     continuation token (if not null there is more to load)
     *
     * @return          bundle with info about owned items
     */
    private Bundle queryOwnedItems(String token) {
        Bundle ownedItems;
        if (token.equals(FAKE_TOKEN)) { token = null; }
        try {
            ownedItems = mService.getPurchases(3, mContext.getPackageName(), "inapp", token);
        } catch(RemoteException e) {
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
            ready = true;
            updateBrowser();
        }
    };

    private void updateBrowser() {
        mHandler.post(updateRunner);
    }

    /**
     * Returns true if purchase data has finished loading.
     *
     * @return
     */
    public boolean isReady() {
        return ready;
    }

    public void load() {
        mHandler.post(loadRunner);
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

    public String buyProduct(String sku, Activity activity) {
        Bundle buyIntentBundle;
        IntentSender intentSender;

        String developerPayload = generateDeveloperPayload(sku);

        try {
             buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
        } catch (RemoteException e) {
            Log.d("buyProduct", e.toString());
            return null;
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

        if (pendingIntent == null) {
            return null;
        }

        intentSender = pendingIntent.getIntentSender();

        try {
            activity.startIntentSenderForResult(intentSender, 1001, new Intent(), 0, 0, 0);  // Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch(IntentSender.SendIntentException e) {
            Log.d("buyProduct", e.toString());
            return null;
        }

        return developerPayload;
    }

    // TODO: encrypt the sku
    private String generateDeveloperPayload(String sku) {
        return (sku + (int)(Math.random() * 10000));
    }

    /**
     * Get the set of purchased skus.
     *
     * @return          set of purchased skus
     */
    public HashSet<String> getPurchasedSkus() {
        HashSet<String> skus = new HashSet<>();
        for(PurchasedProduct product : purchasedProducts) {
            skus.add(product.sku);
        }
        return skus;
    }

    /**
     * Hash a given product been purchased?
     *
     * @param sku       product id
     * @return          true if product had been purchased
     */
    public boolean isOwned(String sku) {
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
