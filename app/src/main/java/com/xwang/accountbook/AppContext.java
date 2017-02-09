package com.xwang.accountbook;

import android.app.Application;

import com.xwang.common.Tips;

/**
 * Created by xwangly on 2017/2/7.
 */

public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Tips.init(this);
    }
}
