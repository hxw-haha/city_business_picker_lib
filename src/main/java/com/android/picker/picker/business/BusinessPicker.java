package com.android.picker.picker.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.picker.R;
import com.android.picker.adapter.BaseListAdapter;
import com.android.picker.bean.BaseBean;
import com.android.picker.bean.business.BigTypeBean;
import com.android.picker.bean.business.BusinessBean;
import com.android.picker.bean.business.CentreTypeBean;
import com.android.picker.bean.business.DoorTypeBean;
import com.android.picker.config.Config;
import com.android.picker.picker.BasePicker;
import com.android.picker.picker.IPickerListener;
import com.android.picker.picker.business.listener.IItemClickListener;
import com.android.picker.picker.business.stepview.StepViewBean;
import com.android.picker.picker.business.stepview.VerticalStepView;
import com.android.picker.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author hanxw
 * @time 2019/7/8 18:11
 */
public class BusinessPicker extends BasePicker {

    private static BusinessPicker businessPicker;

    private static BusinessBean mBusinessBean;
    private View mContentView;
    private ListView mBusinessListView;
    private VerticalStepView mVerticalStepView;
    private LinkedList<StepViewBean> mTitleText;

    private BaseListAdapter<DoorTypeBean> mDoorAdapter;
    private BaseListAdapter<BigTypeBean> mBigAdapter;
    private BaseListAdapter<CentreTypeBean> mCentreAdapter;
    private int tabIndex = Config.INDEX_INVALID;

    private BusinessPicker() {

    }

    public static synchronized BusinessPicker getInstance() {
        if (businessPicker == null) {
            businessPicker = new BusinessPicker();
        }
        return businessPicker;
    }


    /**
     * 显示弹出框
     *
     * @param context
     * @param listener 选择信息回调
     */
    public void showCityPicker(Context context, IPickerListener listener) {
        initPickerPop(context);
        this.mPickerListener = listener;
    }

    private BusinessBean initBusinessData(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return new Gson().fromJson(Utils.getAssetJson(context, Config.BUSINESS_DATA), BusinessBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initPickerPop(final Context context) {
        if (mBusinessBean == null) {
            mBusinessBean = initBusinessData(context);
            if (mBusinessBean == null) {
                return;
            }
        }

        mTitleText = new LinkedList<>();
        initContentView(context);
        initPopWindowView(context, mContentView, mBusinessListView);
    }

    private void initContentView(Context context) {
        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_business_picker, null);
        mContentView.findViewById(R.id.close_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickerListener != null) {
                    mPickerListener.onCancel();
                }
                hidePop();
            }
        });
        mBusinessListView = (ListView) mContentView.findViewById(R.id.list_view);
        mVerticalStepView = mContentView.findViewById(R.id.v_step_view);
        initListView();
    }

    private void initListView() {
        tabIndex = Config.INDEX_INVALID;
        updateListViewData(0);
        mBusinessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateListViewData(position);
            }
        });

    }

    private BaseBean getSelectBean() {
        final BaseBean pleaseSelect = new BaseBean();
        pleaseSelect.name = "请选择";
        pleaseSelect.id = "";
        return pleaseSelect;
    }

    private void updateListViewData(final int position) {
        switch (tabIndex) {
            case Config.INDEX_TAB_DOOR_TYPE:
                tabIndex = Config.INDEX_TAB_BIG_TYPE;
                mDoorAdapter.updateSelectedPosition(position);
                mTitleText.clear();
                final DoorTypeBean doorTypeBean = mDoorAdapter.getListData().get(mDoorAdapter.getSelectedPosition());

                mTitleText.addFirst(new StepViewBean(doorTypeBean, new IItemClickListener() {
                    @Override
                    public void onItemClisk(BaseBean baseBean) {
                        tabIndex = Config.INDEX_TAB_DOOR_TYPE;
                        if (mDoorAdapter != null) {
                            mBusinessListView.setAdapter(mDoorAdapter);
                            if (mDoorAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                                mBusinessListView.setSelection(mDoorAdapter.getSelectedPosition());
                            }
                        }
                        mVerticalStepView.updateTextViewColor(true, false, false);

                    }
                }));


                mTitleText.addLast(new StepViewBean(getSelectBean(), new IItemClickListener() {
                    @Override
                    public void onItemClisk(BaseBean baseBean) {
                        tabIndex = Config.INDEX_TAB_BIG_TYPE;

                        final ArrayList<BigTypeBean> bigTypeBeans = doorTypeBean.list;
                        mBigAdapter = new BaseListAdapter<>(bigTypeBeans);
                        mBusinessListView.setAdapter(mBigAdapter);

                        mVerticalStepView.updateTextViewColor(false, true, false);
                    }
                }));

                final ArrayList<BigTypeBean> bigTypeBeans = doorTypeBean.list;
                mBigAdapter = new BaseListAdapter<>(bigTypeBeans);
                mBusinessListView.setAdapter(mBigAdapter);

                break;
            case Config.INDEX_TAB_BIG_TYPE:
                tabIndex = Config.INDEX_TAB_CENTRE_TYPE;
                mBigAdapter.updateSelectedPosition(position);

                // 反向遍历，避免数组越界
                final int size = mTitleText.size();
                for (int i = size - 1; i >= 0; i--) {
                    if (i >= 1) {
                        // 只保留一个元素
                        mTitleText.removeLast();
                    }
                }

                final BigTypeBean bigTypeBean = mBigAdapter.getListData().get(mBigAdapter.getSelectedPosition());

                mTitleText.addLast(new StepViewBean(bigTypeBean, new IItemClickListener() {
                    @Override
                    public void onItemClisk(BaseBean baseBean) {
                        tabIndex = Config.INDEX_TAB_BIG_TYPE;

                        if (mBigAdapter != null) {
                            mBusinessListView.setAdapter(mBigAdapter);
                            if (mBigAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                                mBusinessListView.setSelection(mBigAdapter.getSelectedPosition());
                            }
                        }
                        mVerticalStepView.updateTextViewColor(false, true, false);
                    }
                }));


                mTitleText.addLast(new StepViewBean(getSelectBean(), new IItemClickListener() {
                    @Override
                    public void onItemClisk(BaseBean baseBean) {
                        tabIndex = Config.INDEX_TAB_CENTRE_TYPE;

                        final ArrayList<CentreTypeBean> centreTypeBeans = bigTypeBean.list;
                        mCentreAdapter = new BaseListAdapter<>(centreTypeBeans);
                        mBusinessListView.setAdapter(mCentreAdapter);

                        mVerticalStepView.updateTextViewColor(false, false, true);
                    }
                }));


                final ArrayList<CentreTypeBean> centreTypeBeans = bigTypeBean.list;
                mCentreAdapter = new BaseListAdapter<>(centreTypeBeans);
                mBusinessListView.setAdapter(mCentreAdapter);

                break;
            case Config.INDEX_TAB_CENTRE_TYPE:
                mCentreAdapter.updateSelectedPosition(position);
                mTitleText.removeLast();
                mTitleText.addLast(new StepViewBean(mCentreAdapter.getListData().get(position), new IItemClickListener() {
                    @Override
                    public void onItemClisk(BaseBean baseBean) {
                        tabIndex = Config.INDEX_TAB_CENTRE_TYPE;

                        if (mCentreAdapter != null) {
                            mBusinessListView.setAdapter(mCentreAdapter);
                            if (mCentreAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                                mBusinessListView.setSelection(mCentreAdapter.getSelectedPosition());
                            }
                        }
                        mVerticalStepView.updateTextViewColor(false, false, true);
                    }
                }));

                callSelectListener();
                break;
            case Config.INDEX_INVALID:
            default:
                tabIndex = Config.INDEX_TAB_DOOR_TYPE;
                mTitleText.clear();
                final ArrayList<DoorTypeBean> doorTypeBeans = mBusinessBean.list;
                mDoorAdapter = new BaseListAdapter<>(doorTypeBeans);
                mBusinessListView.setAdapter(mDoorAdapter);

                mTitleText.addLast(new StepViewBean(getSelectBean(), null));

                break;
        }
        mVerticalStepView.setStepViewTexts(mTitleText);
    }

    private void callSelectListener() {
        final DoorTypeBean doorTypeBean = mDoorAdapter.getItem(mDoorAdapter.getSelectedPosition());
        final BigTypeBean bigTypeBean = mBigAdapter.getItem(mBigAdapter.getSelectedPosition());
        final CentreTypeBean centreTypeBean = mCentreAdapter.getItem(mCentreAdapter.getSelectedPosition());
        if (mPickerListener != null) {
            mPickerListener.onSelected(doorTypeBean, bigTypeBean, centreTypeBean);
        }
        hidePop();
    }
}
