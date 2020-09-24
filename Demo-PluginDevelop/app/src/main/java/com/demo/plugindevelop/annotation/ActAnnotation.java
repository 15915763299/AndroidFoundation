package com.demo.plugindevelop.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.plugindevelop.R;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 9:16
 * description : 使用注解实现 setContentView、setOnClickListener、setOnLongClickListener
 */
@ContentView(value = R.layout.dact_annotation)
public class ActAnnotation extends Activity {

    @ViewInject(R.id.btn1)
    private Button btn1;
    @ViewInject(R.id.btn2)
    private Button btn2;
    @ViewInject(R.id.tx)
    private TextView tx;

    private int i = 0, j = 0, k = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注解注入关键信息，最终还是要通过当前对象去找到注解，获取关键信息，然后执行统一的代码
        // 类似于接口，只是表现形式不同

        // 可以放到BaseActivity里
        InjectUtils.inject(this);
    }

    // 注意方法参数和返回值要与接口一致喔
    @OnClick({R.id.btn1, R.id.btn2})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                btn1.setText(String.valueOf(++i));
                break;
            case R.id.btn2:
                btn2.setText(String.valueOf(++j));
                break;
            default:
        }
    }

    @OnLongClick(R.id.btn2)
    public boolean myLongClick(View view) {
        if (view.getId() == R.id.btn2) {
            tx.setText(String.valueOf(++k));
        }
        return true;
    }
}
