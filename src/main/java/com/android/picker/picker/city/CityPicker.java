package com.android.picker.picker.city;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.picker.R;
import com.android.picker.adapter.BaseListAdapter;
import com.android.picker.bean.BaseBean;
import com.android.picker.bean.city.CityBean;
import com.android.picker.bean.city.DistrictBean;
import com.android.picker.bean.city.LocationBean;
import com.android.picker.bean.city.ProvinceBean;
import com.android.picker.config.Config;
import com.android.picker.picker.BasePicker;
import com.android.picker.picker.IPickerListener;
import com.android.picker.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author hanxw
 * @time 2019/7/4 13:59
 */
public class CityPicker extends BasePicker implements View.OnClickListener {

    private static LocationBean mLocationBean;
    private static CityPicker cityPicker;

    private View mContentView;
    private ListView mCityListView;
    private AppCompatTextView mProTv;
    private AppCompatTextView mCityTv;
    private AppCompatTextView mDistrictTv;
    private View mSelectedLine;

    private int tabIndex = Config.INDEX_INVALID;
    private BaseListAdapter<ProvinceBean> mProvibceAdapter;
    private BaseListAdapter<CityBean> mCityAdapter;
    private BaseListAdapter<DistrictBean> mDistrictAdapter;

    private CityPicker() {

    }

    public static synchronized CityPicker getInstance() {
        if (cityPicker == null) {
            cityPicker = new CityPicker();
        }
        return cityPicker;
    }

    private LocationBean initLocationData(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return new Gson().fromJson(Utils.getAssetJson(context, Config.CITY_DATA), LocationBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示弹出框
     *
     * @param context
     * @param listener 选择信息回调
     */
    public void showCityPicker(Context context, IPickerListener listener) {
        initCityPickerPop(context);
        this.mPickerListener = listener;
    }

    private void initCityPickerPop(final Context context) {
        if (mLocationBean == null) {
            mLocationBean = initLocationData(context);
            if (mLocationBean == null) {
                return;
            }
        }

        initContentView(context);
        initPopWindowView(context, mContentView, mCityListView);
    }


    private void initContentView(final Context context) {
        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_city_picker, null);
        mCityListView = (ListView) mContentView.findViewById(R.id.list_view);
        mProTv = (AppCompatTextView) mContentView.findViewById(R.id.province_tv);
        mCityTv = (AppCompatTextView) mContentView.findViewById(R.id.city_tv);
        mDistrictTv = (AppCompatTextView) mContentView.findViewById(R.id.district_tv);
        mContentView.findViewById(R.id.close_img).setOnClickListener(this);
        mSelectedLine = (View) mContentView.findViewById(R.id.selected_line);

        mProTv.setOnClickListener(this);
        mCityTv.setOnClickListener(this);
        mDistrictTv.setOnClickListener(this);

        initCityListView();
    }

    private void initCityListView() {
        tabIndex = Config.INDEX_INVALID;
        updateListViewData(0);
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateListViewData(position);
            }
        });
    }


    /**
     * 更新list view 加载的数据
     *
     * @param position
     */
    private void updateListViewData(int position) {
        switch (tabIndex) {
            case Config.INDEX_TAB_PROVINCE:
                tabIndex = Config.INDEX_TAB_CITY;
                mProvibceAdapter.updateSelectedPosition(position);
                final ProvinceBean provinceCity = mProvibceAdapter.getItem(mProvibceAdapter.getSelectedPosition());
                mProTv.setText(provinceCity.name);
                unSelectTextColor(mProTv);
                mCityTv.setText(R.string.select_string);
                selectTextColor(mCityTv);
                mDistrictTv.setVisibility(View.GONE);
                tabSelectedIndicatorAnimation(mCityTv);

                final ArrayList<CityBean> cityBeans = mProvibceAdapter.getListData().get(position).cityList;
                if (cityBeans == null || cityBeans.size() <= 0) {
                    callSelectListener(false, false);
                    break;
                }
                mCityAdapter = new BaseListAdapter<>(cityBeans);
                mCityListView.setAdapter(mCityAdapter);
                break;
            case Config.INDEX_TAB_CITY:
                tabIndex = Config.INDEX_TAB_DISTRICT;
                mCityAdapter.updateSelectedPosition(position);
                final CityBean cityCity = mCityAdapter.getItem(mCityAdapter.getSelectedPosition());
                mCityTv.setText(cityCity.name);
                unSelectTextColor(mCityTv);
                mDistrictTv.setVisibility(View.VISIBLE);
                mDistrictTv.setText(R.string.select_string);
                selectTextColor(mDistrictTv);
                tabSelectedIndicatorAnimation(mDistrictTv);

                final ArrayList<DistrictBean> districtBeans = mCityAdapter.getListData().get(position).cityList;
                if (districtBeans == null || districtBeans.size() <= 0) {
                    callSelectListener(true, false);
                    break;
                }
                mDistrictAdapter = new BaseListAdapter<>(districtBeans);
                mCityListView.setAdapter(mDistrictAdapter);
                break;
            case Config.INDEX_TAB_DISTRICT:
                mDistrictAdapter.updateSelectedPosition(position);
                final DistrictBean districtCity = mDistrictAdapter.getItem(mDistrictAdapter.getSelectedPosition());
                mDistrictTv.setText(districtCity.name);
                tabSelectedIndicatorAnimation(mDistrictTv);

                callSelectListener(true, true);
                break;
            case Config.INDEX_INVALID:
            default:
                tabSelectedIndicatorAnimation(mProTv);
                tabIndex = Config.INDEX_TAB_PROVINCE;
                selectTextColor(mProTv);
                mProvibceAdapter = new BaseListAdapter<>(mLocationBean.cityList);
                mCityListView.setAdapter(mProvibceAdapter);
                break;
        }
    }

    /**
     * 回调选择位置的数据
     *
     * @param isCityFlag     是否回调 CityBean 数据
     * @param isDistrictFlag 是否回调 DistrictBean 数据
     */
    private void callSelectListener(boolean isCityFlag, boolean isDistrictFlag) {
        final BaseBean provinceBean = mProvibceAdapter.getItem(mProvibceAdapter.getSelectedPosition());
        final BaseBean cityBean = isCityFlag ? mCityAdapter.getItem(mCityAdapter.getSelectedPosition()) : null;
        final BaseBean districtBean = isDistrictFlag ? mDistrictAdapter.getItem(mDistrictAdapter.getSelectedPosition()) : null;

        if (mPickerListener != null) {
            mPickerListener.onSelected(provinceBean, cityBean, districtBean);
        }
        hidePop();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.province_tv) {
            tabSelectedIndicatorAnimation(mProTv);
            tabIndex = Config.INDEX_TAB_PROVINCE;
            if (mProvibceAdapter != null) {
                mCityListView.setAdapter(mProvibceAdapter);
                if (mProvibceAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                    mCityListView.setSelection(mProvibceAdapter.getSelectedPosition());
                }
            }
        } else if (v.getId() == R.id.city_tv) {
            tabSelectedIndicatorAnimation(mCityTv);
            tabIndex = Config.INDEX_TAB_CITY;
            if (mCityAdapter != null) {
                mCityListView.setAdapter(mCityAdapter);
                if (mCityAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                    mCityListView.setSelection(mCityAdapter.getSelectedPosition());
                }
            }
        } else if (v.getId() == R.id.district_tv) {
            tabSelectedIndicatorAnimation(mDistrictTv);
            tabIndex = Config.INDEX_TAB_DISTRICT;
            if (mDistrictAdapter != null) {
                mCityListView.setAdapter(mDistrictAdapter);
                if (mDistrictAdapter.getSelectedPosition() != Config.INDEX_INVALID) {
                    mCityListView.setSelection(mDistrictAdapter.getSelectedPosition());
                }
            }
        } else if (v.getId() == R.id.close_img) {
            hidePop();
            Utils.setBackgroundAlpha(v.getContext(), 1.0f);
            if (mPickerListener != null) {
                mPickerListener.onCancel();
            }
        }
    }


    /**
     * tab 选中的红色下划线动画
     *
     * @param tab
     * @return
     */
    private void tabSelectedIndicatorAnimation(final TextView tab) {
        tab.post(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mSelectedLine, "X", mSelectedLine.getX(), tab.getX());

                final ViewGroup.LayoutParams params = mSelectedLine.getLayoutParams();
                final ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        params.width = (int) animation.getAnimatedValue();
                        mSelectedLine.setLayoutParams(params);
                    }
                });

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setInterpolator(new FastOutSlowInInterpolator());
                animatorSet.playTogether(xAnimator, widthAnimator);

                animatorSet.start();
            }
        });

    }
}
