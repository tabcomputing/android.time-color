package tabcomputing.tcwallpaper;

import android.content.Context;
import android.content.pm.PackageManager;
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
        name.setText(item.title);
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

    public final String ITEM_TAG_UNKNOWN = "?";
    public final String ITEM_TAG_PURCHASED = "✓";
    public final String ITEM_TAG_FREE = "";

    public void setupItems() {
        addItem("solid", "Solid", R.drawable.thumbnail_solid);
        addItem("gradient", "Gradient", R.drawable.thumbnail_gradient);
        addItem("orb", "Orb", R.drawable.thumbnail_orb);

        addItem("stripes", "Stripes",  R.drawable.thumbnail_stripes);
        addItem("binary", "Binary", R.drawable.thumbnail_binary);
        addItem("plaid",  "Plaid",  R.drawable.thumbnail_plaid);

        addItem("circles", "Circles", R.drawable.thumbnail_circles);
        addItem("pieslice", "Pie Slice", R.drawable.thumbnail_pieslice);
        addItem("squares", "Squares", R.drawable.thumbnail_squares);

        addItem("wave", "Wave", R.drawable.thumbnail_wave);
        addItem("caterpillar", "Caterpillar", R.drawable.thumbnail_caterpillar);
        addItem("horizons", "Horizons", R.drawable.thumbnail_horizons);

        addItem("echo", "Echo", R.drawable.thumbnail_echo);
        addItem("ring", "Ring", R.drawable.thumbnail_ring);
        addItem("radial", "Radial", R.drawable.thumbnail_radial);

        addItem("jack", "Jack", R.drawable.thumbnail_jack);
        addItem("mondrian", "Mondrian", R.drawable.thumbnail_mondrian);
        addItem("qbert", "QBert", R.drawable.thumbnail_qbert);

        addItem("lotus", "Lotus", R.drawable.thumbnail_lotus);
        addItem("space", "Space", R.drawable.thumbnail_space);
        addItem("tile", "Tile", R.drawable.thumbnail_tile);

        addItem("checkers", "Checkers", R.drawable.thumbnail_checkers);
        addItem("maze", "Maze", R.drawable.thumbnail_maze);
        addItem("bigtime", "Big Time", R.drawable.thumbnail_bigtime);
    }

    protected HashMap<String, Item> itemIndex = new HashMap<>();

    public void addItem(String name, String title, int drawId) {
        Item item = new Item(name, title, drawId);
        itemIndex.put(name, item);
        mItems.add(item);
    }

    public boolean hasProduct(String sku) {
        return itemIndex.containsKey(sku);
    }

    /*
    public void markPrice(String sku, String price) {
        for(Item item : mItems) {
            if (item.hasSku(sku)) {
                item.setTag(price);
            }
        }
    }
    */

    /*
    public void markAvailable(String sku) {
        for(Item item : mItems) {
            if (item.hasSku(sku)) {
                item.markAvailable();
            }
        }
    }
    */

    /**
     * Mark item with matching name as purchased.
     *
     * @param name       product name
     * @return           true if item exists
     */
    public boolean markPurchased(String name) {
        if (itemIndex.containsKey(name)) {
            Item item = itemIndex.get(name);
            item.markPurchased();
            return true;
        } else {
            return false;
        }
    }

    /**
     * If google play doesn't return product list, then we mark the wallpaper items with
     * an "unknown" status.
     *
    public void markUnknown() {
        for(Item item : mItems) {
            if (! item.isFree()) {
                item.markUnknown();
            }
        }
    }
    */

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
     *
    public String getPackageName(int position) {
        return wallpaperApps[position];
    }
     */

    /**
     * Get a list of wallpaper names.
     */
    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Item item : mItems) {
            names.add(item.name);
        }
        return names;
    }

    /**
     * TODO: Libraries are currently "wallpaper" but should be "tcwallpaper" or moved to main package.
     * @param name      name of pattern
     * @return          name of package
     */
    public String getPackageName(String name) {
        return ("tabcomputing.tcwallpaper." + name);
    }

    //public String getSku(int position) {
    //    Item item = mItems.get(position);
    //    return item.sku;
    //}

    //public String isPurchased(int position) {
    //    Item item = mItems.get(position);
    //    return item.sku;
    //}

    public boolean isPurchased(String name) {
        if (itemIndex.containsKey(name)) {
            Item item = itemIndex.get(name);
            return item.tag.equals(ITEM_TAG_PURCHASED);
        }
        return false;
    }

    /**
     * Return a string of owned items, the name of each per line.
     *
     * @return      bill of sale list
     *
    public String billOfSale() {
        ArrayList<String> list = new ArrayList<>();
        for(Item item : mItems) {
            if (item.isOwned()) {
                list.add(item.name);
            }
        }
        return TextUtils.join("\n", list);
    }
    */

    /**
     *
     */
    public static class Item {
        private final String ITEM_TAG_UNKNOWN = "?";
        private final String ITEM_TAG_PURCHASED = "";
        private final String ITEM_TAG_FREE = "";
        private final String ITEM_TAG_AVAILABLE = "☐";
        private final String ITEM_TAG_SELECTED = "☑";

        public final String name;
        public final String title;
        public final int drawableId;

        // has been purchased?
        public boolean bought = false;

        public String tag = ITEM_TAG_FREE; //"✗";

        Item(String name, String title, int drawableId) {
            this.name = name;
            this.title = title;
            this.drawableId = drawableId;
        }

        //public boolean hasSku(String sku) {
        //    return this.sku.equals(sku);
        //}

        //public boolean isFree() {
        //    return (sku.equals("free"));
        //}

        //public boolean isOwned() {
        //    return this.bought || isFree();
        //}

        public boolean isSelected() {
            return this.tag.equals(ITEM_TAG_SELECTED);
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        //public void markAvailable() {
        //    this.bought = !isFree();
        //    this.tag = ITEM_TAG_AVAILABLE;
        //}

        public void markPurchased() {
            this.bought = true;
            this.tag = ITEM_TAG_PURCHASED;
        }

        public void markSelected(boolean select) {
            if (select) {
                this.tag = ITEM_TAG_SELECTED;
            } else {
                this.tag = ITEM_TAG_AVAILABLE;
            }
        }

        //public void markUnknown() {
        //    this.bought = !isFree();
        //    this.tag = ITEM_TAG_UNKNOWN;
        //}

    }

}
