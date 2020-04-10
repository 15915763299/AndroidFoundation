package com.demo.changeskin.part;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demo.changeskin.R;

/**
 * @author 尉迟涛
 * create time : 2020/4/8 22:25
 * description : 这里只做示范，具体参看GraySkinConverter内方法
 * 可以结合自定义ViewGroup搞出一个黑白风格的方案
 */
public class ActPart extends AppCompatActivity {

    private static final int COLOR_OFFSET = 30;
    private boolean isEnable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_part);

        ImageView img2 = findViewById(R.id.img2);
        TextView tx1 = findViewById(R.id.tx1);
        TextView tx2 = findViewById(R.id.tx2);
        TextView tx3 = findViewById(R.id.tx3);

        tx2.setOnClickListener(v -> {
            GraySkinConverter graySkinConverter = GraySkinConverter.get();
            graySkinConverter.setEnable(isEnable = !isEnable);

            graySkinConverter.decorateText(img2);
            graySkinConverter.decorateText(tx1);
            graySkinConverter.decorateBackground(tx2);

            tx3.setBackgroundColor(graySkinConverter.convertColor(
                    ContextCompat.getColor(ActPart.this, android.R.color.holo_orange_light),
                    COLOR_OFFSET
            ));
        });
    }
}
