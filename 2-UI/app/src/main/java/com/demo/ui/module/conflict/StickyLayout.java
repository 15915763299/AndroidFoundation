package com.demo.ui.module.conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 尉迟涛
 * create time : 2019/11/23 17:26
 * description : 给recyclerView加个header
 */
public class StickyLayout extends LinearLayout {

    private int lastY;
    private int lastInterceptX, lastInterceptY;
    private int touchSlop;

    private int maxHeight, minHeight;

    private View header;
    private RecyclerView recyclerView;
    private Scroller scroller;

    public StickyLayout(Context context) {
        super(context);
        init();
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() / 2;
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        header = getChildAt(0);
        header.setClickable(true);
        recyclerView = (RecyclerView) getChildAt(1);

        maxHeight = getHeight();
        minHeight = recyclerView.getHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //防止回弹的时候的各种骚操作
                if (!scroller.isFinished()) {
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastInterceptX;
                int deltaY = y - lastInterceptY;

                boolean isScrollVertical = Math.abs(deltaY) >= Math.abs(deltaX);
                boolean isHeaderShowing = 0 <= getScrollY() && getScrollY() < header.getHeight();
                boolean isReachTop = !recyclerView.canScrollVertically(-1);
                boolean isScrollDown = deltaY >= touchSlop;
                //boolean isScrollUp = deltaY <= -touchSlop;

                if (isHeaderShowing) {
                    if (isScrollVertical) {
                        intercept = true;
                    }
                } else {
                    if (isReachTop && isScrollDown) {
                        intercept = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }

        lastInterceptX = x;
        lastY = lastInterceptY = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;

                ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
                lp.height = lp.height - deltaY;
                if (lp.height > maxHeight) {
                    lp.height = maxHeight;
                }
                if (lp.height < minHeight) {
                    lp.height = minHeight;
                }
                recyclerView.setLayoutParams(lp);

                scrollBy(0, -deltaY);
                break;
            case MotionEvent.ACTION_UP:
                int scrollerY = getScrollY();
                if (scrollerY < 0) {
                    smoothScrollTo(0);
                } else if (getScrollY() > header.getHeight()) {
                    smoothScrollTo(header.getHeight());
                }
                break;
            default:
        }
        lastY = y;
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 竖向滑动
     */
    private void smoothScrollTo(int y) {
        scroller.startScroll(0, getScrollY(), 0, y - getScrollY(), 500);
        invalidate();
    }
}
