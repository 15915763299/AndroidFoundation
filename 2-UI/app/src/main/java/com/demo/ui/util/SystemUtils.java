package com.demo.ui.util;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.demo.ui.App;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 尉迟涛
 * create time : 2019/12/1 0:33
 * description :
 */
public class SystemUtils {

    /**
     * 通过反射获取通知的开关状态
     */
    public static boolean isNotificationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = (NotificationManager) App.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                return manager.areNotificationsEnabled();
            }
        } else {
            AppOpsManager mAppOps = (AppOpsManager) App.getApp().getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = App.getApp().getApplicationInfo();
            String pkg = App.getApp().getPackageName();

            int uid = appInfo.uid;
            Class appOpsClass; /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(
                        "checkOpNoThrow",//NotificationManagerCompat.CHECK_OP_NO_THROW,
                        Integer.TYPE, Integer.TYPE, String.class
                );
                Field opPostNotificationValue = appOpsClass.getDeclaredField(
                        "OP_POST_NOTIFICATION"//NotificationManagerCompat.OP_POST_NOTIFICATION
                );
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
