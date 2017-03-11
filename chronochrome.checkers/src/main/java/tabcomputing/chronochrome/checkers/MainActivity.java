package tabcomputing.chronochrome.checkers;

import tabcomputing.library.paper.AbstractMainActivity;

public class MainActivity extends AbstractMainActivity {

    @Override
    protected Class paperFragmentClass() {
        return PaperFragment.class;
    }

    @Override
    protected Class settingsFragmentClass() {
        return SettingsFragment.class;
    }

    @Override
    protected String reviewURI() {
        return "tabcomputing.chronochrome.checkers";
    }

}
