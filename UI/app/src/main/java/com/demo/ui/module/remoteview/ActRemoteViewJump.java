package com.demo.ui.module.remoteview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.demo.ui.R;

/**
 * @author 尉迟涛
 * create time : 2019/11/29 22:32
 * description :
 */
public class ActRemoteViewJump extends Activity {

    public static final String REMOTE_ACTION = "action.REMOTE";
    public static final String EXTRA_REMOTE_VIEWS = "remoteViews";
    private int num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_remote_view_jump);

        TextView tx = findViewById(R.id.tx);
        tx.setText("PID: " + Process.myPid());
    }

    public void onClick(View v) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_remote_msg);
        remoteViews.setImageViewResource(R.id.img, R.mipmap.ic_fish);
        remoteViews.setTextViewText(R.id.tx, "我来自PID为" + Process.myPid() + "的进程，我是" + num++ + "号");

        Intent intent = new Intent(REMOTE_ACTION);
        intent.putExtra(EXTRA_REMOTE_VIEWS, remoteViews);
        sendBroadcast(intent);
    }
}
