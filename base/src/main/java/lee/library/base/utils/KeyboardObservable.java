package lee.library.base.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by chandarlee on 17-1-21.
 */

public class KeyboardObservable{

    private final MessageQueue.IdleHandler mKeyboardChecker = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            mCallback.onChange(isKeyboardShowing(mRootView));
            return false;
        }
    };

    private View mRootView;
    private Callback mCallback;

    public KeyboardObservable(Activity activity, Callback callback) {
        this(activity.getWindow().getDecorView(), callback);
    }

    public KeyboardObservable(View view, Callback callback) {
        if (callback == null || view == null){
            throw new NullPointerException("empty view or callback");
        }
        this.mCallback = callback;
        this.mRootView = view.getRootView();
    }

    public void start(){
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Looper.myQueue().removeIdleHandler(mKeyboardChecker);
                Looper.myQueue().addIdleHandler(mKeyboardChecker);
            }
        });
    }

    public void end(){
        Looper.myQueue().removeIdleHandler(mKeyboardChecker);
        mRootView = null;
        mCallback = null;
    }

    public interface Callback{
        void onChange(boolean show);
    }

    public static boolean isKeyboardShowing(Activity activity){
        return isKeyboardShowing(activity.getWindow().getDecorView());
    }

    public static boolean isKeyboardShowing(View view){
        if (view != null){
            View decor = view.getRootView(); //it is decor view
            //we assume that soft keyboard will occupy at least 1/3 of the display height
            final int softKeyboardHeight = view.getResources().getDisplayMetrics().heightPixels / 3;
            Rect r = new Rect();
            decor.getWindowVisibleDisplayFrame(r);
            int heightDiff = decor.getBottom() - r.bottom;
            return heightDiff > softKeyboardHeight;
        }
        return false;
    }
}
