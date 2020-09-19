package com.demo.changeskin.part;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

/**
 * @author apptest4
 * @date 2020/4/7
 * 皮肤转换器功能定义
 */
public interface SkinConverter {

    int convertColor(@ColorInt int colorInt, int offset);

    void decorateText(ImageView imageView);

    void decorateText(TextView textView);

    Drawable decorateDrawable(Drawable source);

    Bitmap decorateBitmap(Bitmap source);

    void hardwareAccelerate(View view);

}
