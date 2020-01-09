package com.example.administrator.opencvdemo.v2.task;

import android.os.Handler;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.ACache;
import com.example.administrator.opencvdemo.util.LaunchManager;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.util.http.HttpManager;
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

    PointModel paiHang = PointManagerV2.get(PAI_HANG_BANG);
    PointModel bangDanSelf = PointManagerV2.get(BANG_DAN_SELF);
    PointModel bangDanKuaFu = PointManagerV2.get(BANG_DAN_CROSS);
    private boolean doBenfuBangDan;
    PointModel huangGongClose = PointManagerV2.get(HUANG_GONG_CLOSE);
    private int status;

    private int step = 0;

    @Override
    public void bindHandler(Handler handler) {
        super.bindHandler(handler);
        step = 0;
    }

    private boolean hasKuaFu = false;
    private boolean isInBangDan;

    @Override
    protected boolean doTaskBefore() {
        step = 0;
        doBenfuBangDan = false;
        if (checkTime(KEY_WORK_MB, ACache.getTodayEndTime())) {
            return false;
        }
        hasKuaFu = SPUtils.getBoolean(KEY_WORK_KF_MB, false);
        isInBangDan = false;
        return true;
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
            PointManagerV2.execShellCmdChuFuV2();
            Thread.sleep(1800);
            return false;
        } else if (checkPage("府外") && step == 0) {
            doBenfuBangDan = false;
            step = 1;
            swipeToRight();
            if (!Util.checkColorAndClick(FuWaiHelper.paiHangBang)) {
                click(paiHang);
            }
            Thread.sleep(800);
            return false;
        } else if (checkPage("排行榜") && step == 1) {
            if (doBenfuBangDan) {
                if (hasKuaFu) {
                    status = 0;
                    click(bangDanKuaFu);
                } else {
                    end();
                    return true;
                }
            } else {
                status = 0;
                click(bangDanSelf);
            }
            step = 2;
            Thread.sleep(1000);

        } else if (step == 2 && (checkPage("本服榜单") || checkPage("跨服榜单"))) {
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
                        click(target);
                        status = 1;
                        Thread.sleep(1000);
                    }
                } else if (status == 1) {
                    if (checkMobai(moBai, target)) {
                        clickEmpty(moBai);
                    } else {
                        target = pageData.get(2).getRect();
                        target.y += OrcConfig.offsetHeight;
                        click(target);
                        status = 2;
                        Thread.sleep(1000);
                    }
                } else if (status == 2) {
                    if (checkMobai(moBai, target)) {
                        clickEmpty(moBai);
                    } else {
                        if (TaskState.get().isMobaiEnd()){
                            over();
                            return true;
                        }else if (!doBenfuBangDan) {
                            click(huangGongClose);
                            Thread.sleep(1000);
                            doBenfuBangDan = true;
                            step = 1;
                            return false;
                        } else {
                            end();
                            return true;
                        }
                    }
                }
                pageData = Util.getBitmapAndPageData();
            }
        } else {
            if (check(20)) {
                if (step == 1 && TaskState.failCount == 5) {
                    SPUtils.setBoolean(CheckName.FU_WAI_PAI_HANG_BANG, false);
                    FuWaiHelper.paiHangBangInit();
                }
                resetStep();
                return true;
            }
            click(540,20);
            Thread.sleep(200);
            return false;
        }
        return false;
    }

    private void end() throws InterruptedException {

        Util.saveLastRefreshTime(KEY_WORK_KF_MB, ACache.getTodayEndTime());
        click(huangGongClose);
        Thread.sleep(1200);
        click(huangGongClose);
        Thread.sleep(800);
    }

    private void over(){
        Util.saveLastRefreshTime(KEY_WORK_KF_MB, ACache.getTodayEndTime());
        HttpManager.updateTask("mb_fl");
        LaunchManager.killApp();
    }

    private String midColor;

    private void clickEmpty(Rect moBai) throws InterruptedException {
        int x = BaseApplication.getScreenWidth() / 2;
        if (TextUtils.isEmpty(midColor)) {
            midColor = Util.getColor( x, moBai.y);
        }
        Thread.sleep(200);
        clickMid(moBai);
        Thread.sleep(2400);
        while (true) {
            Util.getCapBitmapWithOffset();
            if (TextUtils.equals(midColor, Util.getColor( x, moBai.y))) {
                break;
            }
            clickMid(moBai);
            Thread.sleep(400);
        }
    }

    private boolean checkMobai(Rect moBai, Rect target) {
        return target.width == moBai.width && target.height == moBai.height && target.x == moBai.x;
    }
}
