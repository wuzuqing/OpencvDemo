package com.example.administrator.opencvdemo;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.example.administrator.opencvdemo.notroot.ServiceHelper;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.module_orc.OrcHelper;

public class BaseApplication extends Application {
    private static boolean isShowPanel;
    public static boolean isShowPanel() {
        return isShowPanel;
    }
    private static Context mContext;
    public static void setIsShowPanel(boolean isShowPanel) {
        BaseApplication.isShowPanel = isShowPanel;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }
    public static int getScreenHeight() {
        return screenHeight;
    }

    private static int screenWidth,screenHeight;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SPUtils.init(this);
        OrcHelper.getInstance().init(this);
        ServiceHelper.getInstance().init(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
}
