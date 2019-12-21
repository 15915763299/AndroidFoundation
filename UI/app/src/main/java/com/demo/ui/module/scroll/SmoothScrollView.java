package com.demo.ui.module.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.demo.ui.R;
import com.demo.ui.listener.OnLogListener;
import com.demo.ui.util.DisplayUtils;

/**
 * @author 尉迟涛
 * create time : 2019/11/19 17:21
 * description :
 */
public class SmoothScrollView extends View {

    private static final String TAG = SmoothScrollView.class.getSimpleName();

    /**
     * 显示数字的数量
     */
    private int numberCount;
    /**
     * 数字间距
     */
    private float numberSpacing;
    /**
     * 数字大小
     */
    private float numberWidth, numberHeight;
    /**
     * 画笔
     */
    private TextPaint textPaint;

    /**
     * 画布宽度与视图宽度
     */
    private float mCanvasWidth, mViewWidth;

    /**
     * 滑动速度追踪
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 滑动的最大速度
     */
    private int mMaximumVelocity;
    /**
     * 滑动的最小速度
     */
    private int mMinimumVelocity;
    /**
     * 滑动线程
     */
    private FlingRunnable mFling;

    /**
     * 上一次点击的横坐标
     */
    private float mLastTouchX;

    /**
     * 最小滑动距离
     */
    private float minScroll;

    public SmoothScrollView(Context context) {
        super(context);
        init(null);
    }

    public SmoothScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SmoothScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int numberColor;
        float numberSize;
        setClickable(true);

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SmoothScrollView);
            numberColor = array.getColor(R.styleable.SmoothScrollView_numberColor, 0xffdddddd);
            numberSize = array.getDimension(R.styleable.SmoothScrollView_numberSize, DisplayUtils.sp2px(30));
            numberCount = array.getInt(R.styleable.SmoothScrollView_numberCount, 5);
            numberSpacing = array.getDimension(R.styleable.SmoothScrollView_numberSpacing, DisplayUtils.dip2px(5));
            array.recycle();
        } else {
            numberColor = 0xffdddddd;
            numberSize = DisplayUtils.sp2px(30);
            numberSpacing = DisplayUtils.dip2px(5);
            numberCount = 5;
        }


        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(numberSize);
        textPaint.setColor(numberColor);

        numberWidth = textPaint.measureText("0");
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        numberHeight = fm.descent - fm.ascent;
        minScroll = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mMaximumVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mFling = new FlingRunnable(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        logE("onSizeChanged");
        mViewWidth = w;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        logE("onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        logE("onMeasure");

        int modelW = MeasureSpec.getMode(widthMeasureSpec);
        int modelH = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (modelW) {
            //自定义View，wrap_content的情况需要自己处理
            case MeasureSpec.AT_MOST:// ==>wrap_content
                width = (int) (numberWidth + getPaddingLeft() + getPaddingRight());
                break;
            case MeasureSpec.EXACTLY://当你的控件设置了一个精确的值或者为match_parent时, 为这种模式
            case MeasureSpec.UNSPECIFIED://尽可能的多
            default:
        }
        switch (modelH) {
            //自定义View，wrap_content的情况需要自己处理
            case MeasureSpec.AT_MOST:
                height = (int) (numberHeight + getPaddingTop() + getPaddingBottom());
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
            default:
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        logE("onDraw");

        int h = getHeight();
        int w = getWidth();

        //计算起始位置
        int paddingLeftRight = getPaddingRight() + getPaddingLeft();
        float textWidth = numberSpacing * (numberCount - 1) + numberWidth * calNumberCount(numberCount);
        float x, y = (h - numberHeight) / 2f - textPaint.getFontMetrics().ascent;
        if (textWidth + paddingLeftRight * 2 > w) {
            mCanvasWidth = textWidth + paddingLeftRight * 2;
            x = paddingLeftRight;
        } else {
            mCanvasWidth = w;
            x = (w - textWidth) / 2f;
        }

        //绘制内容
        for (int i = 1; i <= numberCount; i++) {
            String numStr = String.valueOf(i);
            // 减少资源浪费，增加numberSize参数，因为计算短了会造成空白页面
            if (isInVisibleArea(x, numStr.length())) {
                canvas.drawText(numStr, x, y, textPaint);
            }
            x += (numberSpacing + numberWidth * numStr.length());
        }
    }

    /**
     * 计算从1到number的单个数字个数
     */
    private int calNumberCount(int number) {
        int result = 0;
        int digit = String.valueOf(number).length();
        for (int i = 0; i < digit; i++) {
            if (i == digit - 1) {
                int count = number - (int) (Math.pow(10, i)) + 1;
                result += digit * count;
            } else {
                double pow = Math.pow(10, i);
                //一位数：1*9*1，两位数：10*9*2，三位数：100*9*3，...
                result += (int) pow * 9 * (i + 1);
            }
        }
        return result;
    }

    /**
     * 计算当前横坐标是否在可视的范围内
     *
     * @param x 当前横坐标
     * @return true：在可视的范围内；false：不在可视的范围内
     */
    private boolean isInVisibleArea(float x, int numberSize) {
        //scrollerX以原点为基准，计算出来的横坐标为以View左上角为基准的横坐标
        float dx = x - getScrollX();
        return -(numberWidth * numberSize) <= dx && dx <= mViewWidth + numberSpacing;
    }

    /**
     * 控制屏幕不越界
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 当数据的长度不足以滑动时，不做滑动处理
        if (mCanvasWidth <= mViewWidth) {
            return true;
        }

        if (mVelocityTracker == null) {
            //VelocityTracker的构造函数是私有的，使用完后需要调用recycle销毁
            mVelocityTracker = VelocityTracker.obtain();
        }
        //为 VelocityTracker 传入触摸事件
        mVelocityTracker.addMovement(event);

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mLastTouchX = event.getX();
            mFling.stop();
        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            // 滑动的距离，移动后x减去初始x
            float scrollLengthX = event.getX() - mLastTouchX;
//            if (Math.abs(scrollLengthX) > minScroll) {
            // 初始x减去移动后x：
            // getScrollX() 小于0，说明画布右移了
            // getScrollX() 大于0，说明画布左移了
            float endX = getScrollX() - scrollLengthX;

            if (scrollLengthX > 0) {
                // 画布往右移动 -->
                // 注意：这里的等号不能去除，否则会有闪动
                if (endX <= 0) {
                    scrollTo(0, 0);
                } else {
                    //X:负数右移，正数左移
                    scrollBy((int) -scrollLengthX, 0);
                }
            } else if (scrollLengthX < 0) {
                // 画布往左移动  <--
                // 需要考虑是否右越界
                if (endX >= mCanvasWidth - mViewWidth) {
                    scrollTo((int) (mCanvasWidth - mViewWidth), 0);
                } else {
                    scrollBy((int) -scrollLengthX, 0);
                }
            }

            if (scrollListener != null) {
                scrollListener.onMove(getScrollX(), scrollLengthX, endX, mCanvasWidth - mViewWidth);
            }
//            }
            mLastTouchX = event.getX();
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            // 根据已经传入的触摸事件计算出当前的速度
            // 参数一值为1表示每毫秒像素数，1000表示每秒像素数；参数二为最大的速度
            // 可以通过getXVelocity 或 getYVelocity进行获取对应方向上的速度
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

            // 获取横向速度
            int velocityX = (int) mVelocityTracker.getXVelocity();

            // 速度要大于最小的速度值，才开始滑动
            if (Math.abs(velocityX) > mMinimumVelocity) {
                int initX = getScrollX();
                int maxX = (int) (mCanvasWidth - mViewWidth);
                if (maxX > 0) {
                    mFling.start(initX, velocityX, initX, maxX);
                }
            }

            //UP 的时候计算速度，释放
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }
        return super.onTouchEvent(event);
    }

    private OnLogListener onLogListener;

    public void setOnLogListener(OnLogListener onLogListener) {
        this.onLogListener = onLogListener;
    }

    private OnScrollListener scrollListener;

    public void setScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    private void logE(String info) {
        Log.e(TAG, info);
        if (onLogListener != null) {
            onLogListener.log(info);
        }
    }

    public void setNumberCount(int numberCount) {
        // 停止滚动
        if (mFling != null) {
            mFling.stop();
        }
        this.numberCount = numberCount;
        invalidate();
        scrollTo(0, 0);
    }

    /**
     * 滚动线程
     */
    private class FlingRunnable implements Runnable {

        private Scroller mScroller;

        private int mInitX;
        private int mMinX;
        private int mMaxX;
        private int mVelocityX;

        FlingRunnable(Context context) {
            this.mScroller = new Scroller(context, null, false);
        }

        void start(int initX,
                   int velocityX,
                   int minX,
                   int maxX) {
            this.mInitX = initX;//当前滑动距离
            this.mVelocityX = velocityX;//滑动速度
            this.mMinX = minX;//当前滑动距离
            this.mMaxX = maxX;//视图与画布长度差值

            // 先停止上一次的滚动
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }

            // 开始 fling
            mScroller.fling(
                    initX, 0,          //起始位置
                    velocityX, 0,   //速度
                    0, maxX,           //负、正方向滚动最大距离
                    0, 0         //负、正方向滚动最大距离
            );
            post(this);
        }

        @Override
        public void run() {
            // 如果已经结束，就不再进行
            if (!mScroller.computeScrollOffset()) {
                return;
            }

            // 计算偏移量
            int currX = mScroller.getCurrX();
            int diffX = mInitX - currX;

            Log.i(TAG, "run: [currX: " + currX + "]\n"
                    + "[diffX: " + diffX + "]\n"
                    + "[initX: " + mInitX + "]\n"
                    + "[minX: " + mMinX + "]\n"
                    + "[maxX: " + mMaxX + "]\n"
                    + "[velocityX: " + mVelocityX + "]\n"
            );

            // 用于记录是否超出边界，如果已经超出边界，则不再进行回调，即使滚动还没有完成
            boolean isEnd = false;

            if (diffX != 0) {

                // 超出右边界，进行修正
                if (getScrollX() + diffX >= mCanvasWidth - mViewWidth) {
                    diffX = (int) (mCanvasWidth - mViewWidth - getScrollX());
                    isEnd = true;
                }

                // 超出左边界，进行修正
                if (getScrollX() <= 0) {
                    diffX = -getScrollX();
                    isEnd = true;
                }

                if (!mScroller.isFinished()) {
                    scrollBy(diffX, 0);
                }
                mInitX = currX;
            }

            if (!isEnd) {
                post(this);
            }
        }

        /**
         * 进行停止
         */
        void stop() {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }
    }
}
