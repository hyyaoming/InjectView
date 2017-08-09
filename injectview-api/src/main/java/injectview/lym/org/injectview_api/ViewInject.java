package injectview.lym.org.injectview_api;

import injectview.lym.org.injectview_api.finder.Finder;

/**
 *
 * @author yaoming.li
 * @since 2017-08-08 17:20
 */
public interface ViewInject<T> {
    void inject(T object, Object id, Finder finder);
}
