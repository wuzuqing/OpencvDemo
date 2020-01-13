package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.LaunchManager;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;
import org.opencv.core.Point;

import java.util.List;

public class StartAndLoginTaskElement extends AbsTaskElement {
    PointModel pointModel = PointManagerV2.get(LOGIN_GAME);

    public StartAndLoginTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查网络 等待2秒
        while (!isNetConnected()) {
            if (check(5)) {
                TaskState.isWorking = false;
                return true;
            }
            Thread.sleep(2000);
        }
        if (EventHelper.isGame()) {
            killApp();
        }

        // 启动游戏
        LaunchManager.launchApp();
        Thread.sleep(4000);
        Util.gc();
        //获取账号
        TaskState.resetFail();

        while (TaskState.isWorking) {
            Util.getCapBitmapWithOffset();
            LogUtils.logd("step:1 capScreen");
            if (Util.checkColorAndOffset(pointModel) ) {
                break;
            }
            if (AutoTool.isEmulator(BaseApplication.getAppContext())){
                pointModel.setX(540);
                pointModel.setY(1072);
                if (Util.checkColor(pointModel)){
                    break;
                }
            }
            LogUtils.logd("step:2 parsePage");
            pageData = Util.getPageData();
            LogUtils.logd("pageData:"+pageData.toString());
            if (checkPage("登录")) {
                setNewCoord(pointModel, pageData.get(0).getRect());
                LogUtils.logd("step:3 resetCoord");
                break;
            }  else if (check(6)) {
                boolean isInit = SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, false);
                if (!isInit) {
                    if (AutoTool.isEmulator(BaseApplication.getAppContext())){
                        SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, true);
                        initPage();
                    }
                }
                LogUtils.logd("step:4 continue");
                return false;
            }else if (check(12)){
                // 退出游戏
                killApp();
                return true;
            }
            Thread.sleep(600);
        }
        if (TaskState.needContinue) return false;
//        boolean isInit = SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, false);
//        if (!isInit) {
//            SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, true);
//            initPage();
//        }
        LogUtils.logd("step:5 input account");
        //输入账号
        UserInfo userInfo = TaskState.get().getUserInfo();
        EventHelper.inputUserInfo(userInfo.getName());
        Thread.sleep(800);

        if (Util.checkColor(pointModel)) {
            click(pointModel);
            LogUtils.logd("step:6 click default");
        } else {
            clickMid(pageData.get(0).getRect()); //点击登录
            LogUtils.logd("step:7 click parsePage");
        }
//        TaskUtil.sleep(1400);
        LogUtils.logd("step:7 click finish");
        return true;
    }

    private void killApp()  {
        // 退出游戏
        LaunchManager.killApp();
    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        for (Result.ItemsBean itemsBean : result) {
            if (equals(CheckName.LOGIN_BTN_NAME, itemsBean.getItemstring())) {
                // 重新设置坐标信息
                setNewCoord(pointModel, itemsBean.getItemcoord());
                SPUtils.setBoolean(CheckName.LOGIN_BTN_VERSION, true);
                // needSaveCoord = true;
                return;
            }
        }
//        SPUtils.setBoolean(CheckName.LOGIN_BTN_VERSION,false);
    }

}
