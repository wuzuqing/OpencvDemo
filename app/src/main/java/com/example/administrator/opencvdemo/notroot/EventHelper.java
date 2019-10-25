package com.example.administrator.opencvdemo.notroot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.administrator.opencvdemo.util.LogUtils;

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
        return new GestureDescription.StrokeDescription(path2, 0, 40);
    }

    private static GestureDescription.StrokeDescription getSwipeSd(int startX, int startY, int endX, int endY) {
        Path path2 = new Path();
        path2.moveTo(startX, startY);
        path2.lineTo(endX, endY);
        return new GestureDescription.StrokeDescription(path2, 0, 550);
    }

    private static GestureDescription getGd(GestureDescription.StrokeDescription sd) {
        return new GestureDescription.Builder().addStroke(sd).build();
    }

    /**
     * 点击
     *
     * @param service 辅助服务
     * @param x x坐标
     * @param y y坐标
     */
    public static void click(AccessibilityService service, int x, int y) {
        if (service != null && isServiceRunning) {
            service.dispatchGesture(getGd(getClickSd(x, y)), null, null);
        }
    }

    /**
     * 点击
     *
     * @param x x坐标
     * @param y y坐标
     */
    public static void click(int x, int y) {
        click(mService, x, y);
    }

    /**
     * 滑动
     *
     * @param service 辅助服务
     * @param startX x开始坐标
     * @param startY y开始坐标
     * @param endX x结束坐标
     * @param endY y结束坐标
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
     * @param startX x开始坐标
     * @param y y坐标
     * @param endX x结束坐标
     */
    public static void swipeHor(AccessibilityService service, int startX, int endX, int y) {
        swipe(service, startX, y, endX, y);
    }
    /**
     * 横向滑动
     *
     * @param startX x开始坐标
     * @param y y坐标
     * @param endX x结束坐标
     */
    public static void swipeHor( int startX, int endX, int y) {
        swipe(mService, startX, y, endX, y);
    }

    /**
     * 纵向滑动
     *
     * @param service 辅助服务
     * @param startY y开始坐标
     * @param x x坐标
     * @param endY y结束坐标
     */
    public static void swipeVer(AccessibilityService service, int startY, int endY, int x) {
        swipe(service, x, startY, x, endY);
    }

    public static void log(String msg) {
        LogUtils.logd(msg);
    }

    private static AccessibilityEvent accessibilityEvent;

    public static void setCurrentEvent(AccessibilityEvent event) {
        accessibilityEvent = event;
        currentPackageName = event.getPackageName().toString();
        LogUtils.logd("setCurrentEvent:" + currentPackageName + ":" + event.getAction());
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
                long start = System.currentTimeMillis() + 1000;
                log("testClick:" + isServiceRunning);
                while (isServiceRunning && System.currentTimeMillis() < start) {
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

    public static void keyBack() {
        if (mService != null) {
            mService.performBackClick();
        }
    }

    public static void keyBack(int key) {
        if (mService != null) {
            //            mService.per(key);
        }
    }


    public static void inputUserInfo(String userInfoName) {
        if (accessibilityEvent == null || mService==null) {
            return;
        }
        if (isGame()) {
            // 获取事件活动的窗口布局根节点
            AccessibilityNodeInfo rootNode = mService.getRootInActiveWindow();
            // 解析根节点
            handle(rootNode, userInfoName);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void handle(AccessibilityNodeInfo info, String userInfoName) {
        // 判断节点是否有子控件
        if (info.getChildCount() == 0) {
            // 判断节点是否有文字并且有“搜索”文字
            if (TextUtils.equals("android.widget.EditText", info.getClassName()) && info.getInputType() == 1) {
                LogUtils.logd("getInputType:" + info.getInputType() + "");
                setText(info, userInfoName);
            }
        } else {
            // 当 当前节点 有子控件时，解析它的孩子，以此递归
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    handle(info.getChild(i), userInfoName);
                }
            }
        }
    }

    private static void setText(AccessibilityNodeInfo info, String txt) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, txt);
        info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    public static boolean isGame() {
        return TextUtils.equals("com.anzhuojwgly.ckhd", currentPackageName);
    }
}
