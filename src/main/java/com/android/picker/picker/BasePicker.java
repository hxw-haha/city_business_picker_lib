package com.android.picker.picker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.picker.R;
import com.android.picker.utils.Utils;

/**
 * @author hanxw
 * @time 2019/7/8 18:24
 */
public abstract class BasePicker {

    private PopupWindow mPopWindow;
    protected IPickerListener mPickerListener;

    protected void initPopWindowView(final Context context, final View contentView, final View parentView) {
        mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWindow.setAnimationStyle(R.style.AnimBottom);
        mPopWindow.setBackgroundDrawable(new ColorDrawable());
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(false);
        mPopWindow.setFocusable(true);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.setBackgroundAlpha(context, 1.0f);
            }
        });
        if (!isShow()) {
            this.mPopWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        }
    }

    protected void hidePop() {
        if (isShow()) {
            mPopWindow.dismiss();
        }
    }

    private boolean isShow() {
        return mPopWindow.isShowing();
    }


    protected void selectTextColor(AppCompatTextView view) {
        Utils.selectTextColor(view);
    }

    protected void unSelectTextColor(AppCompatTextView view) {
        Utils.unSelectTextColor(view);
    }
}
