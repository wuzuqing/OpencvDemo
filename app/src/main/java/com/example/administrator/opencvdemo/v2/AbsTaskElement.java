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
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.NetWorkUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import org.opencv.core.Rect;

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

    protected boolean needSaveCoord;

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
        boolean taskBefore = doTaskBefore();
        while (TaskState.isWorking && taskBefore) {
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
        if (needSaveCoord) {
            String jsonList = JsonUtils.toJson(CmdData.coordinateList);
            SPUtils.setString(COORDINATE_KEY, jsonList);
            needSaveCoord = false;
        }
        if (TaskState.isWorking) {
            if (mTaskModel.isOnlyOne()) {
                TaskState.isWorking = false;
                return;
            }
            if (taskHandler != null &&  !TaskState.get().isEnd()) {
                TaskState.get().saveNextTask();
                taskHandler.sendEmptyMessage(0);
            }
        }
    }

    protected boolean doTaskBefore() {
        return true;
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
        if (TaskUtil.bitmap == null || TaskUtil.bitmap.isRecycled()) return true;
        if (Util.getColor(TaskUtil.bitmap, model.getX(), model.getY() + OrcConfig.offsetHeight).equals(model.getNormalColor())) { //检查网络环境
            AutoTool.execShellCmd(model);
            Thread.sleep(600);
            return true;
        }
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

    protected void swipeToRight() throws InterruptedException {
        EventHelper.swipeHor(800, 100, 600);
        Thread.sleep(2400);
        FuWaiHelper.paiHangBangInit();
    }

    public void initPage() {
        ImageParse.getSyncData(ScreenCapture.get().getCurrentBitmap(), new ImageParse.Call() {
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

    public boolean equals(String text, String text1) {
        return TextUtils.equals(text, text1);
    }

    protected void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord) {
        setNewCoord(model, coord, 0);
    }

    protected void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord, int offsetY) {
        if (model == null) {
            return;
        }
        int oldX = model.getX();
        int oldY = model.getY();
        String oldColor = model.getNormalColor();
        model.setX(coord.getX() + coord.getWidth() / 2);
        model.setY(coord.getY() + coord.getHeight() / 2);
        model.setNormalColor(Util.getColor(model));
        if (offsetY != 0) {
            model.setSubY(model.getY() + offsetY);
            int height = TaskUtil.bitmap.getHeight();
            if (model.getSubY() > height) {
                model.setSubY(height - 10);
            }
            model.setSubColor(Util.getColor(TaskUtil.bitmap, model.getX(), model.getSubY()));
            LogUtils.logd("oldX:" + oldX + " newX:" + model.getX() + "oldY:" + oldY + " newY:" + model.getY() + "oldColor:" + oldColor + " newColor:" + model.getNormalColor() + " SubColor:" + model.getSubColor());
        } else {
            LogUtils.logd("oldX:" + oldX + " newX:" + model.getX() + "oldY:" + oldY + " newY:" + model.getY() + "oldColor:" + oldColor + " newColor:" + model.getNormalColor());
        }
    }

    protected void setNewCoord(PointModel model, Rect rect) {
        if (model == null) {
            return;
        }
        int oldX = model.getX();
        int oldY = model.getY();
        if (oldX == rect.x && oldY == rect.y) {
            return;
        }
        String oldColor = model.getNormalColor();
        model.setX(rect.x + rect.width / 2);
        model.setY(rect.y + rect.height / 2);
        model.setNormalColor(Util.getColor(model));
        LogUtils.logd("oldX:" + oldX + " newX:" + model.getX() + "oldY:" + oldY + " newY:" + model.getY() + "oldColor:" + oldColor + " newColor:" + model.getNormalColor());
        needSaveCoord = true;
    }
}
