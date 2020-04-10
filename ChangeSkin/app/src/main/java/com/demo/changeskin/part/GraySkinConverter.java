package com.demo.changeskin.part;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.demo.changeskin.App;

/**
 * @author apptest4
 * @date 2020/4/7
 */
public class GraySkinConverter extends BaseSkinConverter {

    private static class Instance {
        private static final GraySkinConverter INSTANCE = new GraySkinConverter();
    }

    public static GraySkinConverter get() {
        return Instance.INSTANCE;
    }

    private GraySkinConverter() {
        super();
    }

    @Override
    protected String getSpName() {
        return "gray";
    }

    public void decorateText(TextView textView, @ColorInt int textColor) {
        if (!isEnable || textView == null) {
            return;
        }
        decorateText(textView);
        textView.setTextColor(textColor);
    }

    public void decorateText(ViewGroup container, int viewId) {
        if (!isEnable || container == null) {
            return;
        }
        View target = container.findViewById(viewId);
        if (target instanceof ImageView) {
            decorateText((ImageView) target);
        } else if (target instanceof TextView) {
            decorateText((TextView) target);
        } else if (target != null) {
            hardwareAccelerate(target);
        }
    }

    public void decorateBackground(View view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        view.setBackground(decorateDrawable(drawable));
    }

    public Drawable getDrawable(int resId) {
        Drawable result = ContextCompat.getDrawable(App.getApp(), resId);
        if (!isEnable || result == null) {
            return result;
        }
        return decorateDrawable(result);
    }

    //****************************************************************************************
    // 以下为基础方法，可自行进行拓展
    //****************************************************************************************

    /**
     * 颜色转换
     */
    @Override
    public int convertColor(int colorInt, int offset) {
        if (!isEnable) {
            return colorInt;
        }
        return forceConvertColor(colorInt, offset);
    }

    /**
     * 颜色转换公式可以参考这个： https://segmentfault.com/a/1190000009000216
     * 如果要匹配ColorMatrixColorFilter处理图片获取的灰色，请使用offset调整，或者，使用更高深的算法...
     *
     * @param offset 控制颜色深浅，负数变深，正数变浅
     */
    public int forceConvertColor(int colorInt, int offset) {
        //此处为将像素值转换为灰度值的方法，存在误差，算法不唯一
        int a = (colorInt >> 24) & 0xff;
        int r = (colorInt >> 16) & 0xff;
        int g = (colorInt >> 8) & 0xff;
        int b = colorInt & 0xff;
        //把每个像素点的RGB值拿出来，算一下他们的平均值(R+G+B)/3，然后再替换原来的RGB值
        int avg = (r + g + b) / 3;
        return (a << 24) | ((avg + offset) << 16) | ((avg + offset) << 8) | (avg + offset);

//        return (int) (Color.alpha(colorInt)
//                + 0.2125 * Color.red(colorInt)
//                + 0.7154 * Color.green(colorInt)
//                + 0.0721 * Color.blue(colorInt));
    }

    /**
     * ImageView
     */
    @Override
    public void decorateText(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (isEnable) {
            try {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
            } catch (Exception ignore) {
            }
        } else {
            imageView.setColorFilter(null);
        }
    }

    /**
     * TextView
     */
    @Override
    public void decorateText(TextView textView) {
        if (textView == null) {
            return;
        }

        try {
            TextPaint paint = textView.getPaint();
            if (paint != null) {
                if (isEnable) {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    paint.setColorFilter(new ColorMatrixColorFilter(matrix));
                } else {
                    paint.setColorFilter(null);
                }
            }
            decorateCompoundDrawable(textView);
        } catch (Exception ignore) {
        }
    }

    private void decorateCompoundDrawable(TextView textView) {
        Drawable[] compounds = textView.getCompoundDrawables();
        if (compounds.length > 3) {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    decorateDrawable(compounds[0]),
                    decorateDrawable(compounds[1]),
                    decorateDrawable(compounds[2]),
                    decorateDrawable(compounds[3])
            );
        }
    }

    /**
     * Drawable
     */
    @Override
    public Drawable decorateDrawable(Drawable source) {
        if (source == null) {
            return null;
        }

        if (isEnable) {
            try {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                source.setColorFilter(cf);
            } catch (Exception ignore) {
            }
        } else {
            source.setColorFilter(null);
        }
        return source;
    }

    /**
     * Bitmap
     */
    @Override
    public Bitmap decorateBitmap(Bitmap source) {
        if (!isEnable || source == null) {
            return source;
        }

        Bitmap result;
        try {
            int height = source.getHeight();
            int width = source.getWidth();
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            //Set the matrix to affect the saturation of colors.
            //A value of 0 maps the color to gray-scale.
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            canvas.drawBitmap(source, 0, 0, paint);
        } catch (Exception ignore) {
            result = source;
        }
        return result;
    }

    /**
     * 硬件加速
     */
    @Override
    public void hardwareAccelerate(View view) {
        if (!isEnable || view == null) {
            return;
        }
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

}
