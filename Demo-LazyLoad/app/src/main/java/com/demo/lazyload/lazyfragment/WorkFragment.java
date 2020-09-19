package com.demo.lazyload.lazyfragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.lazyload.R;
import com.demo.lazyload.jumpfragment.ColorFragment;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2020/2/24 11:45
 * description :
 */
public class WorkFragment extends LazyFragment {

    public static final String WORKER_1 = "1";
    public static final String WORKER_2 = "2";
    public static final String WORKER_3 = "3";
    public static final String WORKER_4 = "4";
    public static final String WORKER_5 = "5";

    public static final String WORKER_4_1 = "4-1";
    public static final String WORKER_4_2 = "4-2";
    public static final String WORKER_4_3 = "4-3";

    private static final String BUNDLE_INDEX = "index";
    private String index;
    private TextView tx;
    private LinearLayout bg;
    private CountDownTimer countDownTimer;
    private DataHandler handler;
    private WeakReference<ActLazy> actLazy;

    public static WorkFragment newInstance(String index, int behavior) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_INDEX, index);
        WorkFragment workFragment = new WorkFragment();
        workFragment.setBehavior(behavior);
        workFragment.setArguments(bundle);
        return workFragment;
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
        Log.d("WorkFragment " + index, info);
    }

    private void logE(String info) {
        Log.e("WorkFragment " + index, info);
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
        return R.layout.frag_normal;
    }

    @Override
    protected void initView(View view) {
        log("initView");
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

    private void releaseGet() {
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
        private WeakReference<WorkFragment> wf;

        DataHandler(WorkFragment fragment) {
            this.wf = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wf != null && wf.get() != null) {
                TextView tx = wf.get().tx;
                tx.setText("加载完成");
                tx.setTextColor(Color.WHITE);

                int color;
                switch ((String) msg.obj) {
                    case WORKER_1:
                        color = ColorFragment.COLORS[0];
                        break;
                    case WORKER_2:
                        color = ColorFragment.COLORS[1];
                        break;
                    case WORKER_3:
                        color = ColorFragment.COLORS[2];
                        break;
                    case WORKER_4:
                        color = ColorFragment.COLORS[3];
                        break;
                    case WORKER_5:
                        color = ColorFragment.COLORS[4];
                        break;
                    case WORKER_4_1:
                        color = ColorFragment.COLORS[5];
                        break;
                    case WORKER_4_2:
                        color = ColorFragment.COLORS[6];
                        break;
                    case WORKER_4_3:
                        color = ColorFragment.COLORS[7];
                        break;
                    default:
                        color = ColorFragment.DEFAULT_COLOR;
                }
                wf.get().bg.setBackgroundColor(color);
            }
        }
    }
}
