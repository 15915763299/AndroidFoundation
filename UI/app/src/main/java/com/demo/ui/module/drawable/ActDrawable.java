package com.demo.ui.module.drawable;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.demo.ui.R;


/**
 * @author 尉迟涛
 * create time : 2019/12/3 21:06
 * description :
 */
public class ActDrawable extends AppCompatActivity implements View.OnClickListener {

    private ImageView img1;
    private TextView tx1, tx2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_drawable);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        img1 = findViewById(R.id.img1);
        tx1 = findViewById(R.id.tx1);
        tx2 = findViewById(R.id.tx2);

        ImageView img2 = findViewById(R.id.img2);
        img2.setImageDrawable(new CustomDrawable(0xff4DB6AC));
        img2.setImageLevel(1);

        TextView tx3 = findViewById(R.id.tx3);
        ScaleDrawable sd = (ScaleDrawable) tx3.getBackground();
        sd.setLevel(1);

        TextView tx4 = findViewById(R.id.tx4);
        ClipDrawable cd = (ClipDrawable) tx4.getBackground();
        cd.setLevel(5000);//0~10000

        SeekBar seek_bar = findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setImageLevel(progress);
                tx1.setText("LevelListDrawable, Level: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
                TransitionDrawable td = (TransitionDrawable) tx2.getBackground();
                td.startTransition(1000);
            }
            break;
            case R.id.btn2: {
                TransitionDrawable td = (TransitionDrawable) tx2.getBackground();
                td.resetTransition();
            }
            break;
            default:
        }
    }
}
