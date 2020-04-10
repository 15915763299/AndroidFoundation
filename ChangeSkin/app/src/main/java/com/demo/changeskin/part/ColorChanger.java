package com.demo.changeskin.part;

import android.animation.ArgbEvaluator;

/**
 * @author apptest4
 * @date 2020/4/9
 * 颜色过度
 */
public class ColorChanger {

    private ArgbEvaluator argbEvaluator;
    private int fromColor, toColor;

    public ColorChanger(int fromColor, int toColor) {
        argbEvaluator = new ArgbEvaluator();
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public int changeColor(float rate) {
        return (int) (argbEvaluator.evaluate(rate, fromColor, toColor));
    }

}
