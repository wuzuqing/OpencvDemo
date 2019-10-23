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
//                    while (hasGengXin) {               //检查进入游戏的环境
//                        if (TaskUtil.isDestory) return;
//                        LogUtils.logd("hasGengXin:" + hasGengXin);
//                        TaskUtil.sleep(2200);
//                        Util.getCapBitmapNew();
//                        if (Util.checkColor(genXin)) {
//                            AutoTool.execShellCmd(dialogClose2);  //维护公告
//                            Thread.sleep(TaskUtil.isNewApi ? 1200 : 600);
//                            break;
//                        } else if (Util.checkColor(startGame)) {
//                            break;
//                        }
//                        if (TaskUtil.check(TaskUtil.failCount, 12)) break;
//
//                    }

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
