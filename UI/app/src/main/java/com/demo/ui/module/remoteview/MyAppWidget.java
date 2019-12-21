package com.demo.ui.module.remoteview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.demo.ui.R;

/**
 * @author 尉迟涛
 * create time : 2019/12/1 21:19
 * description :
 */
public class MyAppWidget extends AppWidgetProvider {

    private static final String TAG = MyAppWidget.class.getSimpleName();

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i(TAG, "onDeleted");
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法，注意是最后一个
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.i(TAG, "onDisabled");
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只第一次调用
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i(TAG, "onEnabled");
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "onReceive: action=" + intent.getAction());

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            String action = intent.getStringExtra("action");
            if ("click".equals(action)) {
                new Thread(() -> {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i <= 10; i++) {
                        RemoteViews remoteViews = newRemoteView(context, i);
                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(
                                new ComponentName(context, MyAppWidget.class),
                                remoteViews
                        );
                        SystemClock.sleep(50);
                    }
                }).start();
                return;
            }
        }
        super.onReceive(context, intent);
    }

    private RemoteViews newRemoteView(Context context, int value) {
        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.view_remote_widget
        );
        remoteViews.setTextViewText(R.id.tx, String.valueOf(value));
        remoteViews.setImageViewResource(R.id.img, R.mipmap.ic_fish);

        // Set click
        // 这里使用自定义Action的话，widget无法接收，一定要使用已定义的
        Intent clickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra("action", "click");
        PendingIntent pd1 = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.img, pd1);

        Intent jumpIntent = new Intent(context, ActRemoteView.class);
        jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pd2 = PendingIntent.getActivity(context, 0, jumpIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn, pd2);
        return remoteViews;
    }

    /**
     * 每次窗口小部件被点击更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = newRemoteView(context, 0);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(
                appWidgetId,
                remoteViews
        );
    }
}
