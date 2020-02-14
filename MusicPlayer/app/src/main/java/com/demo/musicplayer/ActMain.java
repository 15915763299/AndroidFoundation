package com.demo.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActMain extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ActMain.class.getSimpleName();

    private TextView tx_title, tx_author;
    private ImageView btn_play;

    private Receiver receiver;
    private IServiceInterface binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            // 同一进程，直接强转
            binder = (IServiceInterface) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx_title = findViewById(R.id.tx_title);
        tx_author = findViewById(R.id.tx_author);
        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        // 注册 receiver 添加过滤器
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAG);
        registerReceiver(receiver, intentFilter);

        // 绑定 Service
        Intent intent = new Intent(this, SerMusic.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                binder.play();
                break;
            case R.id.btn_next:
                binder.next();
                break;
            case R.id.btn_stop:
                binder.stop();
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void cleanMusicInfo() {
        String none = "------";
        tx_author.setText(none);
        tx_title.setText(none);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive");
            int status = intent.getIntExtra(SerMusic.EXTRA_STATUS, -1);
            MusicInfo musicInfo = intent.getParcelableExtra(SerMusic.EXTRA_MUSIC_INFO);
            if (musicInfo != null) {
                tx_title.setText(musicInfo.getTitle());
                tx_author.setText(musicInfo.getAuthor());
            }

            // 根据回传状态更新按钮图片
            switch (status) {
                case SerMusic.STATUS_STOPPING:
                    btn_play.setImageResource(R.mipmap.ic_play);
                    cleanMusicInfo();
                    break;
                case SerMusic.STATUS_PLAYING:
                    btn_play.setImageResource(R.mipmap.ic_pause);
                    break;
                case SerMusic.STATUS_PAUSING:
                    btn_play.setImageResource(R.mipmap.ic_play);
                    break;
                default:
                    break;
            }
        }
    }
}
