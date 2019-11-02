package com.example.administrator.opencvdemo.v2.task;


import android.text.TextUtils;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.module_orc.OrcConfig;

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
            pageData = Util.getBitmapAndPageData();
            if (Util.checkColor(pointModel)) {
                break;
            } else if (checkPage("登录")) {
                setNewCoord(pointModel,pageData.get(0).getRect());
                break;
            } else if (!TextUtils.isEmpty(OrcConfig.pageName)) {
                // 退出游戏
                killApp();
                return false;
            } else if (check(3)) {
                boolean isInit = SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, false);
                if (!isInit) {
//                    SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, true);
                    initPage();
                }
                return false;
            }
            Thread.sleep(600);
        }
        if (TaskState.needContinue) return false;
        boolean isInit = SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, false);
        if (!isInit) {
            SPUtils.getBoolean(CheckName.LOGIN_BTN_VERSION, true);
            initPage();
        }
        //输入账号
        UserInfo userInfo = TaskState.get().getUserInfo();
        EventHelper.inputUserInfo(userInfo.getName());
        Thread.sleep(800);

        if (Util.checkColor(pointModel)) {
            AutoTool.execShellCmd(pointModel);
        } else {
            AutoTool.execShellCmd(pageData.get(0).getRect()); //点击登录
        }
        TaskUtil.sleep(1400);

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
