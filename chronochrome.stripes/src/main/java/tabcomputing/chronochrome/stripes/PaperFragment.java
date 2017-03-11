package tabcomputing.chronochrome.stripes;

import tabcomputing.library.paper.AbstractPaperFragment;
import tabcomputing.library.paper.AbstractPaperView;
import tabcomputing.library.paper.CommonSettings;

public class PaperFragment extends AbstractPaperFragment {

    @Override
    protected AbstractPaperView paperView() {
        return new PaperView(getActivity().getApplicationContext());
    }

    @Override
    protected CommonSettings getSettings() {
        return Settings.getInstance();
    }

}
