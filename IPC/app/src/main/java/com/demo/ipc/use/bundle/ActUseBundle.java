package com.demo.ipc.use.bundle;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.ipc.R;

/**
 * @author 尉迟涛
 * create time : 2019/11/13 23:05
 * description : 使用Bundle进行进程间通讯。
 * <p>
 * 在A进程的ActUseBundle将信息传递给在B进程的SerRemote（IntentService）处理，最后回传回来并展示。
 */
public class ActUseBundle extends AppCompatActivity implements View.OnClickListener {

    public static final String ACTION = "android.intent.action.UseBundle";

    private EditText edt_start, edt_end;
    private TextView tx;
    private RecUseBundle rec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_bundle);

        edt_start = findViewById(R.id.edt_start);
        edt_end = findViewById(R.id.edt_end);
        tx = findViewById(R.id.tx);
        findViewById(R.id.btn).setOnClickListener(this);

        //注册广播
        rec = new RecUseBundle(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(rec, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rec);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            String start = edt_start.getText().toString();
            String end = edt_end.getText().toString();

            if (start.length() > 0 && end.length() > 0) {
                Intent intent = new Intent(this, SerUseBundleRemote.class);
                intent.putExtra("start", Integer.valueOf(start));
                intent.putExtra("end", Integer.valueOf(end));
                startService(intent);
            }
        }
    }

    public void showText(String str) {
        tx.setText(str);
    }
}
