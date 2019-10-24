package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;

public class StartAndLoginTaskElement extends AbsTaskElement {

    public StartAndLoginTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查网络 等待2秒
        while (!isNetConnected()) {
            if (check(TaskUtil.failCount, 5)) {
                TaskState.isWorking=false;
                return true;
            }
            Thread.sleep(2000);
        }
        // 退出游戏
        AutoTool.keyEvent(4);
        Thread.sleep(800);

        //
        AutoTool.execShellCmdXy(899, 1117);
        Thread.sleep(1200);

        // 启动游戏
        LaunchApp.launchApp();
        Thread.sleep(4000);
        //获取账号
        TaskState.resetFail();

        while (TaskState.isWorking) {
            pageData = Util.getBitmapAndPageData();
            if (checkPage("登录")  ) {
                break;
            }else if (check(8)){

                return true;
            }
            Thread.sleep(600);
        }
        if (TaskState.needContinue) return false;
        //输入账号
        UserInfo userInfo = TaskState.get().getUserInfo();
        EventHelper.inputUserInfo(userInfo.getName());
        Thread.sleep(800);
        AutoTool.execShellCmd(pageData.get(0).getRect()); //点击登录
        TaskUtil.sleep(1000);

        return true;
    }


}
