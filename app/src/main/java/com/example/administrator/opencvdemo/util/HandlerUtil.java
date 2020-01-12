package com.example.administrator.opencvdemo.util;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.administrator.opencvdemo.v2.TaskElement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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


    public static HandlerUtil getInstance(){
        return sHandlerUtil;
    }
    private HandlerUtil(){
        mExecutorService = Executors.newFixedThreadPool(4);
    }

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


    public static void post(Runnable runnable, int time) {
        mHandler.postDelayed(runnable,time);
    }


    private static TextView tvDjs;
    public static void startDjs(TextView tvLimitTime) {
        tvDjs = tvLimitTime;

    }

    private ExecutorService mExecutorService;
    private static HandlerUtil sHandlerUtil = new HandlerUtil();

    public ExecutorService getExecutorService() {
        return mExecutorService;
    }

    public void execute(TaskElement task) {
        if (mExecutorService==null){
            mExecutorService = Executors.newFixedThreadPool(4);
        }
        mExecutorService.execute(task);
    }
}
