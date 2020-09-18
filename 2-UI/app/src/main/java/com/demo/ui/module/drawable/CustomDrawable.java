package com.demo.ui.module.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.ui.util.DisplayUtils;

/**
 * @author 尉迟涛
 * create time : 2019/12/4 20:19
 * description :
 */
public class CustomDrawable extends Drawable {

    private Paint paint;
    private TextPaint textPaint;
    private final float TEXT_LENGTH;

    CustomDrawable(int color) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xffffffff);
        textPaint.setTextSize(DisplayUtils.sp2px(15));

        TEXT_LENGTH = textPaint.measureText("CustomDrawable");
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect = getBounds();
        float x = rect.exactCenterX();
        float y = rect.exactCenterY();
        canvas.drawCircle(x, y, Math.min(x, y), paint);

        float width = rect.right - rect.left;
        float height = rect.bottom - rect.top;
        float oneEighthW = width / 8f;
        float oneEighthH = height / 8f;
        canvas.drawRect(
                oneEighthW,
                oneEighthH * 2,
                oneEighthW * 7,
                oneEighthH * 6,
                paint
        );

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;

        canvas.drawText(
                "CustomDrawable",
                (width - TEXT_LENGTH) / 2,
                height - (height - fontHeight) / 2 - fontMetrics.bottom,
                textPaint
        );
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        //半透明
        return PixelFormat.TRANSLUCENT;
    }
}
