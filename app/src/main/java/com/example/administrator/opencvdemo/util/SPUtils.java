package com.example.administrator.opencvdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.administrator.opencvdemo.BaseApplication;

/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 */
public class SPUtils {

    private static SPUtils spUtils;
    private static SharedPreferences sp;
    private String SP_NAME = "com.psytap.wpyx_preference";

    private SPUtils(Context context) {
        savePreToSDcard(context);
    }

    private void savePreToSDcard(Context context) {
        try {
            sp = context.getSharedPreferences(
                    SP_NAME, Activity.MODE_PRIVATE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init(Context context) {
        if (spUtils == null) {
            synchronized (SPUtils.class) {
                if (spUtils == null) spUtils = new SPUtils(context);
            }
        }
    }

    public static String getString(String key) {
        return sp.getString(key, "");
    }


    public static void setInt(String key, int value) {
        if (sp == null) {
            init(BaseApplication.getAppContext());
        }
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }


    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }


    public static void setLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return sp.getLong(key, 0L);
    }

    public static long getLong(String key, Long defValue) {
        return sp.getLong(key, defValue);
    }


    public static void setFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return sp.getFloat(key, 0F);
    }
    public static float getFloat(String key,float defValue) {
        return sp.getFloat(key, defValue);
    }

    public static void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }
    public static boolean getBoolean(String key, boolean value) {
        return sp.getBoolean(key, value);
    }


    public static void setString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public static void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

}