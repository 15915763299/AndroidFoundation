package com.demo.ui.module.customize2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ui.R;

/**
 * @author 尉迟涛
 * create time : 2020/3/15 16:45
 * description :
 */
public class ActCustomizeView2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tags);

        AutoChangeLineTagsView view = findViewById(R.id.tagsView);
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 5; i++) {
            inflateTextView(inflater, view, "--" + i + "--");
        }

        AutoChangeLineTagsView view2 = findViewById(R.id.tagsView2);
        inflateTextView(inflater, view2, "安卓（Android）是一种基于Linux的自由及开放源代码的操作系统。");
        inflateTextView(inflater, view2, "主要使用于移动设备");
        inflateTextView(inflater, view2, "如智能手机");
        inflateTextView(inflater, view2, "和平板电脑");
        inflateTextView(inflater, view2, "Get started by creating a new file or uploading an existing file. We recommend every repository include a README, LICENSE, and .gitignore.");
        inflateTextView(inflater, view2, "Android 10 现已到来");
        inflateTextView(inflater, view2, "Android 10 现已到来");
        inflateTextView(inflater, view2, "Android 10 现已到来");
        inflateTextView(inflater, view2, "Android 11 都tm来了");
    }

    private void inflateTextView(LayoutInflater inflater, ViewGroup parent, String text) {
        TextView textView = (TextView) inflater.inflate(R.layout.tag, parent, false);
        textView.setText(text);
        parent.addView(textView);
    }
}
