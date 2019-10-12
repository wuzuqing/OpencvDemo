package com.example.administrator.opencvdemo.util;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


/**
 * @author 吴祖清
 * @version 1.0
 * @createDate 2017/12/24 12:15
 * @des ${TODO}
 * @updateAuthor #author
 * @updateDate 2017/12/24
 * @updateDes ${TODO}
 */

public class HandlerUtil implements Constant{

    private static Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TASK_ERROR:
                    mHandler.sendEmptyMessageDelayed(RESET_TASK, 3000);
                    break;
                case TASK_TOAST:
                    LogUtils.logd("正在执行" + msg.obj.toString());
                    break;
                case TASK_TOAST_COUNT_DOWN:

                    break;
                case TASK_STOP:

                    break;
            }
            return false;
        }
    });

    public static void async(Runnable task) {
        new Thread(task).start();
    }
    public static void async(final String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AutoTool.execShellCmd(cmd);
            }
        }).start();
    }

    public static void post(Runnable runnable, int time) {
        mHandler.postDelayed(runnable,time);
    }

    public static void send(String message) {
    }
    private static TextView tvDjs;
    public static void startDjs(TextView tvLimitTime) {
        tvDjs = tvLimitTime;

    }
}
