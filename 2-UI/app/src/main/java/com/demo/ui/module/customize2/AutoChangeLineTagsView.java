package com.demo.ui.module.customize2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.ui.util.DisplayUtils;

/**
 * @author 尉迟涛
 * create time : 2020/2/25 10:59
 * description : 自动换行的 tags view
 */
public class AutoChangeLineTagsView extends ViewGroup {

    private static final int TAG_MARGIN = DisplayUtils.dip2px(10);
    private static final int WRAP_CONTENT_MIN_WIDTH = DisplayUtils.dip2px(100);
    private static final int TAG_LINE_SPACE = TAG_MARGIN / 2;

    private int viewUseWidth;

    public AutoChangeLineTagsView(Context context) {
        super(context);
    }

    public AutoChangeLineTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoChangeLineTagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        if (child instanceof TextView) {
            super.addView(child);

            // 设置为 wrap_content 以便测量
            // TODO 不设置 wrap_content 也可以，但是要设置最大行高，match_parent的时候不至于占满整个屏幕
            LayoutParams lp = child.getLayoutParams();
            lp.width = LayoutParams.WRAP_CONTENT;
            lp.height = LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(lp);

            invalidate();
        } else {
            throw new IllegalArgumentException("只能添加TextView");
        }
    }

    /**
     * 宽度主要根据父 View 的宽度决定
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 先定义宽度，tag的摆放先由宽度决定
        LayoutParams lp = getLayoutParams();
        int viewWidth;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                viewWidth = getMinimumWidth() > WRAP_CONTENT_MIN_WIDTH ?
                        getMinimumWidth() : WRAP_CONTENT_MIN_WIDTH;
                viewWidth += (getPaddingLeft() + getPaddingRight());
            } else {// MATCH_PARENT & 无限制
                viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            }
        }

        int height = measureTags(viewWidth, widthMeasureSpec, heightMeasureSpec);

        int viewHeight;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            if (lp.height == LayoutParams.MATCH_PARENT) {
                viewHeight = MeasureSpec.getSize(heightMeasureSpec);
            } else {// WRAP_CONTENT & 无限制
                viewHeight = height;
            }
        }

        setMeasuredDimension(viewWidth, viewHeight);
    }

    /**
     * 测量tags
     *
     * @param viewWidth 当前view的宽度
     * @return 测量高度
     */
    private int measureTags(int viewWidth, int widthMeasureSpec, int heightMeasureSpec) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        viewUseWidth = viewWidth - paddingLeft - paddingRight;

        // 绘制起始点 xy、行高
        int x = 0, y = paddingTop + paddingBottom, lineMaxHeight = 0;
        // 行数、当前行的 tag 数量
        int rows = 0, rowTagCount = 0;

        View child = null;
        for (int i = 0; i < getChildCount(); i++) {
            // 下面有个 i-- 的回退，这里不重新计算
            if (child == null || child != getChildAt(i)) {
                child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            // 加上当前 View 所需要的横向长度
            int currentRight = x + child.getMeasuredWidth() + paddingRight;
            if (currentRight > viewUseWidth) {
                if (rowTagCount == 0) {
                    // rowViewCount == 0 的时候当前 tag 直接超出 view 的宽度，当前 tag 就算这行的
                    lineMaxHeight = child.getMeasuredHeight();
                } else {
                    // 否则进入下一行，重新计算当前 tag 的宽度
                    i--;
                }

                // 换行，x回到起点，y增加一行，最大行高、行tag数量归零，行数+1
                rows++;
                x = paddingLeft;
                y += (lineMaxHeight + TAG_LINE_SPACE);
                lineMaxHeight = 0;
                rowTagCount = 0;
            } else {
                x += (child.getMeasuredWidth() + TAG_MARGIN);
                rowTagCount++;

                // 取最大的高度
                if (lineMaxHeight < child.getMeasuredHeight()) {
                    lineMaxHeight = child.getMeasuredHeight();
                }
            }
        }
        //最后一行高度
        y += lineMaxHeight;
        //特殊情况处理
        if (rows == 1 && rowTagCount == 0) {
            y -= TAG_LINE_SPACE;
        }
        return y;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        // 绘制起始点 xy 以及行高
        int x = paddingLeft, y = paddingTop, lineMaxHeight = 0;
        // 当前行的 tag 数量
        int rowTagCount = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            // 加上当前 View 所需要的横向长度
            int currentRight = x + child.getMeasuredWidth() + paddingRight;
            if (currentRight > viewUseWidth) {

                if (rowTagCount == 0) {
                    // 绘制当行
                    child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
                    lineMaxHeight = child.getMeasuredHeight();
                } else {
                    i--;
                }

                // 切换到下一行
                x = paddingLeft;
                y += (lineMaxHeight + TAG_LINE_SPACE);
                lineMaxHeight = 0;
                rowTagCount = 0;
            } else {
                child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
                x += (child.getMeasuredWidth() + TAG_MARGIN);
                rowTagCount++;

                // 取最大的高度
                if (lineMaxHeight < child.getMeasuredHeight()) {
                    lineMaxHeight = child.getMeasuredHeight();
                }
            }
        }
    }

}
