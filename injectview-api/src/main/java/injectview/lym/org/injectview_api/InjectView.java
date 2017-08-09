package injectview.lym.org.injectview_api;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import injectview.lym.org.injectview_api.finder.ActivityFinder;
import injectview.lym.org.injectview_api.finder.Finder;
import injectview.lym.org.injectview_api.finder.ViewFinder;

/**
 * 向外提供View注入
 *
 * @author yaoming.li
 * @since 2017-08-08 15:25
 */
public class InjectView {

    private static final Map<String, ViewInject> INJECT_MAP = new HashMap<>();
    private static final ViewFinder VIEW_FINDER = new ViewFinder();
    private static final ActivityFinder ACTIVITY_FINDER = new ActivityFinder();


    public static void inject(Activity activity) {
        inject(activity, activity, ACTIVITY_FINDER);
    }

    public static void inject(View view) {
        inject(view, view, VIEW_FINDER);
    }

    public static void inject(Object o, View view) {
        inject(o, view, VIEW_FINDER);
    }

    private static void inject(Object object, Object source, Finder finder) {
        String className = object.getClass().getName();
        ViewInject viewInject = INJECT_MAP.get(className);
        if (null == viewInject) {
            try {
                Class<?> clazz = Class.forName(className + "$$Inject");
                viewInject = (ViewInject) clazz.newInstance();
                INJECT_MAP.put(className, viewInject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        viewInject.inject(object, source, finder);
    }

}
