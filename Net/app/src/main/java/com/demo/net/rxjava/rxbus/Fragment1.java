package com.demo.net.rxjava.rxbus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.net.R;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 19:42
 * description :
 */
public class Fragment1 extends Fragment {

    private RxBus rxBus;
    private EditText edt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBus = RxBus.get();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_1, container, false);
        edt = view.findViewById(R.id.edt);
//        view.findViewById(R.id.btn).setOnClickListener(v -> {
//            FragmentEvent event = new FragmentEvent(edt.getText().toString());
//            rxBus.post(event);
//        });

        Button btn = view.findViewById(R.id.btn);
        // 点击防抖（防止瞬间多次点击）
        RxView.clicks(btn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                            FragmentEvent event = new FragmentEvent(edt.getText().toString());
                            rxBus.post(event);
                        });
        return view;
    }

}
