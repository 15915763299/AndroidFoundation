package com.demo.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.demo.ui.App;

/**
 * @author 尉迟涛
 * create time : 2019/11/19 17:58
 * description :
 */
public class DisplayUtils {

    public static int dip2px(float dipValue) {
        float density = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    public static int sp2px(float spValue) {
        float scaledDensity = App.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaledDensity + 0.5f);
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        return dm;
    }
}
