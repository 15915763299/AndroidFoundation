package com.demo.lazyload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.lazyload.fitchild.FitFragment;
import com.demo.lazyload.lazyfragment.ActLazy;
import com.demo.lazyload.normal.ActNormal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static int behavior = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
    private TextView tx_behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FitFragment());
        fragments.add(new FitFragment());
        fragments.add(new FitFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragments
        );
        viewPager.setAdapter(adapter);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);

        tx_behavior = findViewById(R.id.tx_behavior);
        tx_behavior.setText("BEHAVIOR_SET_USER_VISIBLE_HINT");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, ActNormal.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, ActLazy.class));
                break;
            case R.id.btn3:
                if (behavior == FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
                    behavior = FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
                    tx_behavior.setText("BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT");
                } else {
                    behavior = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
                    tx_behavior.setText("BEHAVIOR_SET_USER_VISIBLE_HINT");
                }
            default:
        }
    }
}
