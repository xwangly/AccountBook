package com.xwang.accountbook.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.xwang.accountbook.R;
import com.xwang.common.activity.BaseActivity;

public abstract class ToolBarActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.activity_container);
    }

    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(getLayoutInflater().inflate(layoutResID, container, false));
    }

    @Override
    public void setContentView(View view) {
        container.addView(view, 0);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}

