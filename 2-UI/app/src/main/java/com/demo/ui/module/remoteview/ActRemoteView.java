package com.demo.ui.module.remoteview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.ui.R;
import com.demo.ui.util.SystemUtils;


/**
 * @author 尉迟涛
 * create time : 2019/11/29 21:04
 * description : RemoteView的使用
 */
public class ActRemoteView extends Activity implements View.OnClickListener {

    private static final String TAG = ActRemoteView.class.getSimpleName();
    private NotificationManager manager;
    private int notifyId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_remote_view);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //从6.0开始需要自己创建channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(TAG, TAG, NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);

        TextView tx = findViewById(R.id.tx);
        tx.setText("PID: " + Process.myPid());
        initRemoteReceiver();
    }

    private void sendNotification(RemoteViews remoteViews) {
        Intent intent = new Intent(this, ActRemoteViewJump.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, TAG);
        } else {
            builder = new Notification.Builder(this);
        }

        builder.setContentTitle("通知")
                .setContentText("你的鱼要被吃了")
                //.setTicker("你的鱼要被吃了")
                //创建时间
                .setWhen(System.currentTimeMillis())
                //设置显示在系统状态栏上的小图标
                .setSmallIcon(R.mipmap.ic_fish)
                //.setLargeIcon(设置通知的大图标)
                .setContentIntent(pendingIntent)
                //设置点击取消
                .setAutoCancel(true);
        if (remoteViews != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setCustomContentView(remoteViews);
            } else {
                builder.setContent(remoteViews);
            }
        }

        if (manager != null) {
            manager.notify(notifyId++, builder.build());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                try {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        intent.putExtra(Settings.EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);
                    }

                    // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
                    //  if ("MI 6".equals(Build.MODEL)) {
                    //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                    //      intent.setData(uri);
                    //      // intent.setAction("com.android.settings/.SubSettings");
                    //  }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                    Intent intent = new Intent();

                    //下面这种方案是直接跳转到当前应用的设置界面。
                    //https://blog.csdn.net/ysy950803/article/details/71910806
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
            case R.id.btn2:
                if (!SystemUtils.isNotificationEnabled()) {
                    //此时不能Toast
                    new AlertDialog.Builder(this)
                            .setMessage("请打开应用发送通知权限")
                            .create()
                            .show();
                } else {
                    sendNotification(null);
                }
                break;
            case R.id.btn3:
                if (!SystemUtils.isNotificationEnabled()) {
                    new AlertDialog.Builder(this)
                            .setMessage("请打开应用发送通知权限")
                            .create()
                            .show();
                } else {
                    RemoteViews remoteView = new RemoteViews(
                            getPackageName(), R.layout.view_remote_msg
                    );
                    remoteView.setTextViewText(R.id.tx, "你的鱼要被吃了");
                    remoteView.setImageViewResource(R.id.img, R.mipmap.ic_fish);
                    sendNotification(remoteView);
                }
                break;
            case R.id.btn4:
                Intent intent = new Intent(this, ActRemoteViewJump.class);
                startActivity(intent);
                break;
            default:
        }
    }

    //*****************************************************************************

    private LinearLayout content;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = intent
                    .getParcelableExtra(ActRemoteViewJump.EXTRA_REMOTE_VIEWS);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    private void initRemoteReceiver() {
        content = findViewById(R.id.remote_views_content);
        IntentFilter filter = new IntentFilter(ActRemoteViewJump.REMOTE_ACTION);
        registerReceiver(receiver, filter);
    }

    private void updateUI(RemoteViews remoteViews) {
        // View view = remoteViews.apply(this, mRemoteViewsContent);
        // 在不同应用中Id的值不一定相同，需要用以下方法获取布局文件的Id
        int layoutId = getResources().getIdentifier("view_remote_msg", "layout", getPackageName());
        View view = getLayoutInflater().inflate(layoutId, content, false);

        // view: android.support.v7.widget.AppCompatImageView can't use method with RemoteViews: setImageResource(int)
        // 没有RemotableViewMethod注解的View是不能被RemoteViews设置属性值的。
        // AppCompatImageView类中对应的setImageViewResource()方法是没有RemotableViewMethod注解的
        // 当Activity使用的是AppCompatActivity时也要改为Activity。
        // 在AppCompatActivity中布局中使用的ImageView和TextView等也会在加载布局时被转换成对应的兼容类，
        // 兼容类都是这些类的子类，有些方法被继承后没有加上RemotableViewMethod注解，不能被RemoteViews使用
        remoteViews.reapply(this, view);
        content.addView(view);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
