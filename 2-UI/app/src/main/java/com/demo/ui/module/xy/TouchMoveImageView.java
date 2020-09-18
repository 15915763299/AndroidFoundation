package com.demo.ui.module.xy;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.demo.ui.listener.OnLogListener;

/**
 * @author 尉迟涛
 * create time : 2019/11/20 11:20
 * description :
 */
public class TouchMoveImageView extends AppCompatImageView {

    private static final String TAG = TouchMoveImageView.class.getSimpleName();
    private float lastX, lastY;

    public TouchMoveImageView(Context context) {
        super(context);
        init();
    }

    public TouchMoveImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchMoveImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                logE("-----ACTION_DOWN-----");
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int mX = (int) (lastX - event.getX());
                int mY = (int) (lastY - event.getY());
                logE("-----ACTION_MOVE: (" + mX + "," + mY + ")");
                scrollBy(mX, mY);
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                logE("-----ACTION_UP");
                lastX = event.getX();
                lastY = event.getY();
                break;
            default:
        }

        logE("X: " + event.getX() + ", rowX: " + event.getRawX() + ", scrX: " + getScrollX());
        logE("Y: " + event.getY() + ", rowY: " + event.getRawY() + ", scrY: " + getScrollY());
        return super.onTouchEvent(event);
    }

    private void logE(String info) {
        Log.e(TAG, info);
        onLogListener.log(info);
    }

    private OnLogListener onLogListener;

    public void setOnLogListener(OnLogListener onLogListener) {
        this.onLogListener = onLogListener;
    }
}
