package com.xwang.common.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xwang.common.SystemBarTintManager;

/**
 * Created by xwangly on 2017/2/8.
 */


public abstract class BaseActivity extends AppCompatActivity {
    protected SystemBarTintManager mTintManager;

    public BaseActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        this.mTintManager = new SystemBarTintManager(this);
        this.mTintManager.setStatusBarTintEnabled(true);
        this.mTintManager.setStatusBarTintColor(Color.TRANSPARENT);
        this.mTintManager.setNavigationBarTintColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            baseView.setFitsSystemWindows(false);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                setTranslucentStatus(true);
            }

            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
    }

    protected void setTitle(String title) {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setTitle(title);
        }
    }

    public void setTitle(@StringRes int resId) {
        this.setTitle(this.getString(resId));
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window w = this.getWindow();
        WindowManager.LayoutParams winParams = w.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        w.setAttributes(winParams);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
