package com.demo.ui.module.gesture;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;

/**
 * @author 尉迟涛
 * create time : 2019/11/21 0:27
 * description : TODO 双击爱心，放大缩小ImageView
 */
public class ActGesture extends AppCompatActivity implements View.OnTouchListener {

    private TextView txLog;
    private StringBuilder sb = new StringBuilder();
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gesture);

        TextView txGesture = findViewById(R.id.tx_gesture);
        txGesture.setOnTouchListener(this);
        TextView txScaleGesture = findViewById(R.id.tx_scale_gesture);
        txScaleGesture.setOnTouchListener(this);
        txLog = findViewById(R.id.tx_log);
        findViewById(R.id.btn_clear).setOnClickListener((View v) -> {
            sb = new StringBuilder();
            txLog.setText("");
        });

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            //单击：onDown => onSingleTapUp => onSingleTapConfirmed
            //长按：onDown => onShowPress => onLongPress
            //快速滑动：onDown => onScroll * n => onFling
            //双击：onDown => onSingleTapUp => onDoubleTap => onDoubleTapEvent =>
            //      onDown => onDoubleTapEvent * n

            @Override
            public boolean onDown(MotionEvent e) {
                showLog("onDown");
                return false;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //用户手指松开（UP事件）的时候如果没有执行onScroll()和onLongPress()这两个回调的话，就会回调这个，
                // 说明这是一个点击抬起事件，但是不能区分是否双击事件的抬起。
                showLog("onSingleTapUp");
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //可以确认（通过单击DOWN后300ms没有下一个DOWN事件确认）这不是一个双击事件，而是一个单击事件的时候会回调
                showLog("onSingleTapConfirmed");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                //用户按下按键后100ms（根据Android7.0源码）还没有松开或者移动就会回调
                showLog("onShowPress");
            }

            @Override
            public void onLongPress(MotionEvent e) {
                showLog("onLongPress");
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                showLog("onScroll");
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //用户执行抛操作之后的回调
                showLog("onFling");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //可以确认这是一个双击事件的时候回调
                showLog("onDoubleTap");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                // onDoubleTap()回调之后的输入事件（DOWN、MOVE、UP）都会回调这个方法
                // （这个方法可以实现一些双击后的控制，如让View双击后变得可拖动等）。
                showLog("onDoubleTapEvent");
                return false;
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                //当鼠标/触摸板，右键点击时候的回调
                showLog("onContextClick");
                return false;
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                showLog("- focusXY: " + detector.getFocusX() + ", " + detector.getFocusY());
                showLog("- span: " + detector.getCurrentSpan() + ", factor: " + detector.getScaleFactor());
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                showLog("B focusXY: " + detector.getFocusX() + ", " + detector.getFocusY());
                showLog("B spanXY: " + detector.getCurrentSpanX() + ", " + detector.getCurrentSpanY());
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                showLog("E focusXY: " + detector.getFocusX() + ", " + detector.getFocusY());
                showLog("E spanXY: " + detector.getCurrentSpanX() + ", " + detector.getCurrentSpanY());
            }
        });
    }

    private void showLog(String info) {
        sb.append("\n").append(info);
        txLog.setText(sb.toString());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.tx_gesture) {
            gestureDetector.onTouchEvent(event);
        } else if (id == R.id.tx_scale_gesture) {
            scaleGestureDetector.onTouchEvent(event);
        }
        return false;
    }
}
