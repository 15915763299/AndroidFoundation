package com.demo.net.rxjava.rxbus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.net.R;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 19:42
 * description :
 */
public class Fragment3 extends Fragment {

    // Disposable容器，onDestroy中取消订阅
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RxBus rxBus;
    private TextView tx;
    private int times = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBus = RxBus.get();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_3, container, false);
        tx = (TextView) view;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable.add(
                rxBus.toFlowable(FragmentEvent.class)
                        .subscribe(crossActivityEvent -> {
                            String info = String.valueOf(++times);
                            tx.setText("（防抖检测）信息接收次数：" + info);
                            Log.i("Fragment2", info);
                        })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
