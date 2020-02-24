package com.demo.lazyload.lazyfragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.demo.lazyload.MainActivity;
import com.demo.lazyload.R;
import com.demo.lazyload.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/2/24 0:37
 * description :
 */
public class ActLazy extends AppCompatActivity implements
        ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private StringBuilder sb;
    private TextView tx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_normal);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_1, MainActivity.behavior));
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_2, MainActivity.behavior));
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_3, MainActivity.behavior));
        fragments.add(NestViewPagerFragment.newInstance(WorkFragment.WORKER_4, MainActivity.behavior));
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_5, MainActivity.behavior));

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                MainActivity.behavior,
                fragments
        );
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageMargin(50);

        // 分发点击事件
        findViewById(R.id.parent).setOnTouchListener(
                (View v, MotionEvent event) -> viewPager.dispatchTouchEvent(event)
        );

        tx = findViewById(R.id.tx);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                sb = new StringBuilder();
                tx.setText("");
                break;
            default:
        }
    }

    public void logInFrag(String info) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(info).append("\n");
        tx.setText(sb.toString());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int itemId = R.id.fragment_1;
        switch (position) {
            case 0:
                itemId = R.id.fragment_1;
                break;
            case 1:
                itemId = R.id.fragment_2;
                break;
            case 2:
                itemId = R.id.fragment_3;
                break;
            case 3:
                itemId = R.id.fragment_4;
                break;
            case 4:
                itemId = R.id.fragment_5;
                break;
            default:
        }
        navigationView.setSelectedItemId(itemId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_1:
                viewPager.setCurrentItem(0, true);
                return true;
            case R.id.fragment_2:
                viewPager.setCurrentItem(1, true);
                return true;
            case R.id.fragment_3:
                viewPager.setCurrentItem(2, true);
                return true;
            case R.id.fragment_4:
                viewPager.setCurrentItem(3, true);
                return true;
            case R.id.fragment_5:
                viewPager.setCurrentItem(4, true);
                return true;
            default:
        }
        return false;
    }
}