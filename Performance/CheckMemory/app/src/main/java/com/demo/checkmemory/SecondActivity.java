package com.demo.checkmemory;

import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 尉迟涛
 * create time : 2020/4/25 21:22
 * description :
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.btn).setOnClickListener((View v) -> showSoftInput());

        Handler handler = new Handler();
        for (int i = 0; i < 5; i++) {
            handler.postDelayed(() -> {
            }, 3_000);
        }
    }

    private void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        }
    }

}
