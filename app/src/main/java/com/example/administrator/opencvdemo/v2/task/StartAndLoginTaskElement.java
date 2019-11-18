package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;

import java.util.List;

public class StartAndLoginTaskElement extends AbsTaskElement {
    PointModel pointModel = CmdData.get(LOGIN_GAME);

    public StartAndLoginTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查网络 等待2秒
        while (!isNetConnected()) {
            if (check(TaskUtil.failCount, 5)) {
                TaskState.isWorking = false;
                return true;
            }
            Thread.sleep(2000);
        }
        if (EventHelper.isGame()) {
            killApp();
        }

        // 启动游戏
        LaunchApp.launchApp();
        Thread.sleep(4000);
        //获取账号
        TaskState.resetFail();

        while (TaskState.isWorking) {
            Util.getCapBitmapWithOffset();
            LogUtils.logd("step:1 capScreen");
            if (Util.checkColorAndOffset(pointModel)) {
                break;
            }
            LogUtils.logd("step:2 parsePage");
            pageData = Util.getPageData();
            if (checkPage("登录")) {
                setNewCoord(pointModel, pageData.get(0).getRect());
                LogUtils.logd("step:3 resetCoord");
                break;
            }  else if (check(6)) {
                boolean isInit = SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, false);
                if (!isInit) {
//                    SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, true);
                    initPage();
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
            AutoTool.execShellCmd(pointModel);
            LogUtils.logd("step:6 click default");
        } else {
            AutoTool.execShellCmd(pageData.get(0).getRect()); //点击登录
            LogUtils.logd("step:7 click parsePage");
        }
//        TaskUtil.sleep(1400);
        LogUtils.logd("step:7 click finish");
        return true;
    }

    private void killApp() throws InterruptedException {
        // 退出游戏
        AutoTool.killApp();

    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        for (Result.ItemsBean itemsBean : result) {
            if (equals(CheckName.LOGIN_BTN_NAME, itemsBean.getItemstring())) {
                // 重新设置坐标信息
                setNewCoord(pointModel, itemsBean.getItemcoord());
                SPUtils.setBoolean(CheckName.LOGIN_BTN_VERSION, true);
                needSaveCoord = true;
                return;
            }
        }
//        SPUtils.setBoolean(CheckName.LOGIN_BTN_VERSION,false);
    }

}
