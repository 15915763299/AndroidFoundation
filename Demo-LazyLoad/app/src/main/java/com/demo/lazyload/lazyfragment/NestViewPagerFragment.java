package com.demo.lazyload.lazyfragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.lazyload.MainActivity;
import com.demo.lazyload.R;
import com.demo.lazyload.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/2/24 11:45
 * description :
 */
public class NestViewPagerFragment extends LazyFragment {

    private static final String BUNDLE_INDEX = "index";
    private String index;
    private TextView tx;
    private LinearLayout bg;
    private CountDownTimer countDownTimer;
    private DataHandler handler;
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private WeakReference<ActLazy> actLazy;

    public static NestViewPagerFragment newInstance(String index, int behavior) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_INDEX, index);
        NestViewPagerFragment nestViewPagerFragment = new NestViewPagerFragment();
        nestViewPagerFragment.setBehavior(behavior);
        nestViewPagerFragment.setArguments(bundle);
        return nestViewPagerFragment;
    }

    private void init() {
        if (getArguments() != null) {
            index = getArguments().getString(BUNDLE_INDEX);
        }
        if (handler == null) {
            handler = new DataHandler(this);
        }
    }

    private void log(String info) {
        Log.d("NestViewPagerFragment " + index, info);
    }

    private void logE(String info) {
        Log.e("NestViewPagerFragment " + index, info);
    }

    private void logInFrag(String info) {
        if (actLazy == null || actLazy.get() == null) {
            actLazy = new WeakReference<>((ActLazy) getActivity());
        }
        actLazy.get().logInFrag("Fragment " + index + ": " + info);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        init();
        log("onAttach");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        init();
        log("setUserVisibleHint: " + isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_nest_view_pager;
    }

    @Override
    protected void initView(View view) {
        log("initView");
        viewPager = view.findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_4_1, MainActivity.behavior));
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_4_2, MainActivity.behavior));
        fragments.add(WorkFragment.newInstance(WorkFragment.WORKER_4_3, MainActivity.behavior));

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                fragments
        );
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    default:
                }
                navigationView.setSelectedItemId(itemId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView = view.findViewById(R.id.navigation);
        navigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item)-> {
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
                default:
            }
            return false;
        });

        bg = view.findViewById(R.id.bg);
        tx = view.findViewById(R.id.tx);
    }

    private void getData() {
        countDownTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Message.obtain(handler, 0, index).sendToTarget();
            }
        };
        countDownTimer.start();
    }

    private void releaseGet(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        log("第一次出现");
    }

    @Override
    protected void onFragmentResume() {
        logE("加载资源");
        logInFrag("加载资源");
        getData();
    }

    @Override
    protected void onFragmentPause() {
        logE("释放资源");
        logInFrag("释放资源");
        releaseGet();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log("onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        log("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
        releaseGet();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        log("onDetach");
    }

    private static class DataHandler extends Handler {
        private WeakReference<NestViewPagerFragment> wf;

        DataHandler(NestViewPagerFragment fragment) {
            this.wf = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wf != null && wf.get() != null) {
                TextView tx = wf.get().tx;
                tx.setText("加载完成");
                tx.setTextColor(Color.WHITE);

                int color;
                switch ((String)msg.obj) {
                    case WorkFragment.WORKER_4:
                        color = 0xffFFCA28;
                        break;
                    default:
                        color = 0xff8D6E63;
                }
                wf.get().bg.setBackgroundColor(color);
            }
        }
    }
}
