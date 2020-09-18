package com.demo.ui.module.dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/18 20:43
 * description :
 */
public class DispatchView extends View {

    private static final String TAG = DispatchView.class.getSimpleName();

    public DispatchView(Context context) {
        super(context);
    }

    public DispatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void logE(String info, MotionEvent ev) {
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
        info = "View:           " + info;
        Log.e(TAG, info);
        if (onLogListener != null) {
            onLogListener.log(info);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        logE("dispatchTouchEvent", ev);
        if (dispatchType == 0) {
            return false;
        } else if (dispatchType == 1) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        logE("onTouchEvent", event);
        if (touchReturnType == 0) {
            return false;
        } else if (touchReturnType == 1) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private OnLogListener onLogListener;
    private int touchReturnType = 2;
    private int dispatchType = 2;

    public void setOnLogListener(OnLogListener onLogListener) {
        this.onLogListener = onLogListener;
    }

    public int getTouchReturnType() {
        return touchReturnType;
    }

    public void setTouchReturnType(int touchReturnType) {
        this.touchReturnType = touchReturnType;
    }

    public int getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(int dispatchType) {
        this.dispatchType = dispatchType;
    }
}
