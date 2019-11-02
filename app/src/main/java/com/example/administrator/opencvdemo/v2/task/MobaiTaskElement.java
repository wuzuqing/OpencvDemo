package com.example.administrator.opencvdemo.v2.task;

import android.os.Handler;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.ignore.BenfubangdanIgnoreRect;

import org.opencv.core.Rect;

public class MobaiTaskElement extends AbsTaskElement {
    public MobaiTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    PointModel paiHang = CmdData.get(PAI_HANG_BANG);
    PointModel bangDanSelf = CmdData.get(BANG_DAN_SELF);
    PointModel bangDanKuaFu = CmdData.get(BANG_DAN_CROSS);
    private boolean doBenfuBangDan;
    PointModel huangGongClose = CmdData.get(HUANG_GONG_CLOSE);
    private int status;

    private boolean isWait;
    private int step = 0;

    @Override
    public void bindHandler(Handler handler) {
        super.bindHandler(handler);
        step = 0;
    }

    @Override
    protected boolean doTask() throws Exception {
//        if (checkTime( KEY_WORK_MB, ACache.TIME_DAY)) {
//            return  true;
//        }
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
//            FuNeiHelper.init();
            AutoTool.execShellCmdChuFu();
            Thread.sleep(1800);
            return false;
        } else if (checkPage("府外") && step==0) {
            doBenfuBangDan = false;
            step = 1;
            swipeToRight();
            if (!Util.checkColorAndClick(FuWaiHelper.paiHangBang)){
                AutoTool.execShellCmd(paiHang);
            }
            Thread.sleep(800);
            return false;
        } else if (checkPage("排行榜") && step == 1) {
            if (doBenfuBangDan) {
                status = 0;
                AutoTool.execShellCmdNotOffset(bangDanKuaFu);
            } else {
                status = 0;
                AutoTool.execShellCmdNotOffset(bangDanSelf);
            }
            step = 2;
            Thread.sleep(1000);

        } else if (step==2 && ( checkPage("本服榜单") || checkPage("跨服榜单"))) {
            Rect moBai = BenfubangdanIgnoreRect.moBaiMax.clone();
            moBai.y += OrcConfig.offsetHeight;
            Rect target;
            while (true) {
                target = pageData.get(0).getRect();   // 膜拜
                if (status == 0) {
                    if (checkMobai(moBai, target)) {
                        clickEmpty(moBai);
                    } else {
                        target = pageData.get(1).getRect().clone();
                        target.y += OrcConfig.offsetHeight;
                        AutoTool.execShellCmdXy(target.x, target.y);
                        status = 1;
                        Thread.sleep(2000);
                    }
                } else if (status == 1) {
                    if (checkMobai(moBai, target)) {
                        clickEmpty(moBai);
                    } else {
                        target = pageData.get(2).getRect();
                        target.y += OrcConfig.offsetHeight;
                        AutoTool.execShellCmdXy(target.x, target.y);
                        status = 2;
                        Thread.sleep(2000);
                    }
                } else if (status == 2) {
                    if (checkMobai(moBai,target)) {
                        clickEmpty(moBai);
                    } else {
                        if (!doBenfuBangDan) {
                            AutoTool.execShellCmdNotOffset(huangGongClose);
                            Thread.sleep(1200);
                            doBenfuBangDan = true;
                            step = 1;
                            return false;
                        } else {
                            AutoTool.execShellCmdNotOffset(huangGongClose);
                            Thread.sleep(1200);
                            AutoTool.execShellCmdNotOffset(huangGongClose);
                            Thread.sleep(800);
                            return true;
                        }
                    }
                }
                pageData = Util.getBitmapAndPageData();
            }
        } else {
            if (check(20)) {
                if (step==1 && TaskState.failCount==5){
                    SPUtils.setBoolean(CheckName.FU_WAI_PAI_HANG_BANG, false);
                    FuWaiHelper.paiHangBangInit();
                }
                resetStep();
                return true;
            }
            AutoTool.execShellCmdXy(540,20);
            Thread.sleep(200);
            return false;
        }
        return false;
    }

    private void clickEmpty(Rect moBai) throws InterruptedException {
        Thread.sleep(200);
        AutoTool.execShellCmd(moBai);
        Thread.sleep(4500);
        AutoTool.execShellCmd(moBai);
        Thread.sleep(800);
        AutoTool.execShellCmd(moBai);
        Thread.sleep(800);
    }

    private boolean checkMobai(Rect moBai, Rect target) {
        return target.width == moBai.width && target.height == moBai.height && target.x ==moBai.x;
    }
}
