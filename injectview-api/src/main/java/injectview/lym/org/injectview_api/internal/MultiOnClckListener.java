package injectview.lym.org.injectview_api.internal;

import android.view.View;

/**
 * 主要是为了应对同时绑定多个点击事件
 *
 * @author yaoming.li
 * @since 2017-08-08 20:36
 */
public abstract class MultiOnClckListener implements View.OnClickListener {

    private static boolean mEnable = true;

    private static final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mEnable = true;
        }
    };

    @Override
    public void onClick(View view) {
        if (mEnable) {
            mEnable = false;
            view.post(mRunnable);
            doClick(view);
        }
    }

    public abstract void doClick(View view);

}
