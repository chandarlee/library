package lee.library.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class KeyboardUtil {

    public static void showKeyboard(final View view) {
        if (view == null) {
            return;
        }
        view.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    /**
     * {@link #showKeyboard(View)}方法在dialog回来，有无法弹开键盘的情况发生
     *
     * 可以试试这个方法
     * */
    public static void showKeyboardEx(View view){
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isActive(view)){
            view.requestFocus();
            imm.toggleSoftInput(0, 0);
        }
    }

    public static void hideKeyboard(final View view) {
        if (view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.clearFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isActiveForInput(View view){
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive(view);
    }

    /**
     * 点击EditText外的区域时隐藏键盘,需要重写Activity的dispatchTouchEvent方法，并调用此方法
     * @param activity
     * @param ev
     */
    public static boolean hideKeyboardOnOutsideTouch(Activity activity, MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN){
            View curFocus = activity.getCurrentFocus();
            if (curFocus instanceof EditText && isActiveForInput(curFocus)){
                Rect rect = new Rect();
                curFocus.getGlobalVisibleRect(rect);
                if (!rect.contains((int)ev.getRawX(), (int)ev.getRawY())){
                    hideKeyboard(curFocus);
                    return true;
                }
            }
        }
        return false;
    }

}
