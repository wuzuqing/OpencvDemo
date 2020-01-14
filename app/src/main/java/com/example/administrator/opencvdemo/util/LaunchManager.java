package com.example.administrator.opencvdemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.event.InputEventManager;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.module_orc.OrcConfig;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 11:05
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class LaunchManager {

    public static void killApp() {
        InputEventManager.getInstance().killApp();
    }

    public static final String WEIXIN_PACKAGE_NAME = "com.tencent.mm";
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String WPYX_PACKAGE_NAME = "com.psytap.wpyx";
    public static final String JPZMG_PACKAGE_NAME = "com.anzhuojwgly.ckhd";

    public static String APP_PACKAGE_NAME = JPZMG_PACKAGE_NAME;

    public static void launchApp(){
        launchapp(BaseApplication.getAppContext(), JPZMG_PACKAGE_NAME);    //启动游戏
    }
    //跳转页面的方法
    public static void launchapp(Context context) {
        launchapp(context, APP_PACKAGE_NAME);
    }

    //跳转页面的方法
    public static void launchapp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent==null){
            ToastUitl.showShort("未安装该应用");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        // LoadingActivity.launch(context, packageName1, 0);
    }

    //这里是进入应用商店，下载指定APP的方法。
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //这里是判断APP中是否有相应APP的方法
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
