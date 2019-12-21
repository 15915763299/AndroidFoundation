package com.demo.ui.module.customize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.demo.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/11/26 22:00
 * description :
 */
public class ActCustomizeView extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ripple1, btn_ripple2;
    private int ripple1 = 0, ripple2 = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customize_view);

        btn_ripple1 = findViewById(R.id.btn_ripple1);
        btn_ripple2 = findViewById(R.id.btn_ripple2);
        btn_ripple1.setOnClickListener(this);
        btn_ripple2.setOnClickListener(this);

        List<ScaleImageView> viewList = new ArrayList<>();
        ScaleImageView scaleImageView = new ScaleImageView(this);
        scaleImageView.setImageResource(R.mipmap.test);
        viewList.add(scaleImageView);
        scaleImageView = new ScaleImageView(this);
        scaleImageView.setImageResource(R.mipmap.test2);
        viewList.add(scaleImageView);

        ViewPager view_pager = findViewById(R.id.view_pager);
        view_pager.setAdapter(new MyPagerAdapter(viewList));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ripple1:
                btn_ripple1.setText(String.valueOf(ripple1++));
                break;
            case R.id.btn_ripple2:
                btn_ripple2.setText(String.valueOf(ripple2++));
                break;
        }
    }

    private static class MyPagerAdapter extends PagerAdapter {

        private List<ScaleImageView> viewList;

        MyPagerAdapter(List<ScaleImageView> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }
    }
}
