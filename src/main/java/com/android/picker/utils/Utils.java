package com.android.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import com.android.picker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Utils {


    /**
     * 获取 assets 文件内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 设置选中的text color
     *
     * @param view
     */
    public static void selectTextColor(AppCompatTextView view) {
        if (view == null) {
            return;
        }
        view.setTextColor(view.getContext().getResources().getColor(R.color.select_color));
    }

    /**
     * 设置未选中的text color
     *
     * @param view
     */
    public static void unSelectTextColor(AppCompatTextView view) {
        if (view == null) {
            return;
        }
        view.setTextColor(view.getContext().getResources().getColor(R.color.u_select_color));
    }


    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
        if (bgAlpha == 1f) {
            clearDim((Activity) mContext);
        } else {
            applyDim((Activity) mContext, bgAlpha);
        }
    }

    private static void applyDim(Activity activity, float bgAlpha) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
            Drawable dim = new ColorDrawable(Color.BLACK);
            dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            dim.setAlpha((int) (255 * bgAlpha));
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.add(dim);
        }
    }

    private static void clearDim(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.clear();
        }
    }
}
