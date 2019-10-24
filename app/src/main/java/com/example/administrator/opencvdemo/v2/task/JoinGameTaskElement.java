package com.example.administrator.opencvdemo.v2.task;

import android.util.Log;

import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;


public class JoinGameTaskElement extends AbsTaskElement {
    public JoinGameTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查是否有更新

        Log.d(TAG, "doTask: start");
        while (TaskState.isWorking) {
            pageData = Util.getBitmapAndPageData();
            if (checkPage("进入游戏")) {
                AutoTool.execShellCmd(pageData.get(0).getRect());  //进入游戏
                Thread.sleep(1200);
                break;
            } else if (check(8)) {
                resetStep();
                return true;
            }
            TaskUtil.sleep(400);
        }

        while (TaskState.isWorking) {                           //检查 通告对话框的环境
            pageData = Util.getBitmapAndPageData();
            if (checkPage("游戏公告")) {
                AutoTool.execShellCmdXy(pageData.get(1).getRect().x,pageData.get(1).getRect().y);  //关闭通告对话框
                break;
            } else if (check(8)) {
                resetStep();
                return true;
            }
            TaskUtil.sleep(200);
        }
        return true;
    }
}
