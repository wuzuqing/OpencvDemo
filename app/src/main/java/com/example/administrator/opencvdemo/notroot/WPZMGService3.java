package com.example.administrator.opencvdemo.notroot;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.example.administrator.opencvdemo.model.TaskRecordModel;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.util.http.GsonObjectCallback;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.example.administrator.opencvdemo.v2.TaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.administrator.opencvdemo.v2.task.StartAndLoginTaskElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class WPZMGService3 extends Service implements Constant {

    private TaskState mTaskState;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                TaskElement taskStateTask = mTaskState.getTask();
                if (taskStateTask instanceof StartAndLoginTaskElement && mTaskState.isMobaiEnd()){
                    for (TaskRecordModel.DataBean bean : mDataBeans) {
                        if (bean.getAccount().equals(mTaskState.getUserInfo().getName())){
                            mTaskState.saveNextUserInfo();
                            sendEmptyMessage(0);
                            return;
                        }
                    }
                }
                taskStateTask.printWorkName();
                taskStateTask.bindHandler(this);
                HandlerUtil.getInstance().execute(taskStateTask);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mTaskState = TaskState.get();
        mTaskState.init(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private List<TaskRecordModel.DataBean> mDataBeans = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Util.refreshNextDayTime();
        HttpManager.getTaskRecord(new GsonObjectCallback<TaskRecordModel>() {
            @Override
            public void onUi(TaskRecordModel result) {
                if (result != null ) {
                    mDataBeans = result.getData();
                }else {
                    mDataBeans = new ArrayList<>();
                }
                if (mDataBeans==null){
                    mDataBeans = new ArrayList<>();
                }
                TaskState.isWorking = true;
                mMainHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailed(Call call, IOException e) {
                mDataBeans = new ArrayList<>();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
