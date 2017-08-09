package injectview.lym.org.injectview_api.finder;

import android.app.Activity;
import android.view.View;

/**
 *
 * @author yaoming.li
 * @since 2017-08-08 17:46
 */
public class ViewFinder implements Finder {
    @Override
    public View findView(Object o, int id) {
        return ((Activity)o).findViewById(id);
    }
}
