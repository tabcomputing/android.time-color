package tabcomputing.wallpaper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrowseAdapter extends BaseAdapter {
    private Context mContext;

    private final List<Item> mItems = new ArrayList<>();
    private final LayoutInflater mInflater;

    public BrowseAdapter(Context c) {
        mContext = c;

        mInflater = LayoutInflater.from(c);

        setupItems();
    }

    public int getCount() {
        return mItems.size();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public long getItemId(int position) {
        //return position;
        return mItems.get(position).drawableId;
    }

    private GridView.LayoutParams layoutParams = new GridView.LayoutParams(300, 450);

    /**
     * create a new ImageView for each item referenced by the Adapter
     *
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = images[position];
            // if it's not recycled, initialize some attributes
            //imageView = new ImageView(mContext);
            //imageView.setLayoutParams(layoutParams); //new GridView.LayoutParams(300, 450));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
            //imageView.setImageResource(mThumbIds[position]);
        } else {
            imageView = (ImageView) convertView;
            imageView.setImageResource(mThumbIds[position]);
        }
        //imageView.setSelected(true);
        return imageView;
    }
    */

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;
        TextView tag;

        if (v == null) {
            v = mInflater.inflate(R.layout.item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.tag, v.findViewById(R.id.tag));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        tag  = (TextView) v.getTag(R.id.tag);

        Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);
        tag.setText(item.tag);

        return v;
    }

    /*
    private ImageView[] images = new ImageView[15];

    public void setupImages() {
        ImageView imageView;
        for(int i=0; i < mThumbIds.length; i++) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(layoutParams); //new GridView.LayoutParams(300, 450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 16, 8, 4);
            imageView.setImageResource(mThumbIds[i]);

            images[i] = imageView;
        }
    }
    */

    private final String ITEM_TAG_UNKNOWN = "X";
    private final String ITEM_TAG_PURCHASED = "✓";
    private final String ITEM_TAG_FREE = "FREE"; //25¢

    public void setupItems() {
        addProduct("solid", "Solid", R.drawable.thumbnail_solid, "✓");    // always free
        addProduct("gradient", "Gradient", R.drawable.thumbnail_gradient);
        addProduct("radient", "Radiant", R.drawable.thumbnail_radiant);
        addProduct("binarystripes", "Binary Stripes", R.drawable.thumbnail_binarystripes);
        addProduct("caterpillar", "Caterpillar", R.drawable.thumbnail_caterpillar);
        addProduct("echo", "Echo", R.drawable.thumbnail_echo);
        addProduct("horizons", "Horizons", R.drawable.thumbnail_horizons);
        addProduct("lotus", "Lotus", R.drawable.thumbnail_lotus);
        addProduct("mondrian", "Mondrian", R.drawable.thumbnail_mondrian);
        addProduct("pieslice", "Pie Slice", R.drawable.thumbnail_pieslice);
        addProduct("plaid", "Plaid", R.drawable.thumbnail_plaid);
        addProduct("radial", "Radial", R.drawable.thumbnail_radial);
        addProduct("squares", "Scooby Squares", R.drawable.thumbnail_squares);
        addProduct("bigtime", "Big Time", R.drawable.thumbnail_squares);
        addProduct("nothing", "Nothing", R.drawable.thumbnail_squares);
    }

    HashMap<String, Item> itemIndex = new HashMap<>();

    public void addProduct(String productId, String title, int drawId, String tag) {
        Item item = new Item(productId, title, drawId, tag);
        itemIndex.put(productId, item);
        mItems.add(item);
    }

    public void addProduct(String productId, String title, int drawId) {
        addProduct(productId, title, drawId, null);
    }

    public boolean hasProduct(String sku) {
        return itemIndex.containsKey(sku);
    }

    public boolean markPrice(String productId, String price) {
        if (itemIndex.containsKey(productId)) {
            Item item = itemIndex.get(productId);
            item.setTag(price);
            return true;
        } else {
            Log.d("markPrice", "Product " + productId + " does not exist.");
            return false;
        }
    }

    public boolean markPurchased(String productId) {
        if (itemIndex.containsKey(productId)) {
            Item item = itemIndex.get(productId);
            item.setTag(ITEM_TAG_PURCHASED);
            return true;
        } else {
            Log.d("markPurchased", "Product " + productId + " does not exist.");
            return false;
        }
    }

    /*
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.thumbnail_solid,
            R.drawable.thumbnail_binarystripes,
            R.drawable.thumbnail_caterpillar,
            R.drawable.thumbnail_echo,
            R.drawable.thumbnail_gradient,
            R.drawable.thumbnail_horizons,
            R.drawable.thumbnail_lotus,
            R.drawable.thumbnail_mondrian,
            R.drawable.thumbnail_pieslice,
            R.drawable.thumbnail_plaid,
            R.drawable.thumbnail_radial,
            R.drawable.thumbnail_radiant,
            R.drawable.thumbnail_squares,
            R.drawable.thumbnail_caterpillar,
            R.drawable.thumbnail_caterpillar
    };
    */

    private String[] wallpaperApps = {
            "tabcomputing.wallpaper.solid",
            "tabcomputing.wallpaper.binarystripes",
            "tabcomputing.wallpaper.caterpillar",
            "tabcomputing.wallpaper.echo",
            "tabcomputing.wallpaper.gradient",
            "tabcomputing.wallpaper.horizons",
            "tabcomputing.wallpaper.lotus",
            "tabcomputing.wallpaper.mondrian",
            "tabcomputing.wallpaper.pieslice",
            "tabcomputing.wallpaper.plaid",
            "tabcomputing.wallpaper.radial",
            "tabcomputing.wallpaper.radiant",
            "tabcomputing.wallpaper.squares",
            "tabcomputing.wallpaper.caterpillar",
            "tabcomputing.wallpaper.caterpillar"
    };

    /**
     * @deprecated
     * @param uri       package identifier
     * @return          true or false
     */
    public boolean isInstalled(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     *
     * @param position  index of wallpaper
     * @return          name of wallpaper
     */
    //public String getPackageName(int position) {
    //    return wallpaperApps[position];
    //}

    public String getPackageName(String sku) {
        return ("tabcomputing.wallpaper." + sku);
    }

    public String getSku(int position) {
        Item item = mItems.get(position);
        return item.productId;
    }

    private static class Item {
        public final String productId;
        public final String name;
        public final int drawableId;
        public String tag;

        Item(String productId, String name, int drawableId, String tag) {
            this.productId = productId;
            this.name = name;
            this.drawableId = drawableId;
            this.tag = (tag == null ? "✗" : tag);
        }

        Item(String productId, String name, int drawableId) {
            this.productId = productId;
            this.name = name;
            this.drawableId = drawableId;
            this.tag = "✗";
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

    }

    public boolean isPurchased(String sku) {
        if (itemIndex.containsKey(sku)) {
            Item item = itemIndex.get(sku);
            return item.tag.equals(ITEM_TAG_PURCHASED);
        }
        return false;
    }

}
