package com.example.administrator.opencvdemo.notroot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * 作者：士元
 * 时间：2019/10/18 0018 17:18
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
@TargetApi(24)
public class EventHelper {

    private static final String TAG = "EventHelper";
    public static boolean isServiceRunning = false;
    public static String currentPackageName;


    private static GestureDescription.StrokeDescription getClickSd(int x, int y) {
        Path path2 = new Path();
        path2.moveTo(x, y);
        log("getClickSd: " + x + "," + y);
        return new GestureDescription.StrokeDescription(path2, 0, 50);
    }

    private static GestureDescription.StrokeDescription getSwipeSd(int startX, int startY, int endX, int endY) {
        Path path2 = new Path();
        path2.moveTo(startX, startY);
        path2.lineTo(endX, endY);
        return new GestureDescription.StrokeDescription(path2, 100, 50);
    }

    private static GestureDescription getGd(GestureDescription.StrokeDescription sd) {
        return new GestureDescription.Builder().addStroke(sd).build();
    }


    /**
     * 点击
     *
     * @param service 辅助服务
     * @param x       x坐标
     * @param y       y坐标
     */
    public static void click(AccessibilityService service, int x, int y) {
        if (service != null && isServiceRunning) {
            service.dispatchGesture(getGd(getClickSd(x, y)), null, null);
        }
    }

    /**
     * 滑动
     *
     * @param service 辅助服务
     * @param startX  x开始坐标
     * @param startY  y开始坐标
     * @param endX    x结束坐标
     * @param endY    y结束坐标
     */
    public static void swipe(AccessibilityService service, int startX, int startY, int endX, int endY) {
        GestureDescription.StrokeDescription swipeSd = getSwipeSd(startX, startY, endX, endY);
        if (service != null && isServiceRunning) {
            service.dispatchGesture(getGd(swipeSd), null, null);
        }
    }

    /**
     * 横向滑动
     *
     * @param service 辅助服务
     * @param startX  x开始坐标
     * @param y       y坐标
     * @param endX    x结束坐标
     */
    public static void swipeHor(AccessibilityService service, int startX, int endX, int y) {
        swipe(service, startX, y, endX, y);
    }

    /**
     * 纵向滑动
     *
     * @param service 辅助服务
     * @param startY  y开始坐标
     * @param x       x坐标
     * @param endY    y结束坐标
     */
    public static void swipeVer(AccessibilityService service, int startY, int endY, int x) {
        swipe(service, x, startY, x, endY);
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void setCurrentEvent(AccessibilityEvent event) {
        currentPackageName = event.getPackageName().toString();
    }

    private static TaskAccessibilityService mService;

    public static void setServiceRunning(TaskAccessibilityService service, boolean b) {
        EventHelper.isServiceRunning = b;
        EventHelper.mService = service;
        if (isServiceRunning) {

        }
    }

    public static void testClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis()+1000;
                log("testClick:"+isServiceRunning);
                while (isServiceRunning && System.currentTimeMillis()<start) {
                    click(mService, 600, 600);
                    sleep(20);
                }
            }
        }).start();
    }


    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
