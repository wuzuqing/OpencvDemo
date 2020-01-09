package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.ACache;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.module_orc.OrcModel;


public class ClzwTaskElement extends AbsTaskElement {
    public static PointModel beiJing = PointManagerV2.get(Constant.CLZW_BG);
    public static PointModel getZhengJi = PointManagerV2.get(Constant.CLZW_GET);

    public ClzwTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    private boolean isJoinClzw;

    @Override
    protected boolean doTaskBefore() {
        isJoinClzw = false;
        return true;
    }

    @Override
    protected boolean doTask() throws Exception {
        if (checkTime(KEY_WORK_ZW, ACache.TIME_HOUR * 2)) {
            return true;
        }
        while (TaskState.isWorking && !isJoinClzw) {
            Util.getCapBitmapWithOffset();
            if (FuNeiHelper.shiYe != null && Util.checkColor(FuNeiHelper.shiYe)) {
                click(FuNeiHelper.shiYe);
                isJoinClzw = true;
                break;
            }
            if (checkExp(netPoint, "当前网络异常")) {
                continue;
            }
            pageData = Util.getPageData();
            if (checkPage("府外")) {

            } else if (checkPage("府内")) {
                clickMid(pageData.get(1).getRect());
                isJoinClzw = true;
                break;
            } else if (!checkPage("处理公务")) {
                if (check(12)) {
                    resetStep();
                    return true;
                }
                Thread.sleep(200);
                return false;
            }
        }
        Thread.sleep(1000);
        while (TaskState.isWorking && isJoinClzw) {
            LogUtils.logd("step:1 cap");
            Util.getCapBitmapWithOffset();
            LogUtils.logd("step:1 check beiJing");
            if (Util.checkColorAndOffset(beiJing)) {
                clickClose();
                Thread.sleep(800);
                break;
            } else if (checkPage("道具使用")) {
                clickMid(pageData.get(0).getRect());
                Thread.sleep(800);
                continue;
            }else if (checkExp(netPoint, "当前网络异常")) {
                continue;
            }
            boolean hasGet = false;
            if (Util.checkColorAndOffset(getZhengJi)) {
                click(getZhengJi);
                hasGet = true;
            } else {
                pageData = Util.getPageData();
                for (OrcModel model : pageData) {
                    if (model.getResult().startsWith("获得")) {
                        clickMid(model.getRect());
                        hasGet = true;
                        break;
                    }
                }
            }
            if (hasGet) {
                Thread.sleep(700);
                continue;
            }
            boolean hasResetBg = SPUtils.getBoolean(CheckName.ZHEGN_WU_BEI_JING, false);
            if (!hasResetBg) {
                String color = Util.getColor(beiJing);
                beiJing.setNormalColor(color);
                needSaveCoord = true;
            }

            clickClose();
            Thread.sleep(800);
            Util.saveLastRefreshTime(KEY_WORK_ZW, ACache.TIME_HOUR * 2);
        }
        return true;
    }
}
