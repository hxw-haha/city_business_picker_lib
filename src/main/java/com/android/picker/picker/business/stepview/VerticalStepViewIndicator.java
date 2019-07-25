package com.android.picker.picker.business.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.android.picker.R;
import com.android.picker.picker.business.listener.IDrawIndicatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanxw
 * @time 2019/7/8 14:32
 */
class VerticalStepViewIndicator extends View {
    /**
     * 定义默认的高度
     */
    private int defaultStepIndicatorNum =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

    /**
     * 当前有几部流程
     */
    private int mStepNum = 0;
    /**
     * 该View的X轴的中间位置
     */
    private float mCenterX;
    /**
     * 这个控件的动态高度
     */
    private int mHeight;
    /**
     * 圆的半径
     */
    private float mCircleRadius;
    /**
     * 两条连线之间的间距
     */
    private float mLinePadding;
    /**
     * 定义所有圆的圆心点位置的集合
     */
    private List<Float> mCircleCenterPointPositionList;

    private Paint mPaint;
    private Path mPath;

    private IDrawIndicatorListener mOnDrawListener;


    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum) {
        this.mStepNum = stepNum;
        this.requestLayout();

    }

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(IDrawIndicatorListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }


    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        initPaint();
        //圆的半径
        mCircleRadius = 0.12f * defaultStepIndicatorNum;
        //线与线之间的间距
        mLinePadding = 0.98f * defaultStepIndicatorNum;

        mCircleCenterPointPositionList = new ArrayList<>();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(2);

        mPaint.setColor(getResources().getColor(R.color.select_color));
        mPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = defaultStepIndicatorNum;
        mHeight = 0;
        if (mStepNum > 0) {
            mHeight = (int) (getPaddingTop() + getPaddingBottom() + mCircleRadius * 2 * mStepNum + (mStepNum - 1) * mLinePadding) + defaultStepIndicatorNum;
        }

        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }


        setMeasuredDimension(width, mHeight);
        Log.e("stepview", "onMeasure::width=" + width + ",height=" + mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 清空圆点集合，使画布重新画圆
        mCircleCenterPointPositionList.clear();
        // 清空线路信息，使画布重新画线
        mPath.reset();
        mCenterX = getWidth() / 2;
        for (int i = 0; i < mStepNum; i++) {
            mCircleCenterPointPositionList.add(defaultStepIndicatorNum / 2 + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }
        if (mOnDrawListener != null) {
            mOnDrawListener.onDrawIndicator();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画线
        for (int i = 0; i < mStepNum - 1; i++) {
            //前一个ComplectedXPosition
            final float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);

            mPath.moveTo(mCenterX, preComplectedXPosition + mCircleRadius);
            mPath.lineTo(mCenterX, afterComplectedXPosition - mCircleRadius);
            canvas.drawPath(mPath, mPaint);
        }

        // 画圆
        for (int i = 0; i < mStepNum; i++) {
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            if (i == mStepNum - 1) {
                // 描边
                mPaint.setStyle(Paint.Style.STROKE);
            } else {
                // 描边加填充
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            canvas.drawCircle(mCenterX, currentComplectedXPosition, mCircleRadius, mPaint);
        }

        if (mOnDrawListener != null) {
            mOnDrawListener.onDrawIndicator();
        }
    }


}
