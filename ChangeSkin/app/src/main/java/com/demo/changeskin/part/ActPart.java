package com.demo.changeskin.part;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.changeskin.R;

/**
 * @author 尉迟涛
 * create time : 2020/4/8 22:25
 * description :
 */
public class ActPart extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_part);
    }
}
