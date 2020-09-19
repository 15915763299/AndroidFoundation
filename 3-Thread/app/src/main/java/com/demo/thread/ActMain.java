package com.demo.thread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.thread.thread.ActAsyncTask;
import com.demo.thread.thread.ActEmpty;
import com.demo.thread.thread.ActIntentService;
import com.demo.thread.thread.ActThreadPriority;
import com.demo.thread.thread.ActThreadPriority2;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 13:23
 * description :
 */
public class ActMain extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn7_1).setOnClickListener(this);
        findViewById(R.id.btn7_2).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class<?> clzz;
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                clzz = ActAsyncTask.class;
                break;
            case R.id.btn2:
                clzz = ActIntentService.class;
                break;
            case R.id.btn7_1:
                clzz = ActThreadPriority.class;
                break;
            case R.id.btn7_2:
                clzz = ActThreadPriority2.class;
                break;
            case R.id.btn8:
                clzz = ActEmpty.class;
                break;
            default:
                clzz = null;
        }

        if (clzz != null) {
            Intent intent = new Intent(this, clzz);
            startActivity(intent);
        }
    }

}
