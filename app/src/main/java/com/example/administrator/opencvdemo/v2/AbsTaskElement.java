package com.example.administrator.opencvdemo.v2;

import android.os.Handler;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.NetWorkUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;
import com.example.module_orc.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsTaskElement implements TaskElement, Constant {
    public static final String TAG = "TaskElement";

    public AbsTaskElement() {
    }

    public AbsTaskElement(TaskModel taskModel) {
        mTaskModel = taskModel;
    }

    private Handler taskHandler;

    protected TaskModel mTaskModel;
    protected List<OrcModel> pageData = null;
    protected PointModel netPoint = CmdData.get(Constant.NET_CLOSE);

    @Override
    public void bindHandler(Handler handler) {
        taskHandler = handler;
    }

    public void setTaskModel(TaskModel taskModel) {
        mTaskModel = taskModel;
    }

    @Override
    public TaskModel getTaskModel() {
        return mTaskModel;
    }

    @Override
    public final void run() {
        TaskState.resetFail();
        while (TaskState.isWorking) {
            // if (!EventHelper.isGame()) {
            //     sleep(2000);
            //     continue;
            // }
            try {
                boolean isEnd = doTask();
                if (isEnd) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TaskState.isWorking) {
            if (taskHandler != null) {
                TaskState.get().saveNextTask();
                taskHandler.sendEmptyMessage(0);
            }
        }
    }

    private void sleep(long tile) {
        try {
            Thread.sleep(tile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract boolean doTask() throws Exception;

    public boolean isNetConnected() {
        return NetWorkUtils.isNetConnected(BaseApplication.getAppContext());
    }

    protected boolean check(int failCount, int max) {
        return TaskState.check(failCount, max);
    }

    protected boolean check(int max) {
        return check(TaskState.failCount, max);
    }

    protected boolean checkPage(String currentPage) {
        return TextUtils.equals(currentPage, OrcConfig.pageName);
    }

    protected void resetStep() {
        TaskState.get().resetStep();
    }

    public static boolean checkExp(PointModel model, String msg) throws InterruptedException {
        //        if (TaskUtil.bitmap == null || TaskUtil.bitmap.isRecycled()) return true;
        //        if (Util.getColor(TaskUtil.bitmap, model.getX(), model.getY()).equals(model.getNormalColor())) { //检查网络环境
        //            AutoTool.execShellCmd(model);
        //            Thread.sleep(600);
        //            return true;
        //        }
        return false;
    }

    protected void clickClose() {
        AutoTool.execShellCmdClose();
    }

    public boolean checkTime(String type, int saveTime) {
        return Util.checkTime(TaskState.get().getUserInfo(), type, saveTime);
    }

    @Override
    public void printWorkName() {
        if (getTaskModel() != null) {
            LogUtils.logd("当前任务：" + getTaskModel().getName());
        }
    }

    public void initPage(){
        ImageParse.getSyncData(ScreenCapture.get().getCurrentBitmap(),new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                try {
                   callBack(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void callBack(List<Result.ItemsBean> result) {

    }
}
