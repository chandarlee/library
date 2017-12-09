package lee.library.base.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * Created by hzlichengda on 2016/7/13.
 */
public class SelectorUtils {

    private static final int SDK_INT = Build.VERSION.SDK_INT;

    public static GradientDrawable getRectDrawable(Context context, @ColorRes int strokeColorId, @ColorRes int solidColorId){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(2, getColorInt(context, strokeColorId), 0, 0);
        gradientDrawable.setColor(getColorInt(context, solidColorId));
        return gradientDrawable;
    }

    public static Drawable getRoundRectDrawable(Context context, int strokeColorId, int solidColorId, float radius, int[] size){
        GradientDrawable drawable = getRectDrawable(context, strokeColorId, solidColorId);
        drawable.setCornerRadius(radius);
        if (size != null && size.length >=2){
            drawable.setSize(size[0], size[1]);
        }
        return drawable;
    }

    public static Drawable getRoundRectDrawable(Context context, int strokeColorId, int solidColorId, float radius){
        return getRoundRectDrawable(context, strokeColorId, solidColorId, radius, null);
    }

    public static Drawable getRoundRectDrawable(Context context, int strokeColorId, int solidColorId, float[] radii, int[] size){
        GradientDrawable drawable = getRectDrawable(context, strokeColorId, solidColorId);
        drawable.setCornerRadii(radii);
        if (size != null && size.length >=2){
            drawable.setSize(size[0], size[1]);
        }
        return drawable;
    }

    public static Drawable getRoundRectDrawable(Context context, int strokeColorId, int solidColorId, float[] radii){
        return getRoundRectDrawable(context, strokeColorId, solidColorId, radii, null);
    }

    public static Drawable getSimpleSelector(Context context, @DrawableRes int pressedDrawableId, @DrawableRes int normalDrawableId){
        if (pressedDrawableId == 0 || normalDrawableId == 0) return null;
        return getSimpleSelector(getDrawable(context, pressedDrawableId), getDrawable(context, normalDrawableId));
    }

    public static Drawable getSimpleSelector(Drawable pressedDrawable, Drawable normalDrawable){
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        selector.addState(new int[]{0}, normalDrawable);
        return selector;
    }

    public static void setSimpleSelectorBackground(View view, Drawable pressedDrawable, Drawable normalDrawable){
        setBackground(view, getSimpleSelector(pressedDrawable, normalDrawable));
    }

    public static void setBackground(View view, Drawable drawable){
        if (view != null){
            if (versionCompatible(Build.VERSION_CODES.JELLY_BEAN)) {
                view.setBackground(drawable);
            }else {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    public static Drawable getDrawable(Context context, int drawableId){
        if (drawableId == 0) return null;
        if (versionCompatible(Build.VERSION_CODES.LOLLIPOP)){
            return context.getDrawable(drawableId);
        }else {
            return context.getResources().getDrawable(drawableId);
        }
    }

    public static int getColorInt(Context context, @ColorRes int colorId){
        if (colorId == 0) return 0;
        if (versionCompatible(Build.VERSION_CODES.M)){
            return context.getColor(colorId);
        }else {
            return context.getResources().getColor(colorId);
        }
    }

    private static boolean versionCompatible(int version){
        return SDK_INT >= version;
    }
}
