package com.demo.ui.module.dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/18 20:31
 * description :
 */
public class DispatchViewGroup extends FrameLayout {

    private static final String TAG = DispatchViewGroup.class.getSimpleName();

    public DispatchViewGroup(Context context) {
        super(context);
    }

    public DispatchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
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
        info = "ViewGroup: " + info;
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        logE("onInterceptTouchEvent", ev);


        if (ev.getAction() == interceptActionType) {
            return true;
        }
        // 如果是true，才会调用ViewGroup的OnTouchEvent
        // 否则调View的，此时View还会调用自己的dispatchTouchEvent，再调用自己的OnTouchEvent
        if (interceptType == 0) {
            return false;
        } else if (interceptType == 1) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //禁止InterceptTouchEvent的调用，包括所有父View
        //mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT; 这种操作就是取消DISALLOW_INTERCEPT，在DOWN事件时会执行
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private OnLogListener onLogListener;
    private int interceptType = 2;
    private int touchReturnType = 2;
    private int dispatchType = 2;
    private int interceptActionType = -1;

    public void setOnLogListener(OnLogListener onLogListener) {
        this.onLogListener = onLogListener;
    }

    public int getInterceptType() {
        return interceptType;
    }

    public void setInterceptType(int interceptType) {
        this.interceptType = interceptType;
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

    public int getInterceptActionType() {
        return interceptActionType;
    }

    public void setInterceptActionType(int interceptActionType) {
        this.interceptActionType = interceptActionType;
    }
}
