package com.demo.changeskin.replaceview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.demo.changeskin.R;

/**
 * @author 尉迟涛
 * create time : 2020/4/6 22:22
 * description :
 */
public class ActReplaceView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果使用AppCompatActivity，setFactory2需要在super.onCreate之前
        // 因为AppCompatActivity利用了Factory2来替换AppCompact相关的View，具体请阅读源码
        super.onCreate(savedInstanceState);

        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (TextUtils.equals(name, "TextView")) {
                    Button button = new Button(ActReplaceView.this);
                    button.setAllCaps(false);
                    button.setText("TextView被我换走了");
                    return button;
                }
                // 不做处理的话返回null就好了，会交给mPrivateFactory或者
                // 通过LayoutInflater中的onCreateView、createView方法利用反射创建View
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });

        setContentView(R.layout.act_replace);
    }
}
