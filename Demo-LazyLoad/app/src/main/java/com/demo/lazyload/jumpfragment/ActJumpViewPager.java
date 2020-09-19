package com.demo.lazyload.jumpfragment;

import android.os.Bundle;
import android.view.MenuItem;

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
 * create time : 2020/4/24 21:51
 * description :
 */
public class ActJumpViewPager extends AppCompatActivity implements
        ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_jump_viewpager);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fragments.add(ColorFragment.newFragment("--" + i + "--", ColorFragment.COLORS[i]));
        }

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
                viewPager.setCurrentItem(0, false);
                return true;
            case R.id.fragment_2:
                viewPager.setCurrentItem(1, false);
                return true;
            case R.id.fragment_3:
                viewPager.setCurrentItem(2, false);
                return true;
            case R.id.fragment_4:
                viewPager.setCurrentItem(3, false);
                return true;
            case R.id.fragment_5:
                viewPager.setCurrentItem(4, false);
                return true;
            default:
        }
        return false;
    }
}
