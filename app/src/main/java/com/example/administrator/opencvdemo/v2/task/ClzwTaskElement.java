package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.module_orc.OrcModel;


public class ClzwTaskElement extends AbsTaskElement {
    public static PointModel beiJing  =CmdData.get(Constant.ZHENG_JI_BG);
    public ClzwTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
//        if (checkTime( KEY_WORK_ZW, ACache.TIME_HOUR * 2)) {
//            return  true;
//        }
        Util.getCapBitmapNew();
        if (Util.checkColor(beiJing)){
            clickClose();
            Thread.sleep(800);
            return true;
        }
        pageData = Util.getPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
            FuNeiHelper.init();
            if (FuNeiHelper.shiYe!=null && Util.checkColor(FuNeiHelper.shiYe)){
                AutoTool.execShellCmd(FuNeiHelper.shiYe);
            }else{
                AutoTool.execShellCmd(pageData.get(1).getRect());
            }
            Thread.sleep(1000);
            return false;
        } else if (checkPage("道具使用")) {
            AutoTool.execShellCmd(pageData.get(0).getRect());
            Thread.sleep(800);
            return false;
        } else if (!checkPage("处理公务")) {
            if (check(12)) {
                resetStep();
                return true;
            }
            Thread.sleep(200);
            return false;
        }
        for (OrcModel model : pageData) {
            if (model.getResult().startsWith("获得")){
                AutoTool.execShellCmd(model.getRect());
                Thread.sleep(700);
                return false;
            }
        }

        boolean hasResetBg = SPUtils.getBoolean(CheckName.ZHEGN_WU_BEI_JING, false);
        if (!hasResetBg){
            String color = Util.getColor(beiJing);
            beiJing.setNormalColor(color);
            needSaveCoord = true;
        }

        clickClose();
        Thread.sleep(800);

        return true;
    }
}
