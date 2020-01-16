package com.example.administrator.opencvdemo.floatservice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.activity.AccountManagerActivity;
import com.example.administrator.opencvdemo.activity.AssetsPointSettingActivity;
import com.example.administrator.opencvdemo.activity.DialogActivity;
import com.example.administrator.opencvdemo.event.InputEventManager;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.notroot.ServiceHelper;
import com.example.administrator.opencvdemo.notroot.WPZMGService3;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LaunchManager;
import com.example.administrator.opencvdemo.util.MenKeFenShuHelper;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.administrator.opencvdemo.v2.task.ChongBangTaskElement;
import com.example.administrator.opencvdemo.v2.task.KuaFuJiangLiTaskElement;
import com.example.administrator.opencvdemo.v2.task.MenKeLeiTaiTaskElement;
import com.example.administrator.opencvdemo.v2.task.XiaoBangTaskElement;
import com.example.administrator.opencvdemo.v2.task.YamenTaskElement;

/**
 * 作者：士元
 * 时间：2019/10/23 0023 10:14
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class GameFloatView extends BaseFloatView {

    private TextView tvShowOrHide;
    View llPanel;

    public GameFloatView(@NonNull Context context) {
        super(context);
    }

    public GameFloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.toucherlayout;
    }

    private static final String TAG = "GameFloatView";

    @Override
    protected void bindView() {
        Log.d(TAG, "bindView: ");
        llPanel = findViewById(R.id.ll_panel);
        tvShowOrHide = (TextView) findViewById(R.id.tv_show_or_hide);
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

        findViewById(R.id.tvSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DialogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                hidePanel1();
            }
        });
        findViewById(R.id.tvStartSome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ServiceHelper.getInstance().goAccess()) {
                    xiaoHao();
                    ((TextView) v).setText(Util.isWPZMGServiceRunning ? "取消挂机" : "开始挂机");
                    hidePanel1();
                }
            }
        });
        findViewById(R.id.tvOneTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountManagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                hidePanel1();
            }
        });
        findViewById(R.id.tvStartOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel1();
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {

                        // Test.test(bitmap);
                    }
                });
            }
        });
        findViewById(R.id.tvLaunch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchManager.launchapp(getContext());
            }
        });
        findViewById(R.id.tvUpgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hidePanel1();
                Util.gc();
            }
        });
        findViewById(R.id.tvTestSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AssetsPointSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        findViewById(R.id.tvTestDebug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调试专用
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (SPUtils.getBoolean(Constant.KEY_OPEN_SPEED)) {
                                InputEventManager.getInstance().swipe(800, 600, 350, 600);
                                Util.sleep(800);
                                InputEventManager.getInstance().swipe(800, 600, 350, 600);
                            } else {
                                InputEventManager.getInstance().swipe(800, 600, 350, 600);
                                Util.sleep(2000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // long start = SystemClock.elapsedRealtime();
                        // while (true){
                        //     try {
                        //         Util.getBitmapAndPageData();
                        //         Util.sleep(1000);
                        //         long now = SystemClock.elapsedRealtime();
                        //         LogUtils.logd("used:"+(now-start));
                        //         start = now;
                        //     } catch (InterruptedException e) {
                        //         e.printStackTrace();
                        //     }
                        // }

                        // try {
                        //     PointManagerV2.execShellCmdClose();
                        //     // MenKeFenShuHelper.getInstance().getPkModel();
                        // } catch (Exception e) {
                        //     e.printStackTrace();
                        // }
                    }
                });
            }
        });
        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 主号
                if (!ServiceHelper.getInstance().goAccess()) {
                    hidePanel1();
                    TaskState.isWorking = true;
                    // MenKeLeiTaiTaskElement
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(new YamenTaskElement(new TaskModel("邮件", true)));
                }
            }
        });
    }

    private void hidePanel1() {
        llPanel.setVisibility(View.GONE);
        tvShowOrHide.setText("显示");
    }

    private void xiaoHao() {
        Util.isWPZMGServiceRunning = !Util.isWPZMGServiceRunning;
        Intent intent2 = new Intent(getContext(), WPZMGService3.class);
        if (!Util.isWPZMGServiceRunning) {
            Util.setResLastTime(0);
            TaskState.isWorking = false;
            getContext().stopService(intent2);
        } else {
            getContext().startService(intent2);
        }
    }

    public void hidePanel() {
        hidePanel1();
    }
}
