package com.android.picker.picker.business.stepview;

import com.android.picker.bean.BaseBean;
import com.android.picker.picker.business.listener.IItemClickListener;

/**
 * @author hanxw
 * @time 2019/7/8 17:27
 */
public class StepViewBean {
    public BaseBean baseBean;
    public IItemClickListener itemClickListener;


    public StepViewBean(BaseBean baseBean, IItemClickListener itemClickListener) {
        this.baseBean = baseBean;
        this.itemClickListener = itemClickListener;
    }
}
