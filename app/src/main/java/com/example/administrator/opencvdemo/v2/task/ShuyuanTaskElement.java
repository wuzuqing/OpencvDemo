package com.example.administrator.opencvdemo.v2.task;


import android.util.TypedValue;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.ACache;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;

import java.util.ArrayList;
import java.util.List;

public class ShuyuanTaskElement extends AbsTaskElement {

    private PointModel oneKey = CmdData.get(Constant.ACADEMY_ONE_KEY);
    PointModel academyGetOk = CmdData.get(ACADEMY_GET_OK);
    private List<PointModel> shuYuans;
    private int offsetY;

    public ShuyuanTaskElement(TaskModel taskModel) {
        super(taskModel);
        shuYuans = Util.getShuYanModel();
        if (shuYuans == null) {
            shuYuans = new ArrayList<>();
        }
        offsetY = (int) TypedValue.applyDimension(1, 40, BaseApplication.getAppContext().getResources().getDisplayMetrics());
    }

    private boolean isInShuYuan;

    @Override
    protected boolean doTask() throws Exception {
        if (checkTime( KEY_SHU_YUAN, ACache.TIME_HOUR * 3)) {
            return  true;
        }
        while (TaskState.isWorking && !isInShuYuan) {
            pageData = Util.getBitmapAndPageData();
            if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
            if (checkPage("府内")) {
                AutoTool.execShellCmdChuFu();
                Thread.sleep(1200);
                continue;
            } else if (checkPage("府外")) {
                FuWaiHelper.init();
                if (!Util.checkColorAndClick(FuWaiHelper.shuYuan)) {
                    if (pageData.size() > 0) {
                        AutoTool.execShellCmd(pageData.get(0).getRect());
                        isInShuYuan = true;
                        break;
                    }
                } else {
                    isInShuYuan = true;
                    break;
                }
            } else if (check(10)) {
                resetStep();
                return true;
            }
        }
        Thread.sleep(800);
        pageData = Util.getBitmapAndPageData();
        boolean isInit = SPUtils.getBoolean(CheckName.SHU_YUAN, false);
        if (!isInit) {
            initPage();
        }
        while (TaskState.isWorking && isInShuYuan) {
            pageData = Util.getBitmapAndPageData();
            if (Util.checkColorAndClick(oneKey)) {
                Thread.sleep(1000);
                Util.checkColorAndClick(oneKey);
                Thread.sleep(1000);
                AutoTool.execShellCmd(academyGetOk);
                Thread.sleep(200);
            } else {
                for (PointModel model : shuYuans) {
                    if (Util.checkSubColor(model)){
                        AutoTool.execShellCmd(model);
                        Thread.sleep(2000);
                        AutoTool.execShellCmd(model);
                        Thread.sleep(700);
                        AutoTool.execShellCmd(academyGetOk);
                        Thread.sleep(800);
                    }
                }
            }
            AutoTool.execShellCmdClose();
            Thread.sleep(600);
            AutoTool.execShellCmdChuFu();
            Thread.sleep(600);
            Util.saveLastRefreshTime(KEY_SHU_YUAN, ACache.TIME_HOUR * 3);
            break;
        }

        return true;
    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        int index = 0;
        for (Result.ItemsBean bean : result) {
            if (bean.getItemstring().endsWith("键完成")) {
                bean.getItemcoord().setY(bean.getItemcoord().getY() - 20);
                setNewCoord(oneKey, bean.getItemcoord());
                needSaveCoord = true;
                SPUtils.setBoolean(CheckName.SHU_YUAN, true);
            }
            if (equals("已完成", bean.getItemstring()) || equals("请点击派遣门客学习", bean.getItemstring()) ||  bean.getItemstring().startsWith("请点击")) {
                if (shuYuans.size() > index) {
                    setNewCoord(shuYuans.get(index), bean.getItemcoord(), offsetY);
                    index++;
                }
                needSaveCoord = true;
                SPUtils.setBoolean(CheckName.SHU_YUAN, true);
            }
        }
    }
}
