package lee.library.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;

/**
 * Created by chandarlee on 17-4-18.
 * 用于解决嵌套在ScrollView里面的滑动冲突
 */

public class ScrollControlWebView extends WebView{

    private static final String TAG = "ScrollControlWebView";

    ViewGroup mScrollableParent;
    float mDownY;

    public ScrollControlWebView(Context context) {
        super(context);
    }

    public ScrollControlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollControlWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findFirstScrollableParent();
    }

    void findFirstScrollableParent(){
        ViewParent parent = getParent();
        while (parent instanceof ViewGroup && !ScrollView.class.isAssignableFrom(parent.getClass())){
            parent = parent.getParent();
        }

        if (parent instanceof ViewGroup){
            mScrollableParent = (ViewGroup) parent;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i(TAG, "onTouchEvent down: scrollY:" + getScrollY() + " top:" + getTopRelativeScrollableParent() + " parent scrollY:" + mScrollableParent.getScrollY());
        int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN){
            if (getScrollY() > 0 || getTopRelativeScrollableParent() - mScrollableParent.getScrollY() <= 0){
                mDownY = event.getY();
                requestDisallowInterceptTouchEvent(true);
                super.onTouchEvent(event);
                return true;
            }
        }else if (actionMasked == MotionEvent.ACTION_MOVE){
            if (getScrollY() == 0 && event.getY() - mDownY > 0){
                requestDisallowInterceptTouchEvent(false);
            }
        }
        return super.onTouchEvent(event);
    }

    private int getTopRelativeScrollableParent(){
        if (mScrollableParent != null){
            int top = getTop();
            ViewParent parent = getParent();
            while (parent != mScrollableParent){
                top += ((ViewGroup)parent).getTop();
                parent = parent.getParent();
            }
            return top;
        }
        return Integer.MAX_VALUE;
    }
}
