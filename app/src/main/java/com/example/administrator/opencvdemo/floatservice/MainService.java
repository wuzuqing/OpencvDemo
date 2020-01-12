package com.example.administrator.opencvdemo.floatservice;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.module_orc.OpenCVHelper;

public class MainService extends Service {

    private static final String TAG = "MainService";

    private ScreenBroadcastReceiver mScreenBroadcastReceiver;
    //状态栏高度.
    private GameFloatView gameFloatView;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createTouchView();
        BaseApplication.setIsShowPanel(true);
        startScreenBroadcastReceiver();
        OpenCVHelper.init(this);
    }

    private void createTouchView() {
        gameFloatView = new GameFloatView(this);
        gameFloatView.showFloatView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("action")) {
            String action = intent.getStringExtra("action");
            if ("ACTION_BOOT_COMPLETED".equals(action) || "com.g.android.RING".equals(action)) {
                gameFloatView.hidePanel();
                // xiaoHao(true);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (gameFloatView != null) {
            gameFloatView.hideFloatView();
        }
        if (mScreenBroadcastReceiver != null) {
            unregisterReceiver(mScreenBroadcastReceiver);
            mScreenBroadcastReceiver = null;
        }
        super.onDestroy();
        BaseApplication.setIsShowPanel(false);
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, MainService.class);
        context.stopService(intent);
        context.startService(intent);
        context.moveTaskToBack(true);
    }

    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenBroadcastReceiver, filter);
    }
}
