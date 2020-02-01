package com.demo.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 13:23
 * description :
 */
public class ActMain extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
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
            default:
                clzz = null;
        }

        if (clzz != null) {
            Intent intent = new Intent(this, clzz);
            startActivity(intent);
        }
    }
}
