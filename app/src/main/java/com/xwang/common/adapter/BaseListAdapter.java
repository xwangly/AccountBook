package com.xwang.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected final int mItemLayoutId;

    public BaseListAdapter(Context context, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonViewHolder viewHolder = getViewHolder(position,
                convertView, parent);
        convert(position, viewHolder);
        return viewHolder.getConvertView();
    }
    
    protected abstract void convert(int position, CommonViewHolder helper);
    
    private CommonViewHolder getViewHolder(int position, View convertView,
            ViewGroup parent) {
        return CommonViewHolder.get(mContext, convertView, parent,
                mItemLayoutId, position);
    }
    
}
