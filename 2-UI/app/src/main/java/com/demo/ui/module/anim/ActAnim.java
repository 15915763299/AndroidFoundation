package com.demo.ui.module.anim;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;
import com.demo.ui.util.DisplayUtils;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2019/12/5 21:04
 * description : 列表动画 & Activity跳转动画
 */
public class ActAnim extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_anim);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        initListView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            Intent intent = new Intent(this, ActAnimJump.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int halfW = DisplayUtils.getScreenMetrics().widthPixels / 2;
            performAnimate(btn1, btn1.getWidth(), halfW);
        }
    }

    /**
     * 宽度变化动画
     */
    private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        // 也可以自行设置估值器
        // private IntEvaluator mEvaluator = new IntEvaluator();
        valueAnimator.addUpdateListener((ValueAnimator animator) -> {
            target.getLayoutParams().width = (int) animator.getAnimatedValue();
            target.requestLayout();
        });
        valueAnimator.setDuration(4000).start();
    }

    /**
     * 初始化list，带Item动画
     */
    private void initListView() {
        ListView listView = findViewById(R.id.list_view);
        ArrayList<String> data = new ArrayList<>();
        data.add("Activity");
        data.add("Service");
        data.add("BroadcastReceiver");
        data.add("ContentProvider");
        SimpleAdapter adapter = new SimpleAdapter(this, data);
        listView.setAdapter(adapter);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_list_item);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);
    }
}
