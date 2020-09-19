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
public class Fragment2 extends Fragment {

    // Disposable容器，onDestroy中取消订阅
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RxBus rxBus;
    private TextView tx;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBus = RxBus.get();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_2, container, false);
        tx = (TextView) view;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.add(
                    rxBus.toFlowable(FragmentEvent.class)
                            .subscribe(crossActivityEvent -> {
                                tx.setText(crossActivityEvent.imfo);
                                Log.i("Fragment2", crossActivityEvent.imfo);
                            })
            );
        }
    }

    @Override
    public void onDestroy() {
        // clear时网络请求会随即cancel
        compositeDisposable.clear();
        compositeDisposable = null;
        super.onDestroy();
    }
}
