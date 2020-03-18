package com.demo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.demo.ui.module.anim.ActAnim;
import com.demo.ui.module.customize2.ActCustomizeView2;
import com.demo.ui.module.drawable.ActDrawable;
import com.demo.ui.module.remoteview.ActRemoteView;
import com.demo.ui.module.customize.ActCustomizeView;
import com.demo.ui.module.dispatch.ActClickDispatch;
import com.demo.ui.module.gesture.ActGesture;
import com.demo.ui.module.conflict.ActScrollConflict;
import com.demo.ui.module.xy.ActXY;
import com.demo.ui.module.scroll.ActSmoothScroll;

public class ActMain extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6_1).setOnClickListener(this);
        findViewById(R.id.btn6_2).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class<?> cls;
        switch (v.getId()) {
            case R.id.btn1:
                cls = ActXY.class;
                break;
            case R.id.btn2:
                cls = ActClickDispatch.class;
                break;
            case R.id.btn3:
                cls = ActSmoothScroll.class;
                break;
            case R.id.btn4:
                cls = ActGesture.class;
                break;
            case R.id.btn5:
                cls = ActScrollConflict.class;
                break;
            case R.id.btn6_1:
                cls = ActCustomizeView.class;
                break;
            case R.id.btn6_2:
                cls = ActCustomizeView2.class;
                break;
            case R.id.btn7:
                cls = ActRemoteView.class;
                break;
            case R.id.btn8:
                cls = ActDrawable.class;
                break;
            case R.id.btn9:
                cls = ActAnim.class;
                break;
            default:
                cls = null;
        }

        if (cls != null) {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        }
    }
}
