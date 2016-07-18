package tabcomputing.tcwallpaper;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ClockSettingsFragment extends PreferenceFragment {

    //@Override
    //public void onCreatePreferences(final Bundle savedInstanceState, String whatString) {
    //    //super.onCreatePreferences(savedInstanceState, whatString);
    //}

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.clock_preferences);
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
}
