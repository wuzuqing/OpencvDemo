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
import com.example.administrator.opencvdemo.activity.AssetsPointSettingActivity;
import com.example.administrator.opencvdemo.activity.DialogActivity;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.notroot.ServiceHelper;
import com.example.administrator.opencvdemo.notroot.WPZMGService3;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Test;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.administrator.opencvdemo.v2.task.ShuyuanTaskElement;

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
                if (!ServiceHelper.getInstance().goAccess()){
                    xiaoHao(true);
                    ((TextView) v).setText(Util.isWPZMGServiceRunning ? "取消挂机" : "开始挂机");
                    hidePanel1();
                }
            }
        });
        findViewById(R.id.tvOneTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.tvStartOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel1();
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        Util.getCapBitmapNew();
                        Test.test(ScreenCapture.get().getCurrentBitmap());
                    }
                });
                Test.testWork();
            }
        });
        findViewById(R.id.tvLaunch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchApp.launchapp(getContext());

            }
        });
        findViewById(R.id.tvUpgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel1();
                Test.testWork();

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

        findViewById(R.id.tvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 主号
                if (!ServiceHelper.getInstance().goAccess()){
                    hidePanel1();
                    TaskState.isWorking = true;
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(new ShuyuanTaskElement(new TaskModel("书院",true)));
                }
            }
        });
    }

    private void hidePanel1() {
        llPanel.setVisibility(View.GONE);
        tvShowOrHide.setText("显示");
    }

    private void xiaoHao(boolean some) {
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
