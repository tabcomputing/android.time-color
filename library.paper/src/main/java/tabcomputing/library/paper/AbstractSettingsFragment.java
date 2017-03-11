package tabcomputing.library.paper;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public abstract class AbstractSettingsFragment extends PreferenceFragment {

    //@Override
    //public void onCreatePreferences(final Bundle savedInstanceState, String whatString) {
    //    //super.onCreatePreferences(savedInstanceState, whatString);
    //}

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Can we cleanly separate the clock settings from the pattern settings?
        //       If so we could keep the preferences in separate xml files.
        //addPreferencesFromResource(R.xml.clock_preferences);

        //Preference button = findPreference("usePatternSettings");
        //button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        //    @Override
        //    public boolean onPreferenceClick(Preference preference) {
        //        readCurrentWallpaperPreferences();
        //        return true;
        //    }
        //});
    }

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //    View rootView = inflater.inflate(R.layout.settings, container, false);
    //    return rootView;
    //}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }

    /**
     * Import custom settings from current wallpaper.
     */
    protected void readCurrentWallpaperPreferences() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
        WallpaperInfo info = wallpaperManager.getWallpaperInfo();

        if (info == null) { return; }

        String serviceName = info.getServiceName();

        if (! serviceName.contains("tabcomputing.tcwallpaper")) {
            // TODO display message that current wallpaper is not a T+C=W wallpaper
            return;
        }

        CommonSettings settings = CommonSettings.getInstance();

        //if (settings.usePatternSettings()) {
            Context context = getActivity().getApplicationContext();
            settings.readPreferences(serviceName, context);
        //}

        ListPreference l;

        l = (ListPreference) findPreference(CommonSettings.KEY_TIME_SYSTEM);
        l.setValueIndex(settings.timeSystem());

        l = (ListPreference) findPreference(CommonSettings.KEY_COLOR_GAMUT);
        l.setValueIndex(settings.colorGamut());

        CheckBoxPreference b;

        b = (CheckBoxPreference) findPreference(CommonSettings.KEY_COLOR_DYNAMIC);
        b.setChecked(settings.hasDynamicColor());

        //b = (CheckBoxPreference) findPreference(CommonSettings.KEY_COLOR_DUPLEX);
        //b.setChecked(settings.isDuplexed());

        b = (CheckBoxPreference) findPreference(CommonSettings.KEY_BASE_CONVERT);
        b.setChecked(settings.isBaseConverted());

        b = (CheckBoxPreference) findPreference(CommonSettings.KEY_TIME_SECONDS);
        b.setChecked(settings.displaySeconds());

        // refresh screen
        //View view = getView();
        //if (view != null) {
        //    view.invalidate();
        //}
    }

}
