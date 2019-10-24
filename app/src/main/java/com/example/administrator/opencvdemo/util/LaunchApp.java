package com.example.administrator.opencvdemo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import com.example.administrator.opencvdemo.BaseApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/28 10:22
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/28$
 * @updateDes ${TODO}
 */

public class LaunchApp {
    //首先我们必须要知道要跳转的app的包名，每一个APP的包名都是独立的，纵使是马甲包和主包的包名也是不一样的。
    //我们将要跳转的包名填在以下位置。

    public static final String WEIXIN_PACKAGE_NAME = "com.tencent.mm";
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String WPYX_PACKAGE_NAME = "com.psytap.wpyx";
    public static final String JPZMG_PACKAGE_NAME = "com.anzhuojwgly.ckhd";
    public static final String SELF_APP = "com.itant.autoclick";

    public static String APP_PACKAGE_NAME = JPZMG_PACKAGE_NAME;
    public static void launchApp(){
        launchapp(BaseApplication.getAppContext(), LaunchApp.JPZMG_PACKAGE_NAME);    //启动游戏
    }
    //跳转页面的方法
    public static void launchapp(Context context) {
        launchapp(context, APP_PACKAGE_NAME);
    }

    //跳转页面的方法
    public static void launchapp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            LogUtils.logd("activities:" + activities.toString() + " activityThread:" + activityThread + " activitiesField:" + activitiesField);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String TAG = "LaunchApp";

    public static Activity reflectActivity() {

        try {
            String actName = topAct(BaseApplication.getAppContext());// com.example.calledjar.MainActivity
            Class clz = BaseApplication.getAppContext().getClass().forName(
                "android.app.ActivityThread");
            Method meth = clz.getMethod("currentActivityThread");
            Object currentActivityThread = meth.invoke(null);
            Field f = clz.getDeclaredField("mActivities");
            f.setAccessible(true);
            //            Map obj = (Map) f.get(currentActivityThread);

            Map activities = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // 4.4 以下使用的是 HashMap
                activities = (HashMap) f.get(currentActivityThread);
            } else { // 4.4 以上使用的是 ArrayMap
                activities = (ArrayMap) f.get(currentActivityThread);
            }

            Log.e(TAG, "______reflectActivity____" + actName + activities.toString());
            for (Object key : activities.keySet()) {
                Object activityRecord = activities.get(key);
                Field actField = activityRecord.getClass().getDeclaredField(
                    "activity");
                actField.setAccessible(true);
                Object activity = actField.get(activityRecord);
                System.out.println(activity);
                Activity act1 = (Activity) activity;

                String act1N = act1.getClass().toString();// class
                // com.example.calledjar.MainActivity
                String act1Name = act1N.substring(6);// class
                // com.example.calledjar.MainActivity

                if (actName.equals(act1Name)) {
                    //                    Log.e(tag, "______reflectActivity____" + act.toString());
                    return act1;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static String topAct(Context application2) {

        ActivityManager am = (ActivityManager) application2
            .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(30).get(0).topActivity;

        return cn.getClassName();
    }

    public static void killApp() {

    }
}
