package com.xwang.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by xwangly on 2017/2/9.
 */

public class Tips {
    private static Toast sToast;

    private Tips() {
    }

    @SuppressLint({"ShowToast"})
    public static void init(Context context) {
        if(sToast == null) {
            sToast = Toast.makeText(context, "", LENGTH_SHORT);
        }

    }

    public static void showToast(@StringRes int resId) {
        show(resId, LENGTH_SHORT);
    }

    public static void showToast(Object object) {
        show(object, LENGTH_SHORT);
    }

    public static void showToastLong(@StringRes int resId) {
        show(resId, LENGTH_LONG);
    }

    public static void showToastLong(Object object) {
        show(object, LENGTH_LONG);
    }

    private static void show(@StringRes int resId, int length) {
        check();
        if(resId > 0) {
            sToast.setText(resId);
            sToast.setDuration(length);
            sToast.show();
        }

    }

    private static void show(Object object, int length) {
        check();
        if(object != null) {
            sToast.setText(object.toString());
            sToast.setDuration(length);
            sToast.show();
        }

    }

    private static void check() {
        if(sToast == null) {
            throw new IllegalStateException("you must call T.init(context) first");
        }
    }

    public static void showSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static Snackbar makeSnackbar(View view, String msg) {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar makeSnackbar(View view, @StringRes int resId) {
        return Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
    }
}
