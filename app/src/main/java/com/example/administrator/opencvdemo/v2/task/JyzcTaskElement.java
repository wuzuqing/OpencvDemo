package com.example.administrator.opencvdemo.v2.task;

import android.text.TextUtils;

import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.module_orc.OrcModel;

import org.opencv.core.Rect;

public class JyzcTaskElement extends AbsTaskElement {
    public JyzcTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    private boolean needClickZhengshou = true;
    private boolean isEnd;
    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
            AutoTool.execShellCmd(pageData.get(0).getRect());
            isEnd = false;
            Thread.sleep(1000);
            return false;
        } else if (checkPage("道具使用")) {
            AutoTool.execShellCmd(pageData.get(0).getRect());
            Thread.sleep(800);
            return false;
        } else if (!checkPage("经营资产")) {
            if (check(12)) {
                resetStep();
                return true;
            }
            Thread.sleep(200);
            return false;
        }
        if (needClickZhengshou){
            AutoTool.execShellCmd(CmdData.get(ZHENG_SHOU));
            Thread.sleep(800);
            needClickZhengshou = false;
            return false;
        }
        if (isEnd) {
            clickClose();
            Thread.sleep(1000);
            return true;
        }
        int count = 0;

        for (OrcModel orcModel : pageData) {
            if (TextUtils.equals("经营", orcModel.getResult())) {
                Rect rect = orcModel.getRect();
                AutoTool.execShellCmdXy(rect.x ,rect.y);
                Thread.sleep(200);
                count++;
            }
        }
        Thread.sleep(600);
         isEnd = count == 0;
        LogUtils.logd(" count" + count);
        return false;
    }
}
