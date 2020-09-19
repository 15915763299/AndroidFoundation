package com.demo.lazyload.jumpfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.lazyload.R;

/**
 * @author 尉迟涛
 * create time : 2020/4/24 22:10
 * description :
 */
public class ColorFragment extends Fragment {

    public static final int[] COLORS = {
            0xff26C6DA,
            0xff66BB6A,
            0xffD4E157,
            0xffFFCA28,
            0xffFF7043,
            0xffFFB300,
            0xffFFA000,
            0xffFF8F00
    };
    public static final int DEFAULT_COLOR =  0xff8D6E63;

    private String name = "未命名";
    private int color = DEFAULT_COLOR;

    public static ColorFragment newFragment(String name, @ColorInt int color) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("color", color);

        ColorFragment colorFragment = new ColorFragment();
        colorFragment.setArguments(bundle);
        return colorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            color = bundle.getInt("color");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_normal, container, false);
        view.setBackgroundColor(color);
        TextView tx = view.findViewById(R.id.tx);
        tx.setText(name);
        return view;
    }
}
