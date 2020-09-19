package com.demo.changeskin.custompaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author 尉迟涛
 * create time : 2020/4/8 22:11
 * description : 通过自定义paint，重写draw、dispatchDraw方法
 *
 * 这种方式会让视频也变灰的喔
 */
public class GrayLinearLayout extends LinearLayout {
    private Paint mPaint = new Paint();

    public GrayLinearLayout(Context context) {
        super(context);
        init();
    }

    public GrayLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrayLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

}
