package com.xwang.common.adapter;

import android.content.Context;

import java.util.List;

/**
 * 
 * @param <T>
 */
public abstract class CommonBaseAdapter<T> extends BaseListAdapter {
    protected List<T> mDatas;

    public CommonBaseAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        super(context, itemLayoutId);
        this.mDatas = mDatas;
    }
    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void appendDatas(List<T> mDatas) {
        if (mDatas != null) {
            if (this.mDatas != null) {
                this.mDatas.addAll(mDatas);
            } else  {
                this.mDatas = mDatas;
            }
        }
    }
    
    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    
    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    protected void convert(int position, CommonViewHolder helper) {
        T item = getItem(position);
        convert(position, helper, item);
    }

    protected abstract void convert(int position, CommonViewHolder helper, T item);
    

    
}
