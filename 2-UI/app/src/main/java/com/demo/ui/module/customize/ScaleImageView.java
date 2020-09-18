package com.demo.ui.module.customize;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

/**
 * @author 尉迟涛
 * create time : 2019/11/26 22:56
 * description : 手势放大图片
 */
public class ScaleImageView extends AppCompatImageView implements
        ViewTreeObserver.OnGlobalLayoutListener,
        View.OnTouchListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = ScaleImageView.class.getSimpleName();
    private boolean mOnce = false;
    private Matrix mMatrix;
    private float mInitScale, mMidScale, mMaxScale;
    private float mTouchSlop;
    private float mLastPointerX, mLastPointerY;

    private boolean isScaling;//是否正在放大或缩小---防止用户在正在放大或缩小时疯狂点击
    private boolean isCanDrag;
    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    /**
     * 记录上次多点触控的数量
     */
    private int mLastPointerCount;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;


    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        mMatrix = new Matrix();
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isScaling) {
                    return true;
                }
                //以此点为缩放中心
                float x = e.getX();
                float y = e.getY();

                if (getScale() < mMidScale) {
                    postDelayed(new SlowlyScaleRunnable(mMidScale, x, y), 16);
                    isScaling = true;
//					mMatrix.postScale(mMidScale/getScale(), mMidScale/getScale(), x, y);
//					checkBorderAndCenterWhenScale();
//					setImageMatrix(mMatrix);
                } else {
                    postDelayed(new SlowlyScaleRunnable(mInitScale, x, y), 16);
                    isScaling = true;
//					mMatrix.postScale(mInitScale/getScale(), mInitScale/getScale(), x, y);
//					checkBorderAndCenterWhenScale();
//					setImageMatrix(mMatrix);
                }
                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //注册onGlobalLayoutListener，可以考虑用view的post方法
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除onGlobalLayoutListener
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * 捕获图片加载完成事件 onMeasure 和onDraw都不适合
     */
    @Override
    public void onGlobalLayout() {
        //初始化的操作 一次就好  为了保证对缩放只进行一次
        if (!mOnce) {
            //得到控件的宽和高--不一定是屏幕的宽和高 可能会有actionBar等等
            int width = getWidth();
            int height = getHeight();

            //得到我们的图片 以及宽和高
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            // 这里说下Drawable这个抽象类，具体实现类为BitmapDrawable
            // BitmapDrawable这个类重写了getIntrinsicWidth()和getIntrinsicHeight()方法
            // 这两个方法看字面意思就知道是什么了，就是得到图片固有的宽和高的
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            Log.e(TAG, intrinsicWidth + ":intrinsicWidth");
            Log.e(TAG, intrinsicHeight + ":intrinsicHeight");
            // 如果图片宽度比控件宽度小  高度比控件大 需要缩小
            float scale = 1.0f;//缩放的比例因子
            if (width > intrinsicWidth && height < intrinsicHeight) {
                scale = height * 1.0f / intrinsicHeight;
            }
            // 如果图片比控件大 需要缩小
            if (width < intrinsicWidth && height > intrinsicHeight) {
                scale = width * 1.0f / intrinsicWidth;
            }

            if ((width < intrinsicWidth && height < intrinsicHeight) || (width > intrinsicWidth && height > intrinsicHeight)) {
                scale = Math.min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
            }

            //得到初始化缩放的比例
            mInitScale = scale;
            mMidScale = 2 * mInitScale;//双击放大的值
            mMaxScale = 4 * mInitScale;//放大的最大值

            //将图片移动到控件的中心
            int dx = width / 2 - intrinsicWidth / 2;
            int dy = height / 2 - intrinsicHeight / 2;
            //将一些参数设置到图片或控件上 设置平移缩放 旋转
            mMatrix.postTranslate(dx, dy);
            mMatrix.postScale(mInitScale, mInitScale, width / 2f, height / 2f);//以控件的中心进行缩放
            setImageMatrix(mMatrix);

            mOnce = true;
        }
    }

    /**
     * 获取图片当前的缩放值
     */
    public float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        //捕获用户多指触控时系统计算缩放的比例---因为有缩放区间，所以需要添加区间判断逻辑
        float scaleFactor = detector.getScaleFactor();
        Log.e("ScaleGestureDetector", "scaleFactor:" + scaleFactor);
        if (getDrawable() == null) {
            return true;
        }

        //最大最小控制
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            //不断检测 控制白边和中心位置
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.e(TAG, "onScaleBegin");
        return true;//修改为true 才会进入onScale()这个函数  否则多指触控一直走onScaleBegin方法 不走 onScale和 onScaleEnd方法
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.e(TAG, "onScaleEnd");
    }

    /**
     * 获得图片放大或缩小之后的宽和高 以及 left top right bottom的坐标点
     */
    private RectF getMatrixRectF() {
        RectF rect = new RectF();
        Drawable drawable = getDrawable();
        if (null != drawable) {
            rect.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mMatrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 在缩放的时候进行边界控制以及我们的中心位置控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        //控件的宽和高
        int width = getWidth();
        int height = getHeight();
        Log.i("top", "top:" + rect.top);
        Log.i("left", "left:" + rect.left);
        Log.i("right", "right:" + rect.right);
        Log.i("bottom", "bottom:" + rect.bottom);

        //如果图片的宽和高大于控件的宽和高 在缩放过程中会产生border 进行偏移补偿
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }

        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }

        //如果图片的宽和高小于控件的宽和高 让其居中
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //双击放大与缩小事件传递给GestureDetector 放在最前面 防止双击时还能产生移动的事件响应
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        //将手势传递给ScaleGestureDetector
        /*boolean onTouchEvent =*/ mScaleGestureDetector.onTouchEvent(event);

        //-------------------------将放大的图片自由移动逻辑处理-----------------start------------
        //得到触控中心点的坐标
        float pointerX = 0;
        float pointerY = 0;
        //拿到多点触控的数量
        int pointerCount = event.getPointerCount();
        Log.i("pointerCount", "pointerCount:" + pointerCount);
        for (int i = 0; i < pointerCount; i++) {
            pointerX += event.getX(i);
            pointerY += event.getY(i);
        }
        pointerX /= pointerCount;
        pointerY /= pointerCount;
        if (mLastPointerCount != pointerCount) {
            //手指发生改变时 需要重新判断 是否能够移动
            isCanDrag = false;
            mLastPointerX = pointerX;
            mLastPointerY = pointerY;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果图片放大时 处理图片平移与ViewPager的滑动冲突
                if (getParent() instanceof ViewPager ||
                        (getParent() != null && getParent().getParent() instanceof ScrollView)) {
                    if (rectF.width() - getWidth() > 0.01 || rectF.height() - getHeight() > 0.01) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //如果图片放大时 处理图片平移与ViewPager的滑动冲突
                if (getParent() instanceof ViewPager ||
                        (getParent() != null && getParent().getParent() instanceof ScrollView)) {
                    if (rectF.width() - getWidth() > 0.01 || rectF.height() - getHeight() > 0.01) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                float dx = pointerX - mLastPointerX;
                float dy = pointerY - mLastPointerY;
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        //如果图片宽度小于控件宽度 不允许横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //如果图片的高度小于控件的高度 不允许纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }

                        mMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mMatrix);
                    }
                }
                mLastPointerX = pointerX;
                mLastPointerY = pointerY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
            default:
        }
        //-------------------------将放大的图片自由移动逻辑处理-------------------end----------
        return true;
    }

    /**
     * 当移动时 进行边界检查
     */
    private void checkBorderWhenTranslate() {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top;
        }
        if (rect.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rect.bottom;
        }

        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left;
        }
        if (rect.right < width && isCheckLeftAndRight) {
            deltaX = width - rect.right;
        }

        mMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 判断滑动的距离是否触发滑动的临界条件
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    /**
     * 自动放大与缩小
     */
    private class SlowlyScaleRunnable implements Runnable {
        //缩放的目标值
        private float mTargetScale;
        //缩放的中心点
        private float x;
        private float y;

        //放大与缩小的梯度
        private final float BEGGER = 1.07F;
        private final float SMALL = 0.97F;

        private float tmpScale;

        SlowlyScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BEGGER;
            }
            if (getScale() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            Log.e(TAG, "run scale");
            //进行缩放
            mMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
            float currentScale = getScale();
            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                //循环调用
                postDelayed(this, 16);
            } else {
                isScaling = false;
                //到达了目标值
                float scale = mTargetScale / currentScale;
                mMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mMatrix);
            }
        }

    }
}
