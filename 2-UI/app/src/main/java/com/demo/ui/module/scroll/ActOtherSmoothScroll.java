package com.demo.ui.module.scroll;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2019/11/21 17:08
 * description : 流畅滑动的自定义View
 */
public class ActOtherSmoothScroll extends AppCompatActivity implements View.OnClickListener {

    private static final int MSG_MOVE = 1;

    private static class MainThreadHandler extends Handler {
        private static final int FRAME_COUNT = 100;
        private static final int DELAYED_TIME = 16;
        private WeakReference<ActOtherSmoothScroll> wr;
        private int count = 0;

        MainThreadHandler(ActOtherSmoothScroll act) {
            this.wr = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (wr != null && wr.get() != null) {
                ActOtherSmoothScroll act = wr.get();

                if (msg.what == MSG_MOVE) {
                    if (++count < FRAME_COUNT) {
                        float fraction = count / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 80);
                        act.btnHandler.scrollTo(scrollX, 0);
                        sendEmptyMessageDelayed(MSG_MOVE, DELAYED_TIME);
                    }
                }
            }
        }

        void clear() {
            count = 0;
        }
    }

    private MainThreadHandler handler;
    private Button btnHandler, btnAnimO, btnAnimV;
    private ObjectAnimator animO;
    private ValueAnimator animV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_other_smooth_scroll);
        btnHandler = findViewById(R.id.btn_handler);
        btnHandler.setOnClickListener(this);
        btnAnimO = findViewById(R.id.btn_anim_o);
        btnAnimO.setOnClickListener(this);
        btnAnimV = findViewById(R.id.btn_anim_v);
        btnAnimV.setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        handler = new MainThreadHandler(this);

        animO = ObjectAnimator.ofFloat(btnAnimO, "translationX", 0, 200);
        animO.setDuration(2000);

        animV = ValueAnimator.ofInt(300);
        animV.setDuration(1000);
        animV.addUpdateListener((ValueAnimator animation) -> {
            int value = (int) animation.getAnimatedValue();
            btnAnimV.scrollTo(value, 0);
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_handler) {
            handler.sendEmptyMessage(MSG_MOVE);
        } else if (id == R.id.btn_anim_o) {
            animO.start();
        } else if (id == R.id.btn_anim_v) {
            animV.start();
        } else if (id == R.id.btn_reset) {
            handler.clear();
            btnHandler.scrollTo(0, 0);
            btnAnimO.setTranslationX(0);
            btnAnimV.scrollTo(0, 0);
        }
    }
}
