package com.demo.changeskin.y;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;

/**
 * @author 尉迟涛
 * create time : 2020/4/7 0:10
 * description : 换肤策略
 */
public interface SkinStrategy {

    /**
     * 判断是否在规定时间内，是则进行换肤处理
     * @return 是否在规定时间内
     */
    boolean isRightTime();

    /**
     * 颜色转换
     */
    int convertColor(@ColorInt int colorInt);

    /**
     * 获取处理后的背景
     */
    Drawable getDrawable(int resId);


    void decorate(ImageView imageView);

    void decorate(TextView textView);

    void decorateBackground(View source);

    void decorateDrawable(Drawable drawable);
}
