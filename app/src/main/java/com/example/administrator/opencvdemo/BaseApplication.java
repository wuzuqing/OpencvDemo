package com.example.administrator.opencvdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.administrator.opencvdemo.event.InputEventManager;
import com.example.administrator.opencvdemo.event.RootInputEventImpl;
import com.example.administrator.opencvdemo.event.SimpleV24InputEventImpl;
import com.example.administrator.opencvdemo.notroot.ServiceHelper;
import com.example.administrator.opencvdemo.util.AccountManager;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.ChengJiuArray;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.StaticVal;
import com.example.module_orc.OrcHelper;

public class BaseApplication extends Application {
    public static int densityDpi = 480;
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

    private static int screenWidth, screenHeight;
    private static final int DEFAULT_HEIGHT = 1920;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SPUtils.init(this);
        OrcHelper.getInstance().init(this);
        ServiceHelper.getInstance().init(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        screenWidth = metrics.widthPixels;
        screenHeight = getScreenHeight3(this);

        if (AutoTool.isRoot() || AutoTool.isEmulator(this)) {
            InputEventManager.getInstance().setClickEvent(new RootInputEventImpl());
        } else if (Build.VERSION.SDK_INT >= 24) {
            InputEventManager.getInstance().setClickEvent(new SimpleV24InputEventImpl());
        }

        LogUtils.logd("densityDpi:" + metrics.densityDpi + " screenHeight:" + screenHeight);
        PointManagerV2.init();
        Util.init();
        AccountManager.init();
        StaticVal.init();
        ChengJiuArray.init();
    }

    /**
     * 包含虚拟导航栏高度
     */
    public static int getScreenWidth3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        defaultDisplay.getRealSize(outPoint);
        return outPoint.x;
    }

    public static int getScreenHeight3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        defaultDisplay.getRealSize(outPoint);
        return outPoint.y;
    }

    public static float getRatioY(float value) {
        return getScreenHeight() * value;
    }
}
