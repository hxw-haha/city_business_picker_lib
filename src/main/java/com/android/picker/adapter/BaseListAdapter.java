package com.android.picker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.picker.R;
import com.android.picker.bean.BaseBean;
import com.android.picker.config.Config;

import java.util.ArrayList;

/**
 * @author hanxw
 * @time 2019/7/4 15:35
 */
public class BaseListAdapter<T extends BaseBean> extends BaseAdapter {

    private ArrayList<T> mListData;

    private int selectIndex = Config.INDEX_INVALID;

    public BaseListAdapter(ArrayList<T> list) {
        this.mListData = list;
    }

    public ArrayList<T> getListData() {
        return mListData;
    }

    public void updateSelectedPosition(int index) {
        this.selectIndex = index;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return this.selectIndex;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public T getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_picker_item, parent, false);
            holder = new HolderView();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView) convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        BaseBean item = getItem(position);
        holder.name.setText(item.getName());

        boolean checked = selectIndex != Config.INDEX_INVALID && selectIndex == position;
        holder.name.setEnabled(!checked);
        holder.selectImg.setVisibility(checked ? View.VISIBLE : View.GONE);

        return convertView;
    }

    static class HolderView {
        TextView name;
        ImageView selectImg;
    }
}
