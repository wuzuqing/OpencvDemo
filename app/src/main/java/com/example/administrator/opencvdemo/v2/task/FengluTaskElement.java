package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.ACache;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;


/**
 * 领取俸禄
 */
public class FengluTaskElement extends AbsTaskElement {
    public FengluTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    PointModel getFengLu = PointManagerV2.get(HUANG_GONG_GET);
    PointModel wang = PointManagerV2.get(HUANG_GONG_WANG);
    PointModel huangGongClose = PointManagerV2.get(HUANG_GONG_CLOSE);

    @Override
    protected boolean doTaskBefore() {
        if (checkTime(KEY_WORK_FL, ACache.getTodayEndTime()) && !getTaskModel().isOnlyOne()) {
            return false;
        }
        return super.doTaskBefore();
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        printCurrentPage();
        if (checkPage("府内")) {
            PointManagerV2.execShellCmdChuFuV2();
            Thread.sleep(1800);
            return false;
        } else if (checkPage("府外")) {
            if (FuWaiHelper.isFuNei){
                FuWaiHelper.isFuNei = false;
                PointManagerV2.execShellCmdChuFuV2();
                Thread.sleep(1800);
                return false;
            }else if (Util.checkColor(FuNeiHelper.huaAn)){
                FuWaiHelper.isFuNei = false;
                PointManagerV2.execShellCmdChuFuV2();
                Thread.sleep(1800);
                return false;
            }
            FuWaiHelper.init();
            if (!Util.checkColorAndClick(FuWaiHelper.huangGong)) {
                clickMid(pageData.get(0).getRect());
            }
            Thread.sleep(800);
            click(wang);
            Thread.sleep(800);
            return false;
        } else if (checkPage("皇宫")) {
            clickMid(pageData.get(0).getRect());
            Thread.sleep(800);
        } else if (!checkPage("皇宫俸禄")) {
            if (check(12)) {
                resetStep();
                return true;
            }
            Thread.sleep(200);
            return false;
        }
        click(getFengLu);
        Thread.sleep(600);
        click(huangGongClose);
        Thread.sleep(300);
        click(huangGongClose);
        Thread.sleep(1200);
        Util.saveLastRefreshTime(KEY_WORK_FL, ACache.getTodayEndTime());
        return true;
    }
}
