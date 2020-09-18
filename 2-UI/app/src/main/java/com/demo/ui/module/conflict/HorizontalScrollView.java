package com.demo.ui.module.conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author 尉迟涛
 * create time : 2019/11/22 13:05
 * description : 解决滑动冲突，（view）内部拦截法 & （view）外部拦截法
 */
public class HorizontalScrollView extends ViewGroup {

    private static final String TAG = "HorizontalScrollViewEx";

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;
    /**
     * 记录上次滑动的坐标
     */
    private int mLastX = 0;
    /**
     * 记录上次滑动的坐标(onInterceptTouchEvent)
     */
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    /**
     * 外部拦截法
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "onInterceptTouchEvent: ACTION_DOWN");
                intercepted = false;
                //滑动结束之前，事件交由父View处理，防止滑动中断
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "onInterceptTouchEvent: ACTION_MOVE");
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                //拦截横向滑动
                intercepted = Math.abs(deltaX) > Math.abs(deltaY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }

        Log.d(TAG, "intercepted=" + intercepted);
        mLastX = x;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

//    /**
//     * 内部拦截法
//     * 1、需要在子View的onInterceptTouchEvent或onTouchEvent方法中，event为DOWN的时候，
//     *    调用父滑动View的requestDisallowInterceptTouchEvent
//     * 2、同时父View要重写 onInterceptTouchEvent 方法
//     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return event.getAction() == MotionEvent.ACTION_DOWN;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                int deltaX = x - mLastX;
                Log.d(TAG, "onTouchEvent: deltaX: " + deltaX);
                scrollBy(-deltaX, 0);
                break;
            }
            case MotionEvent.ACTION_UP: {
                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();

                //滑的速度到达阈值就认为需要进入下一页
                if (Math.abs(xVelocity) >= 100) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    //滑动的距离超过一半，就进入下一页
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                //保证在0页和最后一页滑动时不会越界
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));

                //最终计算滑动位置
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx);
                Log.d(TAG, "onTouchEvent: dx = " + dx);
                mVelocityTracker.clear();
                break;
            }
            default:
                break;
        }
        mLastX = x;
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            setMeasuredDimension(widthSpaceSize, childView.getMeasuredHeight());
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth, heightSpaceSize);
        } else {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    /**
     * 横向滑动
     */
    private void smoothScrollBy(int dx) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
