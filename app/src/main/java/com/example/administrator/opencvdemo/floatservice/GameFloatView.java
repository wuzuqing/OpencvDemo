package com.example.administrator.opencvdemo.floatservice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.activity.AssetsPointSettingActivity;
import com.example.administrator.opencvdemo.activity.DialogActivity;
import com.example.administrator.opencvdemo.notroot.WPZMGService3;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.Test;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.TaskState;

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
                llPanel.setVisibility(View.GONE);
                tvShowOrHide.setText("显示");
            }
        });
        findViewById(R.id.tvStartSome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xiaoHao(true);
                ((TextView) v).setText(Util.isWPZMGServiceRunning ? "取消挂机" : "开始挂机");
                llPanel.setVisibility(View.GONE);
                tvShowOrHide.setText("显示");
            }
        });
        findViewById(R.id.tvOneTask).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.tvStartOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HandlerUtil.async(new Runnable() {
//                    @Override
//                    public void run() {
//                        long start = System.currentTimeMillis();
//                        ScreenCapture.startCaptureSync();
//                        long end = System.currentTimeMillis();
//                        List<OrcModel> result = OrcHelper.getInstance().executeCallSync(ScreenCapture.get().getCurrentBitmap());
//                        if (result.size() >= 1) {
//                            AutoTool.execShellCmd(result.get(0).getRect());
//                        }
//                        BitmapPreviewActivity.show(getContext());
//                        Log.d(TAG, "used:" + (end - start) + " call: " + result.toString());
//                    }
//                });
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
//                AutoTool.execShellCmd("input swipe 300 100 100 100");
//                EventHelper.click(300,400);
                llPanel.setVisibility(View.GONE);
                tvShowOrHide.setText("显示");
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

            }
        });
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
        llPanel.setVisibility(View.GONE);
        tvShowOrHide.setText("显示");
    }
}
