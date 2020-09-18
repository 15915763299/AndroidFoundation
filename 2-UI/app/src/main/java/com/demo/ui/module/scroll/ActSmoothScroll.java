package com.demo.ui.module.scroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;
import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/19 11:28
 * description : 惯性滑动实现（Scroller + VelocityTracker）
 */
public class ActSmoothScroll extends AppCompatActivity implements
        OnLogListener, View.OnClickListener, OnScrollListener, View.OnLayoutChangeListener {

    private TextView tx, txDetail;
    private SmoothScrollView smoothScrollView;
    private EditText edt;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("---------onCreate--------");
        setContentView(R.layout.act_smooth_scroll);

        tx = findViewById(R.id.tx);
        edt = findViewById(R.id.edt);
        txDetail = findViewById(R.id.tx_detail);
        smoothScrollView = findViewById(R.id.smooth_scroll_view);
        smoothScrollView.setOnLogListener(this);
        smoothScrollView.setScrollListener(this);
        smoothScrollView.addOnLayoutChangeListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_set).setOnClickListener(this);
        findViewById(R.id.btn_other_impl).setOnClickListener(this);

        tx.post(() -> tx.setText(sb.toString()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_set) {
            String numStr = edt.getText().toString();
            if (numStr.length() > 0) {
                smoothScrollView.setNumberCount(Integer.parseInt(numStr));
            }
        } else if (id == R.id.btn_clear) {
            sb = new StringBuilder();
            tx.setText("");
        } else if (id == R.id.btn_other_impl) {
            Intent intent = new Intent(this, ActOtherSmoothScroll.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("---------onStart--------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("---------onResume--------");
    }

    @Override
    public void log(String info) {
        if (sb.toString().length() != 0) {
            sb.append("\n");
        }
        sb.append(info);
        if (tx != null) {
            tx.setText(sb.toString());
        }
    }

    @Override
    public void onMove(float scrollX, float scrollLengthX, float endX, float viewOffset) {
        txDetail.setText(scrollX + "\n" + scrollLengthX + "\n" + endX + "\n" + viewOffset);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        log("onLayoutChange");
    }
}
