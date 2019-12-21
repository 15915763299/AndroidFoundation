package com.demo.ui.module.scroll;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.demo.ui.R;
import com.demo.ui.module.conflict.HorizontalScrollView;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2019/11/21 17:08
 * description :
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
                        act.btn_handler.scrollTo(scrollX, 0);
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
    private Button btn_handler, btn_anim_o, btn_anim_v;
    private ObjectAnimator animO;
    private ValueAnimator animV;

    private HorizontalScrollView scroller_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_other_smooth_scroll);
        btn_handler = findViewById(R.id.btn_handler);
        btn_handler.setOnClickListener(this);
        btn_anim_o = findViewById(R.id.btn_anim_o);
        btn_anim_o.setOnClickListener(this);
        btn_anim_v = findViewById(R.id.btn_anim_v);
        btn_anim_v.setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        handler = new MainThreadHandler(this);

        animO = ObjectAnimator.ofFloat(btn_anim_o, "translationX", 0, 200);
        animO.setDuration(2000);

        animV = ValueAnimator.ofInt(300);
        animV.setDuration(1000);
        animV.addUpdateListener((ValueAnimator animation) -> {
            int value = (int) animation.getAnimatedValue();
            btn_anim_v.scrollTo(value, 0);
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
            btn_handler.scrollTo(0, 0);
            btn_anim_o.setTranslationX(0);
            btn_anim_v.scrollTo(0, 0);
        }
    }
}
