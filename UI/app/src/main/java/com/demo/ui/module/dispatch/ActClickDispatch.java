package com.demo.ui.module.dispatch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        DispatchViewGroup view_group = findViewById(R.id.view_group);
        view_group.setOnLogListener(this);
        view_group.setOnTouchListener(this);
        view_group.setOnClickListener(this);
        DispatchView view = findViewById(R.id.view);
        view.setOnLogListener(this);
        view.setOnTouchListener(this);
        view.setOnClickListener(this);
        tx = findViewById(R.id.tx);

        findViewById(R.id.btn_clear).setOnClickListener((View v) -> {
            sb = new StringBuilder();
            tx.setText("");
        });

        Button btn_change1 = findViewById(R.id.btn_change1);
        btn_change1.setOnClickListener((View v) -> {
            int type = view_group.getInterceptType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view_group.setInterceptType(index);
            btn_change1.setText(returnType[index]);
        });

        Button btn_change2 = findViewById(R.id.btn_change2);
        btn_change2.setOnClickListener((View v) -> {
            int type = view_group.getTouchReturnType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view_group.setTouchReturnType(index);
            btn_change2.setText(returnType[index]);
        });

        Button btn_change3 = findViewById(R.id.btn_change3);
        btn_change3.setOnClickListener((View v) -> {
            int type = view_group.getDispatchType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view_group.setDispatchType(index);
            btn_change3.setText(returnType[index]);
        });

        Button btn_change4 = findViewById(R.id.btn_change4);
        btn_change4.setOnClickListener((View v) -> {
            int type = view_group.getInterceptActionType();
            int index = type + 1 > 2 ? -1 : type + 1;
            view_group.setInterceptActionType(index);
            btn_change4.setText(actionType[index + 1]);
        });

        Button btn_change5 = findViewById(R.id.btn_change5);
        btn_change5.setOnClickListener((View v) -> {
            int type = view.getDispatchType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view.setDispatchType(index);
            btn_change5.setText(returnType[index]);
        });

        Button btn_change6 = findViewById(R.id.btn_change6);
        btn_change6.setOnClickListener((View v) -> {
            int type = view.getTouchReturnType();
            int index = type + 1 > 2 ? 0 : type + 1;
            view.setTouchReturnType(index);
            btn_change6.setText(returnType[index]);
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
