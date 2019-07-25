package com.android.picker.picker;

import com.android.picker.bean.BaseBean;

/**
 * @author hanxw
 * @time 2019/7/25 9:28
 */
public interface IPickerListener {
    /**
     * 选择回调信息
     *
     * @param doorType
     * @param bigType
     * @param centreType
     */
    void onSelected(BaseBean doorType, BaseBean bigType, BaseBean centreType);

    /**
     * 取消
     */
    void onCancel();
}
