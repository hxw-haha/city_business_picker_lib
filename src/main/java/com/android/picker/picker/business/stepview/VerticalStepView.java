package com.android.picker.picker.business.stepview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.picker.R;
import com.android.picker.bean.BaseBean;
import com.android.picker.picker.business.listener.IDrawIndicatorListener;
import com.android.picker.picker.business.listener.IItemClickListener;
import com.android.picker.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hanxw
 * @time 2019/7/8 15:55
 */
public class VerticalStepView extends LinearLayoutCompat implements IDrawIndicatorListener {

    private VerticalStepViewIndicator mStepsViewIndicator;
    private FrameLayout mTextContainer;
    private List<StepViewBean> mTitleText;
    private LinkedList<AppCompatTextView> mTextViews = new LinkedList<>();


    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_vertical_step_view, this);
        mStepsViewIndicator = (VerticalStepViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (FrameLayout) rootView.findViewById(R.id.rl_text_container);
    }

    /**
     * 更新 text view 选择的颜色
     * <p>true：选择状态</p>
     *
     * @param isDoorTypeFlag   是否是门类选择状态
     * @param isBigTypeFlag    是否是中类选择状态
     * @param isCentreTypeFlag 是否是大类选择状态
     */
    public void updateTextViewColor(boolean isDoorTypeFlag, boolean isBigTypeFlag, boolean isCentreTypeFlag) {
        if (mTextViews != null) {
            final int size = mTextViews.size();
            if (size > 0) {
                final AppCompatTextView doorView = mTextViews.get(0);
                if (isDoorTypeFlag) {
                    Utils.selectTextColor(doorView);
                } else {
                    Utils.unSelectTextColor(doorView);
                }
            }
            if (size > 1) {
                final AppCompatTextView bigView = mTextViews.get(1);
                if (isBigTypeFlag) {
                    Utils.selectTextColor(bigView);
                } else {
                    Utils.unSelectTextColor(bigView);
                }
            }
            if (size > 2) {
                final AppCompatTextView centreView = mTextViews.get(2);
                if (isCentreTypeFlag) {
                    Utils.selectTextColor(centreView);
                } else {
                    Utils.unSelectTextColor(centreView);
                }
            }
        }
    }

    /**
     * 设置显示的文字
     *
     * @param stepViewTexts
     * @return
     */
    public void setStepViewTexts(List<StepViewBean> stepViewTexts) {
        mTitleText = stepViewTexts;
        if (stepViewTexts != null) {
            mStepsViewIndicator.setStepNum(mTitleText.size());
        } else {
            mStepsViewIndicator.setStepNum(0);
        }
    }


    @Override
    public void onDrawIndicator() {
        if (mTextContainer != null) {
            mTextViews.clear();
            mTextContainer.removeAllViews();
            List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
            if (mTitleText != null && complectedXPosition != null && complectedXPosition.size() > 0) {
                final int titleTextSize = mTitleText.size();
                for (int i = 0; i < titleTextSize; i++) {
                    final BaseBean baseBean = mTitleText.get(i).baseBean;
                    final IItemClickListener itemClickListener = mTitleText.get(i).itemClickListener;

                    final AppCompatTextView textView = new AppCompatTextView(getContext());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setText(baseBean.name);
                    textView.setPadding(30, 10, 0, 10);

                    // 测量宽度
                    final int w = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    final int h = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    textView.measure(w, h);

                    float textViewY = complectedXPosition.get(i)
                            - textView.getMeasuredHeight() / 2;

                    textView.setY(textViewY);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//                    textView.setTypeface(null, Typeface.BOLD);
                    if ((titleTextSize - 1) == i) {
                        textView.setTextColor(getResources().getColor(R.color.select_color));
                    } else {
                        textView.setTextColor(getResources().getColor(R.color.u_select_color));
                    }


                    mTextContainer.addView(textView);
                    mTextViews.add(textView);

                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onItemClisk(baseBean);
                            }
                        }
                    });
                }
            }
        }
    }
}
