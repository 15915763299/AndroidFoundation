package com.demo.ui.module.xy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.demo.ui.R;
import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/18 22:28
 * description :
 */
public class ActXY extends AppCompatActivity implements
        OnLogListener, View.OnClickListener {

    private TouchMoveImageView img;
    private TextView tx;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xy);

        tx = findViewById(R.id.tx);
        img = findViewById(R.id.img);
        img.setOnLogListener(this);

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
    }

    @Override
    public void log(String info) {
        sb.append("\n").append(info);
        tx.setText(sb.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_clear) {
            sb = new StringBuilder();
            tx.setText("");
        } else if (id == R.id.btn_reset) {
            img.scrollTo(0, 0);
        }
    }
}
