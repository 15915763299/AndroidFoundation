package com.demo.lazyload.normal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.lazyload.R;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2020/2/23 18:33
 * description :
 */
public class NormalFragment extends Fragment {

    private static final String TAG = NormalFragment.class.getSimpleName();
    private static final String BUNDLE_INDEX = "index";
    private int index;
    private TextView tx;
    private LinearLayout bg;
    private CountDownTimer countDownTimer;
    private DataHandler handler;
    private WeakReference<ActNormal> actNormal;

    public static NormalFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_INDEX, index);
        NormalFragment normalFragment = new NormalFragment();
        normalFragment.setArguments(bundle);
        return normalFragment;
    }

    private void init() {
        if (getArguments() != null) {
            index = getArguments().getInt(BUNDLE_INDEX);
        }
        if (handler == null) {
            handler = new DataHandler(this);
        }
    }

    private void log(String method) {
        Log.d(TAG + " " + index, method);
    }

    private void logE(String method) {
        Log.e(TAG, TAG + " " + index + ": " + method);
    }

    private void logInFrag(String info) {
        if (actNormal == null || actNormal.get() == null) {
            actNormal = new WeakReference<>((ActNormal) getActivity());
        }
        actNormal.get().logInFrag("Fragment " + index + ": " + info);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        init();
        log("setUserVisibleHint");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        init();
        log("onAttach");
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
        logE("onResume");
        logInFrag("加载资源");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("onCreateView");
        View contentView = inflater.inflate(R.layout.frag_normal, container, false);
        bg = contentView.findViewById(R.id.bg);
        tx = contentView.findViewById(R.id.tx);
        getData();
        return contentView;
    }

    /**
     * 模拟加载资源
     */
    private void getData() {
        countDownTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                handler.sendEmptyMessage(index);
            }
        };
        countDownTimer.start();
    }

    /**
     * 模拟释放资源
     */
    private void releaseGet() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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
        logInFrag("释放资源");
        releaseGet();
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        log("onDetach");
    }

    private static class DataHandler extends Handler {

        private WeakReference<NormalFragment> wf;

        DataHandler(NormalFragment fragment) {
            this.wf = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wf != null && wf.get() != null) {
                TextView tx = wf.get().tx;
                tx.setText("加载完成");
                tx.setTextColor(Color.WHITE);

                int color;
                switch (msg.what) {
                    case 1:
                        color = 0xff26C6DA;
                        break;
                    case 2:
                        color = 0xff66BB6A;
                        break;
                    case 3:
                        color = 0xffD4E157;
                        break;
                    case 4:
                        color = 0xffFFCA28;
                        break;
                    case 5:
                        color = 0xffFF7043;
                        break;
                    default:
                        color = 0xff8D6E63;
                }
                wf.get().bg.setBackgroundColor(color);
            }
        }
    }
}
