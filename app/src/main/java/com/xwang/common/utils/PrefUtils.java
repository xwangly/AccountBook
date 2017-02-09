package com.xwang.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by xwangly on 2017/2/9.
 */

public class PrefUtils {
    private static final String PREF_NAMESPACE = "io.xwang.zhuanlan.PREFS";
    public static final String KEY_HAS_SHORT_CUT = "has_short_cut";
    public static final String KEY_FIRST_ENTER = "first_enter";
    private static SharedPreferences SP;

    public PrefUtils() {
    }

    public static SharedPreferences getPreferences(Context context) {
        if(SP == null) {
            SP = context.getSharedPreferences("io.bxbxbai.zhuanlan.PREFS", 0);
        }

        return SP;
    }

    public static void setValue(@NonNull Context context, @NonNull String key, @NonNull Object value) {
        String type = value.getClass().getSimpleName();
        SharedPreferences.Editor editor = getPreferences(context).edit();
        if(String.class.getSimpleName().equals(type)) {
            editor.putString(key, (String)value);
        } else if(Integer.class.getSimpleName().equals(type)) {
            editor.putInt(key, ((Integer)value).intValue());
        } else if(Boolean.class.getSimpleName().equals(type)) {
            editor.putBoolean(key, ((Boolean)value).booleanValue());
        } else if(Float.class.getSimpleName().equals(type)) {
            editor.putFloat(key, ((Float)value).floatValue());
        } else if(Long.class.getSimpleName().equals(type)) {
            editor.putLong(key, ((Long)value).longValue());
        }

        editor.apply();
    }

    public static Object getValue(Context context, String key, Object defaultValue) {
        String type = defaultValue.getClass().getSimpleName();
        getPreferences(context);
        return String.class.getSimpleName().equals(type)?SP.getString(key, (String)defaultValue):(Integer.class.getSimpleName().equals(type)?Integer.valueOf(SP.getInt(key, ((Integer)defaultValue).intValue())):(Boolean.class.getSimpleName().equals(type)?Boolean.valueOf(SP.getBoolean(key, ((Boolean)defaultValue).booleanValue())):(Float.class.getSimpleName().equals(type)?Float.valueOf(SP.getFloat(key, ((Float)defaultValue).floatValue())):(Long.class.getSimpleName().equals(type)?Long.valueOf(SP.getLong(key, ((Long)defaultValue).longValue())):defaultValue))));
    }

    public static void setHasShortCut(Context context, Boolean has) {
        setValue(context, "has_short_cut", has);
    }

    public static boolean hasShortCut(Context context) {
        return ((Boolean)getValue(context, "has_short_cut", Boolean.valueOf(false))).booleanValue();
    }

    public static void createShortCut(@NonNull Context context) {
    }
}
