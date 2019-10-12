package com.example.administrator.opencvdemo.floatservice;

import java.util.List;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;

public class MainService extends Service {

    private static final String TAG = "MainService";

    private View toucherLayout;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;

    private ScreenBroadcastReceiver mScreenBroadcastReceiver;
    //状态栏高度.
    private int statusBarHeight = -1;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerUtil.send("启动服务");
        createTouchView();
        Log.d(TAG, "onCreate: ");
        BaseApplication.setIsShowPanel(true);
        startScreenBroadcastReceiver();
        OpenCVHelper.init(this);
    }

    //    private boolean isWPZMGServiceRunning;
    private int halfViewSize;
    private TextView tvShowOrHide, tvLimitTime;
    View llPanel;
    private boolean isTy;

    private void createTouchView() {
        //赋值WindowManager&LayoutParam.
        halfViewSize = 120 / 2;
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;//可以监听MotionEvent的ACTION_OUTSIDE事件
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版限制--即允许在可见的屏幕之外

        params.alpha = 1.0f;
        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = -2;
        params.height = -2;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = inflater.inflate(R.layout.toucherlayout, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        llPanel = toucherLayout.findViewById(R.id.ll_panel);
        tvShowOrHide = (TextView) toucherLayout.findViewById(R.id.tv_show_or_hide);
        tvLimitTime = (TextView) toucherLayout.findViewById(R.id.tv_limit_time);

        if (isTy) {
            tvLimitTime.setVisibility(View.VISIBLE);
            HandlerUtil.startDjs(tvLimitTime);
        } else {
            tvLimitTime.setVisibility(View.GONE);
        }
        tvShowOrHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llPanel.isShown()) {
                    llPanel.setVisibility(View.GONE);
                } else {
                    llPanel.setVisibility(View.VISIBLE);
                }
                ((TextView) v).setText(llPanel.isShown() ? "隐藏" : "显示");
            }
        });

        toucherLayout.findViewById(R.id.tvSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(MainService.this, DialogActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
                // llPanel.setVisibility(View.GONE);
                // tvShowOrHide.setText("显示");
            }
        });
        toucherLayout.findViewById(R.id.tvStartSome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xiaoHao(true);
                // ((TextView) v).setText(Util.isWPZMGServiceRunning ? "取消挂机" : "开始挂机");
                // llPanel.setVisibility(View.GONE);
                // tvShowOrHide.setText("显示");
            }
        });
        toucherLayout.findViewById(R.id.tvOneTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (!TaskUtil.isDestory) {
                //     TaskUtil.isDestory = true;
                //     return;
                // }
                // Intent intent = new Intent(MainService.this, WPZMGService2.class);
                // intent.putExtra("oneTask", true);
                // startService(intent);
                // llPanel.setVisibility(View.GONE);
                // tvShowOrHide.setText("显示");
            }
        });
        toucherLayout.findViewById(R.id.tvStartOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Util.isWPZMGServiceRunning = !Util.isWPZMGServiceRunning;
                //                Intent intent = new Intent(MainService.this, WPZMGService.class);
                //                if (!Util.isWPZMGServiceRunning) {
                //                    intent.putExtra("stop", true);
                //                    Util.setResLastTime(0);
                //                }
                //                llPanel.setVisibility(View.GONE);
                //                tvShowOrHide.setText("显示");
                //                startService(intent);
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        ScreenCapture.startCaptureSync();
                        List<OrcModel> result = OrcHelper.getInstance().executeCallSync(ScreenCapture.get().getCurrentBitmap());
                        if (result.size()>=2){
                            AutoTool.execShellCmd(AutoTool.clickFloat(result.get(1).getRect()));
                        }
                        Log.d(TAG, "call: " + result.toString());
                    }
                });
            }
        });
        toucherLayout.findViewById(R.id.tvLaunch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchApp.launchapp(getApplication());
                // String packageName = SPUtils.getString("packageName");
                // if (TextUtils.isEmpty(packageName)) {
                //     packageName = LaunchApp.JPZMG_PACKAGE_NAME;
                // }
                // LoadingActivity.launch(MainService.this, packageName, 0);
            }
        });
        toucherLayout.findViewById(R.id.tvUpgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TaskUtil.upgrade();
                //                AlarmService.set(MainService.this,60);
                //                stopSelf();
                //                HandlerUtil.async(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        String string = JsonUtils.getJsonFromMusic("click.json");
                //                        String[] split = string.split(",");
                //                        AutoTool.execShellCmdXy(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                //                    }
                //                });
            }
        });
        toucherLayout.findViewById(R.id.tvTestSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(MainService.this, AssetsPointSettingActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
            }
        });

        toucherLayout.findViewById(R.id.tvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (isTy()) return;
                // HandlerUtil.async(new Runnable() {
                //     @Override
                //     public void run() {
                //         if (!TaskUtil.isDestory) {
                //             TaskUtil.isDestory = true;
                //         } else {
                //             TaskUtil.sixTaskNewV2(null, null);
                //         }
                //     }
                // });
            }
        });
        toucherLayout.findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                xiaoHao(false);
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                //                llPanel.setVisibility(View.GONE);
                //                tvShowOrHide.setText("显示");
            }
        });
        toucherLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) event.getRawX() - halfViewSize;
                        params.y = (int) event.getRawY() - halfViewSize - statusBarHeight;
                        windowManager.updateViewLayout(toucherLayout, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("action")) {
            String action = intent.getStringExtra("action");
            if ("ACTION_BOOT_COMPLETED".equals(action) || "com.g.android.RING".equals(action)) {
                llPanel.setVisibility(View.GONE);
                tvShowOrHide.setText("显示");
                // xiaoHao(true);
            }
        }
        return START_STICKY;
    }
    //
    // private void xiaoHao(boolean some) {
    //     Util.isWPZMGServiceRunning = !Util.isWPZMGServiceRunning;
    //     Intent intent2 = new Intent(MainService.this, WPZMGService2.class);
    //     if (!Util.isWPZMGServiceRunning) {
    //         intent2.putExtra("stop", true);
    //         Util.setResLastTime(0);
    //     }
    //     startService(intent2);
    // }

    @Override
    public void onDestroy() {
        if (tvShowOrHide != null) {
            windowManager.removeView(toucherLayout);
        }
        Log.d(TAG, "onDestroy: ");
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
