package com.demo.ui.module.dispatch;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;
import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/18 16:06
 * description : 分析点击事件的分发
 */
public class ActClickDispatch extends AppCompatActivity implements
        View.OnTouchListener, View.OnClickListener, OnLogListener {

    private static final String TAG = ActClickDispatch.class.getSimpleName();
    private TextView tx;
    private StringBuilder sb = new StringBuilder();
    private String[] returnType = {"false", "true", "super"};
    private String[] actionType = {"none", "down", "up", "move"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_click_dispatch);

        DispatchViewGroup viewGroup = findViewById(R.id.view_group);
        viewGroup.setOnLogListener(this);
        viewGroup.setOnTouchListener(this);
        viewGroup.setOnClickListener(this);
        DispatchView view = findViewById(R.id.view);
        view.setOnLogListener(this);
        view.setOnTouchListener(this);
        view.setOnClickListener(this);
        tx = findViewById(R.id.tx);

        findViewById(R.id.btn_clear).setOnClickListener((View v) -> {
            sb = new StringBuilder();
            tx.setText("");
        });

        Button btnChange1 = findViewById(R.id.btn_change1);
        btnChange1.setOnClickListener((View v) -> {
            int type = viewGroup.getInterceptType();
            int index = type + 1 > 2 ? 0 : type + 1;
            viewGroup.setInterceptType(index);
            btnChange1.setText(returnType[index]);
        });

        Button btnChange2 = findViewById(R.id.btn_change2);
        btnChange2.setOnClickListener((View v) -> {
            int type = viewGroup.getTouchReturnType();
            int index = type + 1 > 2 ? 0 : type + 1;
            viewGroup.setTouchReturnType(index);
            btnChange2.setText(returnType[index]);
        });

        Button btnChange3 = findViewById(R.id.btn_change3);
        btnChange3.setOnClickListener((View v) -> {
            int type = viewGroup.getDispatchType();
            int index = type + 1 > 2 ? 0 : type + 1;
            viewGroup.setDispatchType(index);
            btnChange3.setText(returnType[index]);
        });

        Button btnChange4 = findViewById(R.id.btn_change4);
        btnChange4.setOnClickListener((View v) -> {
            int type = viewGroup.getInterceptActionType();
            int index = type + 1 > 2 ? -1 : type + 1;
            viewGroup.setInterceptActionType(index);
            btnChange4.setText(actionType[index + 1]);
        });

        Button btnChange5 = findViewById(R.id.btn_change5);
        btnChange5.setOnClickListener((View v) -> {
            int type = view.getDispatchType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view.setDispatchType(index);
            btnChange5.setText(returnType[index]);
        });

        Button btnChange6 = findViewById(R.id.btn_change6);
        btnChange6.setOnClickListener((View v) -> {
            int type = view.getTouchReturnType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view.setTouchReturnType(index);
            btnChange6.setText(returnType[index]);
        });
    }


    private void logE(String info, MotionEvent ev) {
        if (ev != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    info += " DOWN";
                    break;
                case MotionEvent.ACTION_MOVE:
                    info += " MOVE";
                    break;
                case MotionEvent.ACTION_CANCEL:
                    info += " CANCEL";
                    break;
                case MotionEvent.ACTION_UP:
                    info += " UP";
                    break;
                default:
                    info += ev.getAction();
                    break;
            }
        }
        info = "Activity:       " + info;
        Log.e(TAG, info);
        log(info);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        logE("dispatchTouchEvent", ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        logE("onTouchEvent", event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DispatchView) {
            logE("onClick-V", null);
        } else if (v instanceof DispatchViewGroup) {
            logE("onClick-G", null);
        } else {
            logE("onClick", null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof DispatchView) {
            logE("onTouch-V", event);
        } else if (v instanceof DispatchViewGroup) {
            logE("onTouch-G", event);
        } else {
            logE("onTouch", event);
        }
        return false;
    }

    @Override
    public void log(String info) {
        sb.append("\n").append(info);
        tx.setText(sb.toString());
    }


}
