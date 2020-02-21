##ViewPager的点击事件分发
主要分析 onInterceptTouchEvent 和 onTouchEvent

### onInterceptTouchEvent
```
@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    /*
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onMotionEvent will be called and we do the actual
     * scrolling there.
     */

    final int action = ev.getAction() & MotionEvent.ACTION_MASK;

    // 如果是 CANCEL 或 UP 事件，则返回 false，将事件分发给子 View
    // Always take care of the touch gesture being complete.
    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
        // Release the drag.
        if (DEBUG) Log.v(TAG, "Intercept done!");
        resetTouch();
        return false;
    }

    // 排除 CANCEL 和 UP 事件，如果点击事件也不是 DOWN 的话：
    // Nothing more to do here if we have decided whether or not we
    // are dragging.（这里判断是否正在拖动）
    if (action != MotionEvent.ACTION_DOWN) {
        // 1、判断是否即将开始拖动，是则不拦截。当出现横向滑动，或者 ViewPager
        // 正在完成自动归位的动画的时候，mIsBeingDragged 会被赋值为true。
        if (mIsBeingDragged) {
            if (DEBUG) Log.v(TAG, "Intercept returning true!");
            return true;
        }
        // 2、判断是否不能滑动，是则拦截。当 ViewPager 横向滑动与子 Vive 有滑动冲突时，
        // 或者出现竖直滑动时，mIsUnableToDrag 会被赋值为 true。
        if (mIsUnableToDrag) {
            if (DEBUG) Log.v(TAG, "Intercept returning false!");
            return false;
        }
    }

    switch (action) {
        case MotionEvent.ACTION_MOVE: {
            /*
             * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
             * whether the user has moved far enough from his original down touch.
             */

            /*
            * Locally do absolute value. mLastMotionY is set to the y value
            * of the down event.
            */
            final int activePointerId = mActivePointerId;
            if (activePointerId == INVALID_POINTER) {
                // 如果没有一个有效点击，就跳出 switch 语句。
                // If we don't have a valid id, the touch down wasn't on content.
                break;
            }

            // 获取有效点击的下标
            final int pointerIndex = ev.findPointerIndex(activePointerId);
            // 获取有效点击的横坐标
            final float x = ev.getX(pointerIndex);
            // 获取横向变化量，mLastMotionX 在 DOWN 或 MOVW 事件中赋值
            final float dx = x - mLastMotionX;
            // xDiff 是绝对值
            final float xDiff = Math.abs(dx);
            // 获取有效点击的纵坐标
            final float y = ev.getY(pointerIndex);
            // yDiff 同理
            final float yDiff = Math.abs(y - mInitialMotionY);
            if (DEBUG) Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);

            // 如果这次 MOVE 有横向滑动，并且手指不在一侧的沟中进行同向滑动
            // 并且坐标点在子 View 中可横向滑动的控件上
            if (dx != 0 && !isGutterDrag(mLastMotionX, dx)
                    && canScroll(this, false, (int) dx, (int) x, (int) y)) {
                // Nested view has scrollable area under this point. Let it be handled there.
                // 记录 last xy
                mLastMotionX = x;
                mLastMotionY = y;
                // 设置不能拖拽为 true
                mIsUnableToDrag = true;
                // 继续向子 View 分发
                return false;
            }
            // 如果是横向滑动（mTouchSlop 是满足滑动的最小变化值，不满足这个值则不视为滑动）
            // 并且横向滑动距离是纵向的两倍还多
            if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                if (DEBUG) Log.v(TAG, "Starting drag!");
                // 设置即将拖拽为 true
                mIsBeingDragged = true;
                // 通知父 View 禁止打断事件，要把事件传下来
                requestParentDisallowInterceptTouchEvent(true);
                // 设置自身状态为“正在拖动”
                setScrollState(SCROLL_STATE_DRAGGING);
                // 记录 last xy，横坐标变化一个 mTouchSlop 的量，纵坐标记录新值
                mLastMotionX = dx > 0
                        ? mInitialMotionX + mTouchSlop : mInitialMotionX - mTouchSlop;
                mLastMotionY = y;
                // 开启子 View 绘制缓存，提高绘图速度
                setScrollingCacheEnabled(true);
            } else if (yDiff > mTouchSlop) {
                // The finger has moved enough in the vertical
                // direction to be counted as a drag...  abort
                // any attempt to drag horizontally, to work correctly
                // with children that have scrolling containers.
                if (DEBUG) Log.v(TAG, "Starting unable to drag!");
                // 纵向滑动，设置不能拖动为 true
                mIsUnableToDrag = true;
            }

            // 如果此时即将进行拖拽
            if (mIsBeingDragged) {
                // Scroll to follow the motion event
                // 滑动 ~
                if (performDrag(x)) {
                    // 刷新界面
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
            break;
        }

        case MotionEvent.ACTION_DOWN: {
            /*
             * Remember location of down touch.
             * ACTION_DOWN always refers to pointer index 0.
             */
            // 记录 last xy
            mLastMotionX = mInitialMotionX = ev.getX();
            mLastMotionY = mInitialMotionY = ev.getY();
            mActivePointerId = ev.getPointerId(0);
            mIsUnableToDrag = false;

            mIsScrollStarted = true;
            // 计算当前偏移量
            // 题外话：对于 Scroller，当 startScroll 执行过程中即在 duration 时间内，
            // computeScrollOffset 方法会一直返回false，但当动画执行完成后会返回返加true.
            mScroller.computeScrollOffset();
            // 如果 ViewPager 正在完成自动归位的动画的时候按住
            if (mScrollState == SCROLL_STATE_SETTLING
                    && Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough) {
                // Let the user 'catch' the pager as it animates.
                // 这个时候有 DOWN 事件，就是 ViewPager 在执行动画的时候被用户按住了
                // 把动画停了，之后 UP 的时候会给 pager 归位
                mScroller.abortAnimation();
                // 是否等待填充（绘制 pager）
                mPopulatePending = false;
                // 绘制 ~
                populate();
                mIsBeingDragged = true;
                requestParentDisallowInterceptTouchEvent(true);
                // 设置自身状态为“拖拽中”
                setScrollState(SCROLL_STATE_DRAGGING);
            } else {
                // 否则直接结束滚动
                completeScroll(false);
                mIsBeingDragged = false;
            }

            if (DEBUG) {
                Log.v(TAG, "Down at " + mLastMotionX + "," + mLastMotionY
                        + " mIsBeingDragged=" + mIsBeingDragged
                        + "mIsUnableToDrag=" + mIsUnableToDrag);
            }
            break;
        }

        case MotionEvent.ACTION_POINTER_UP:
            // 一个手指起来了，找找还有没有手指可以替代的
            onSecondaryPointerUp(ev);
            break;
    }

    // 获取速度计算器
    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    /*
     * The only time we want to intercept motion events is if we are in the
     * drag mode.
     */
     // 返回是否即将拖拽，是就打断事假自己处理，否则继续往子 View 传
    return mIsBeingDragged;
}
```
### onTouchEvent
```
@Override
public boolean onTouchEvent(MotionEvent ev) {
    // 假拖动，消费掉
    if (mFakeDragging) {
        // A fake drag is in progress already, ignore this real one
        // but still eat the touch events.
        // (It is likely that the user is multi-touching the screen.)
        return true;
    }

    // 如果是 DOWN 事件，并且手指碰到 ViewPager 的边了，不消费
    if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
        // Don't handle edge touches immediately -- they may actually belong to one of our
        // descendants.
        return false;
    }

    // ViewPager 是空的，不消费
    if (mAdapter == null || mAdapter.getCount() == 0) {
        // Nothing to present or scroll; nothing to touch.
        return false;
    }

    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    final int action = ev.getAction();
    boolean needsInvalidate = false;

    switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: {
            // 停了动画，和 Intecept 中的有点像
            mScroller.abortAnimation();
            mPopulatePending = false;
            populate();

            // Remember where the motion event started
            // 记录 last xy
            mLastMotionX = mInitialMotionX = ev.getX();
            mLastMotionY = mInitialMotionY = ev.getY();
            mActivePointerId = ev.getPointerId(0);
            break;
        }
        case MotionEvent.ACTION_MOVE:
            if (!mIsBeingDragged) {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    // A child has consumed some touch events and put us into an inconsistent
                    // state.
                    // 重置
                    needsInvalidate = resetTouch();
                    break;
                }
                final float x = ev.getX(pointerIndex);
                final float xDiff = Math.abs(x - mLastMotionX);
                final float y = ev.getY(pointerIndex);
                final float yDiff = Math.abs(y - mLastMotionY);
                if (DEBUG) {
                    Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                }
                // 横向滑动
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    if (DEBUG) Log.v(TAG, "Starting drag!");
                    mIsBeingDragged = true;
                    // 记得通知父 View
                    requestParentDisallowInterceptTouchEvent(true);
                    mLastMotionX = x - mInitialMotionX > 0 ? mInitialMotionX + mTouchSlop :
                            mInitialMotionX - mTouchSlop;
                    mLastMotionY = y;
                    // 开始滑了都要设置这两个参数
                    setScrollState(SCROLL_STATE_DRAGGING);
                    setScrollingCacheEnabled(true);

                    // Disallow Parent Intercept, just in case
                    // 告诉爷爷...
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            // 注意这里没用 else 喔，mIsBeingDragged 可能会变化
            // Not else! Note that mIsBeingDragged can be set above.
            if (mIsBeingDragged) {
                // Scroll to follow the motion event
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(activePointerIndex);
                // 或等于
                needsInvalidate |= performDrag(x);
            }
            break;
        case MotionEvent.ACTION_UP:
            // 如果是即将滑动的
            if (mIsBeingDragged) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                // 准备画新 pager 了
                mPopulatePending = true;
                final int width = getClientWidth();
                final int scrollX = getScrollX();
                // 获取当前 pager item 信息
                final ItemInfo ii = infoForCurrentScrollPosition();
                final float marginOffset = (float) mPageMargin / width;
                final int currentPage = ii.position;
                final float pageOffset = (((float) scrollX / width) - ii.offset)
                        / (ii.widthFactor + marginOffset);
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(activePointerIndex);
                // 横向滑动距离
                final int totalDelta = (int) (x - mInitialMotionX);
                // 判断下一个显示的 pager
                int nextPage = determineTargetPage(currentPage, pageOffset, initialVelocity,
                        totalDelta);
                // 就像我们调用 setCurrentItem
                setCurrentItemInternal(nextPage, true, true, initialVelocity);
                // 重置
                needsInvalidate = resetTouch();
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            if (mIsBeingDragged) {
                // 取消，滑回当前 pager，并重置触摸参数
                scrollToItem(mCurItem, true, 0, false);
                needsInvalidate = resetTouch();
            }
            break;
        case MotionEvent.ACTION_POINTER_DOWN: {
            // 当屏幕上已经有触点处于按下的状态的时候，再有新的触点被按下时触发。又有手指来了。
            final int index = ev.getActionIndex();
            final float x = ev.getX(index);
            // 把参数更新为新手指的
            mLastMotionX = x;
            mActivePointerId = ev.getPointerId(index);
            break;
        }
        case MotionEvent.ACTION_POINTER_UP:
            // 有手指起来了，看看有没有可以换的
            onSecondaryPointerUp(ev);
            mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId));
            break;
    }
    if (needsInvalidate) {
        ViewCompat.postInvalidateOnAnimation(this);
    }
    return true;
}
```

### resetTouch
```
private boolean resetTouch() {
    boolean needsInvalidate;
    // mActivePointerId 用来记录有效点击的 ID，INVALID_POINTER 表示无效点击
    // 我们每次点击的时候，点击事件都会有自己的 ID
    mActivePointerId = INVALID_POINTER;
    endDrag();
    // mLeftEdge 与 mRightEdge 是 EdgeEffect 的对象，用来控制 ViewPager 的边缘特效
    mLeftEdge.onRelease();
    mRightEdge.onRelease();
    needsInvalidate = mLeftEdge.isFinished() || mRightEdge.isFinished();
    return needsInvalidate;
}
```

### endDrag
```
private void endDrag() {
    // 设置即将拖动为 false，设置不能滑动为 false
    mIsBeingDragged = false;
    mIsUnableToDrag = false;

    // 清空速度计算器的缓存
    if (mVelocityTracker != null) {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
}
```
### isGutterDrag
```
// ViewPager 内部的两边会有一段间距，叫 gutter
// |<--            ViewPager           -->|
// | gutter |   ViewPagerInner   | gutter |
// 这个方法判断横坐标是否在 gutter 内
private boolean isGutterDrag(float x, float dx) {
    // x 位于左侧 gutter 并且手指在向左滑；x 位于右侧 gutter 并且手指在向右滑
    return (x < mGutterSize && dx > 0) || (x > getWidth() - mGutterSize && dx < 0);
}
```
### canScroll
```
// 逐个检查子 View 是否能横向滑动
protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
    if (v instanceof ViewGroup) {
        final ViewGroup group = (ViewGroup) v;
        final int scrollX = v.getScrollX();
        final int scrollY = v.getScrollY();
        final int count = group.getChildCount();
        // Count backwards - let topmost views consume scroll distance first.
        for (int i = count - 1; i >= 0; i--) {
            // TODO: Add versioned support here for transformed views.
            // This will not work for transformed views in Honeycomb+
            final View child = group.getChildAt(i);
            if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight()
                    && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()
                    && canScroll(child, true, dx, x + scrollX - child.getLeft(),
                            y + scrollY - child.getTop())) {
                return true;
            }
        }
    }

    return checkV && v.canScrollHorizontally(-dx);
}
```
### setScrollingCacheEnabled
```
// 设置子 View 绘制缓存
// View 组件显示的内容可以通过 cache 机制保存为 bitmap，提高绘图速度
private void setScrollingCacheEnabled(boolean enabled) {
    if (mScrollingCacheEnabled != enabled) {
        mScrollingCacheEnabled = enabled;
        if (USE_CACHE) {
            final int size = getChildCount();
            for (int i = 0; i < size; ++i) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    child.setDrawingCacheEnabled(enabled);
                }
            }
        }
    }
}
```
### performDrag
```
// 真正的进行拖动
// x 为当前 MOVE 事件的横坐标
private boolean performDrag(float x) {
    boolean needsInvalidate = false;

    // 计算本次横向滑动量
    final float deltaX = mLastMotionX - x;
    // 设置 last x 为当前值
    mLastMotionX = x;

    // 计算新的 scrollX
    float oldScrollX = getScrollX();
    float scrollX = oldScrollX + deltaX;
    // 获 ViewPager 取除 padding 的宽度
    final int width = getClientWidth();

    // mFirstOffset 第一个item的偏移量；mLastOffset 最后一个item的偏移量；
    // 偏移量指的不是长度，而是多少个 page item
    float leftBound = width * mFirstOffset;
    float rightBound = width * mLastOffset;
    boolean leftAbsolute = true;
    boolean rightAbsolute = true;

    // 计算左右边界
    final ItemInfo firstItem = mItems.get(0);
    final ItemInfo lastItem = mItems.get(mItems.size() - 1);
    if (firstItem.position != 0) {
        leftAbsolute = false;
        leftBound = firstItem.offset * width;
    }
    if (lastItem.position != mAdapter.getCount() - 1) {
        rightAbsolute = false;
        rightBound = lastItem.offset * width;
    }

    // 拖到尽头时，展示左右动画效果
    if (scrollX < leftBound) {
        if (leftAbsolute) {
            float over = leftBound - scrollX;
            mLeftEdge.onPull(Math.abs(over) / width);
            needsInvalidate = true;
        }
        scrollX = leftBound;
    } else if (scrollX > rightBound) {
        if (rightAbsolute) {
            float over = scrollX - rightBound;
            mRightEdge.onPull(Math.abs(over) / width);
            needsInvalidate = true;
        }
        scrollX = rightBound;
    }
    // Don't lose the rounded component
    mLastMotionX += scrollX - (int) scrollX;
    // 滑动到新的 scrollX
    scrollTo((int) scrollX, getScrollY());
    // 滑动 page
    pageScrolled((int) scrollX);

    return needsInvalidate;
}
```
### getClientWidth
```
// 获 ViewPager 取除 padding 的宽度
private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
}
```
### pageScrolled
```
// page 滚动
private boolean pageScrolled(int xpos) {
    if (mItems.size() == 0) {
        if (mFirstLayout) {
            // If we haven't been laid out yet, we probably just haven't been populated yet.
            // Let's skip this call since it doesn't make sense in this state
            return false;
        }
        mCalledSuper = false;
        onPageScrolled(0, 0, 0);
        if (!mCalledSuper) {
            throw new IllegalStateException(
                    "onPageScrolled did not call superclass implementation");
        }
        return false;
    }
    // 获取当前滑动的iteminfo
    final ItemInfo ii = infoForCurrentScrollPosition();
    // 获 ViewPager 取除 padding 的宽度
    final int width = getClientWidth();
    final int widthWithMargin = width + mPageMargin;
    final float marginOffset = (float) mPageMargin / width;
    final int currentPage = ii.position;
    final float pageOffset = (((float) xpos / width) - ii.offset)
            / (ii.widthFactor + marginOffset);
    final int offsetPixels = (int) (pageOffset * widthWithMargin);

    mCalledSuper = false;
    // 回调接口
    onPageScrolled(currentPage, pageOffset, offsetPixels);
    if (!mCalledSuper) {
        throw new IllegalStateException(
                "onPageScrolled did not call superclass implementation");
    }
    return true;
}
```
### infoForCurrentScrollPosition
```
// 获取当前滑动的iteminfo
private ItemInfo infoForCurrentScrollPosition() {
    final int width = getClientWidth();
    final float scrollOffset = width > 0 ? (float) getScrollX() / width : 0;
    final float marginOffset = width > 0 ? (float) mPageMargin / width : 0;
    int lastPos = -1;
    float lastOffset = 0.f;
    float lastWidth = 0.f;
    boolean first = true;

    ItemInfo lastItem = null;
    for (int i = 0; i < mItems.size(); i++) {
        ItemInfo ii = mItems.get(i);
        // 这里的 offset 是具体的滑动距离
        float offset;
        // 不是第一个也不是最后一个
        if (!first && ii.position != lastPos + 1) {
            // Create a synthetic item for a missing page.
            ii = mTempItem;
            ii.offset = lastOffset + lastWidth + marginOffset;
            ii.position = lastPos + 1;
            ii.widthFactor = mAdapter.getPageWidth(ii.position);
            i--;
        }
        offset = ii.offset;

        final float leftBound = offset;
        final float rightBound = offset + ii.widthFactor + marginOffset;
        // 是第一个并且滑到了左边的尽头
        // getScrollX()大于0，整个页面是左滑了的，就是左边有一部分已经看不到了
        // 反之，如果 getScrollX() 小于0，整个页面是右滑了的
        if (first || scrollOffset >= leftBound) {
            if (scrollOffset < rightBound || i == mItems.size() - 1) {
                return ii;
            }
        } else {
            return lastItem;
        }
        first = false;
        lastPos = ii.position;
        lastOffset = offset;
        lastWidth = ii.widthFactor;
        lastItem = ii;
    }

    return lastItem;
}
```
### completeScroll
```
//  结束滚动的方法
private void completeScroll(boolean postEvents) {
    boolean needPopulate = mScrollState == SCROLL_STATE_SETTLING;
    if (needPopulate) {
        // Done with scroll, no longer want to cache view drawing.
        setScrollingCacheEnabled(false);
        boolean wasScrolling = !mScroller.isFinished();
        if (wasScrolling) {
            mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
                if (x != oldX) {
                    pageScrolled(x);
                }
            }
        }
    }
    mPopulatePending = false;
    for (int i = 0; i < mItems.size(); i++) {
        ItemInfo ii = mItems.get(i);
        if (ii.scrolling) {
            needPopulate = true;
            ii.scrolling = false;
        }
    }
    if (needPopulate) {
        if (postEvents) {
            ViewCompat.postOnAnimation(this, mEndScrollRunnable);
        } else {
            mEndScrollRunnable.run();
        }
    }
}
```
### populate
```
// 确定选中项进行绘制
void populate() {
    populate(mCurItem);
}

// 真正的确定选中项后进行绘制
void populate(int newCurrentItem) {
    ItemInfo oldCurInfo = null;
    if (mCurItem != newCurrentItem) {
        oldCurInfo = infoForPosition(mCurItem);
        mCurItem = newCurrentItem;
    }

    if (mAdapter == null) {
        sortChildDrawingOrder();
        return;
    }

    // Bail now if we are waiting to populate.  This is to hold off
    // on creating views from the time the user releases their finger to
    // fling to a new position until we have finished the scroll to
    // that position, avoiding glitches from happening at that point.
    if (mPopulatePending) {
        if (DEBUG) Log.i(TAG, "populate is pending, skipping for now...");
        sortChildDrawingOrder();
        return;
    }

    // Also, don't populate until we are attached to a window.  This is to
    // avoid trying to populate before we have restored our view hierarchy
    // state and conflicting with what is restored.
    if (getWindowToken() == null) {
        return;
    }

    mAdapter.startUpdate(this);

    final int pageLimit = mOffscreenPageLimit;
    final int startPos = Math.max(0, mCurItem - pageLimit);
    final int N = mAdapter.getCount();
    final int endPos = Math.min(N - 1, mCurItem + pageLimit);

    if (N != mExpectedAdapterCount) {
        String resName;
        try {
            resName = getResources().getResourceName(getId());
        } catch (Resources.NotFoundException e) {
            resName = Integer.toHexString(getId());
        }
        throw new IllegalStateException("The application's PagerAdapter changed the adapter's"
                + " contents without calling PagerAdapter#notifyDataSetChanged!"
                + " Expected adapter item count: " + mExpectedAdapterCount + ", found: " + N
                + " Pager id: " + resName
                + " Pager class: " + getClass()
                + " Problematic adapter: " + mAdapter.getClass());
    }

    // Locate the currently focused item or add it if needed.
    int curIndex = -1;
    ItemInfo curItem = null;
    for (curIndex = 0; curIndex < mItems.size(); curIndex++) {
        final ItemInfo ii = mItems.get(curIndex);
        if (ii.position >= mCurItem) {
            if (ii.position == mCurItem) curItem = ii;
            break;
        }
    }

    if (curItem == null && N > 0) {
        curItem = addNewItem(mCurItem, curIndex);
    }

    // Fill 3x the available width or up to the number of offscreen
    // pages requested to either side, whichever is larger.
    // If we have no current item we have no work to do.
    if (curItem != null) {
        float extraWidthLeft = 0.f;
        int itemIndex = curIndex - 1;
        ItemInfo ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
        final int clientWidth = getClientWidth();
        final float leftWidthNeeded = clientWidth <= 0 ? 0 :
                2.f - curItem.widthFactor + (float) getPaddingLeft() / (float) clientWidth;
        for (int pos = mCurItem - 1; pos >= 0; pos--) {
            if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                if (ii == null) {
                    break;
                }
                if (pos == ii.position && !ii.scrolling) {
                    mItems.remove(itemIndex);
                    mAdapter.destroyItem(this, pos, ii.object);
                    if (DEBUG) {
                        Log.i(TAG, "populate() - destroyItem() with pos: " + pos
                                + " view: " + ((View) ii.object));
                    }
                    itemIndex--;
                    curIndex--;
                    ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
                }
            } else if (ii != null && pos == ii.position) {
                extraWidthLeft += ii.widthFactor;
                itemIndex--;
                ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
            } else {
                ii = addNewItem(pos, itemIndex + 1);
                extraWidthLeft += ii.widthFactor;
                curIndex++;
                ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
            }
        }

        float extraWidthRight = curItem.widthFactor;
        itemIndex = curIndex + 1;
        if (extraWidthRight < 2.f) {
            ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
            final float rightWidthNeeded = clientWidth <= 0 ? 0 :
                    (float) getPaddingRight() / (float) clientWidth + 2.f;
            for (int pos = mCurItem + 1; pos < N; pos++) {
                if (extraWidthRight >= rightWidthNeeded && pos > endPos) {
                    if (ii == null) {
                        break;
                    }
                    if (pos == ii.position && !ii.scrolling) {
                        mItems.remove(itemIndex);
                        mAdapter.destroyItem(this, pos, ii.object);
                        if (DEBUG) {
                            Log.i(TAG, "populate() - destroyItem() with pos: " + pos
                                    + " view: " + ((View) ii.object));
                        }
                        ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                    }
                } else if (ii != null && pos == ii.position) {
                    extraWidthRight += ii.widthFactor;
                    itemIndex++;
                    ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                } else {
                    ii = addNewItem(pos, itemIndex);
                    itemIndex++;
                    extraWidthRight += ii.widthFactor;
                    ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                }
            }
        }

        calculatePageOffsets(curItem, curIndex, oldCurInfo);

        mAdapter.setPrimaryItem(this, mCurItem, curItem.object);
    }

    if (DEBUG) {
        Log.i(TAG, "Current page list:");
        for (int i = 0; i < mItems.size(); i++) {
            Log.i(TAG, "#" + i + ": page " + mItems.get(i).position);
        }
    }

    mAdapter.finishUpdate(this);

    // Check width measurement of current pages and drawing sort order.
    // Update LayoutParams as needed.
    final int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
        final View child = getChildAt(i);
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        lp.childIndex = i;
        if (!lp.isDecor && lp.widthFactor == 0.f) {
            // 0 means requery the adapter for this, it doesn't have a valid width.
            final ItemInfo ii = infoForChild(child);
            if (ii != null) {
                lp.widthFactor = ii.widthFactor;
                lp.position = ii.position;
            }
        }
    }
    sortChildDrawingOrder();

    if (hasFocus()) {
        View currentFocused = findFocus();
        ItemInfo ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
        if (ii == null || ii.position != mCurItem) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                ii = infoForChild(child);
                if (ii != null && ii.position == mCurItem) {
                    if (child.requestFocus(View.FOCUS_FORWARD)) {
                        break;
                    }
                }
            }
        }
    }
}
```
### onSecondaryPointerUp
```
private void onSecondaryPointerUp(MotionEvent ev) {
    final int pointerIndex = ev.getActionIndex();
    final int pointerId = ev.getPointerId(pointerIndex);
    if (pointerId == mActivePointerId) {
        // This was our active pointer going up. Choose a new
        // active pointer and adjust accordingly.
        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
        mLastMotionX = ev.getX(newPointerIndex);
        mActivePointerId = ev.getPointerId(newPointerIndex);
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }
}
```
### determineTargetPage
```
// 确定目标page
private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
    int targetPage;
    if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
        targetPage = velocity > 0 ? currentPage : currentPage + 1;
    } else {
        final float truncator = currentPage >= mCurItem ? 0.4f : 0.6f;
        targetPage = currentPage + (int) (pageOffset + truncator);
    }

    if (mItems.size() > 0) {
        final ItemInfo firstItem = mItems.get(0);
        final ItemInfo lastItem = mItems.get(mItems.size() - 1);

        // Only let the user target pages we have items for
        targetPage = Math.max(firstItem.position, Math.min(targetPage, lastItem.position));
    }

    return targetPage;
}
```
### setCurrentItemInternal
```
// 真正执行item选中的方法，第一个参数为要选中的item，第二个参数为是否是由顺滑滑动，
// 第三个参数为是否是永久的，第四个参数为滑动速度
void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
    if (mAdapter == null || mAdapter.getCount() <= 0) {
        setScrollingCacheEnabled(false);
        return;
    }
    if (!always && mCurItem == item && mItems.size() != 0) {
        setScrollingCacheEnabled(false);
        return;
    }

    if (item < 0) {
        item = 0;
    } else if (item >= mAdapter.getCount()) {
        item = mAdapter.getCount() - 1;
    }
    final int pageLimit = mOffscreenPageLimit;
    if (item > (mCurItem + pageLimit) || item < (mCurItem - pageLimit)) {
        // We are doing a jump by more than one page.  To avoid
        // glitches, we want to keep all current pages in the view
        // until the scroll ends.
        for (int i = 0; i < mItems.size(); i++) {
            mItems.get(i).scrolling = true;
        }
    }
    final boolean dispatchSelected = mCurItem != item;

    if (mFirstLayout) {
        // We don't have any idea how big we are yet and shouldn't have any pages either.
        // Just set things up and let the pending layout handle things.
        mCurItem = item;
        if (dispatchSelected) {
            dispatchOnPageSelected(item);
        }
        requestLayout();
    } else {
        populate(item);
        scrollToItem(item, smoothScroll, velocity, dispatchSelected);
    }
}
```
参考：https://blog.csdn.net/shenglong0210/article/details/88533432
