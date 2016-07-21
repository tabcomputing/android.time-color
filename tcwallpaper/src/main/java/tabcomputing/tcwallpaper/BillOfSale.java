package tabcomputing.tcwallpaper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to track what purchases have been made.
 */
public class BillOfSale {

    public BillOfSale(Context context) {
        this.context = context;
    }

    private Context context;

    // TODO: rename
    public final static String PRODUCT_ID = "freedom";

    // bill-of-sale file
    final String BILL_OF_SALE_FILE = "tcwallpaper.bos";

    // set of owned product skus
    private Set<String> billOfSale = new HashSet<>();

    /**
     * Add a sku to the billOfSale.
     *
     * @param sku       product id
     */
    public void add(String sku) {
        billOfSale.add(sku);
    }

    /**
     * Read bill-of-sale from user file.
     *
     * @return      list of names
     */
    public boolean readBillOfSale() {
        List<String> list;

        int n;
        FileInputStream fs;
        StringBuffer data = new StringBuffer("");

        try {
            fs = context.openFileInput(BILL_OF_SALE_FILE);
        } catch (FileNotFoundException e) {
            return false;
        }

        byte[] buffer = new byte[1024];
        try {
            while ((n = fs.read(buffer)) != -1) {
                data.append(new String(buffer, 0, n));
            }
            fs.close();
        } catch (IOException e) {
            try {
                fs.close();
            } catch (IOException e2) {
                Log.e("BILL-OF-SALE", e2.toString());
                return false;
            }
        }

        list = Arrays.asList(data.toString().split("\n"));

        billOfSale = new HashSet<>(list);

        return true;
    }

    /**
     * Write bill-of-sale to user file.
     */
    public boolean writeBillOfSale() {
        FileOutputStream fs;

        String bos = toText();

        try {
            fs = context.openFileOutput(BILL_OF_SALE_FILE, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            // NOTE: This ought never happen.
            Log.e("BILL-OF-SALE", e.toString());
            return false;
        }

        try {
            fs.write(bos.getBytes());
        } catch (IOException e) {
            Log.e("BILL-OF-SALE", e.toString());
            return false;
        }

        try {
            fs.close();
        } catch (IOException e) {
            Log.e("BILL-OF-SALE", e.toString());
            return false;  // TODO: ???
        }

        return true;
    }

    /**
     * Return a string of owned items, the name of each per line.
     *
     * @return      bill of sale list
     */
    public String toText() {
        return TextUtils.join("\n", billOfSale);
    }

    public boolean isOwned(String sku) {
        return billOfSale.contains(sku);
    }

}
