package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;

import java.util.ArrayList;
import java.util.List;

public class ShuyuanTaskElement extends AbsTaskElement {

    private PointModel oneKey = CmdData.get(Constant.ACADEMY_ONE_KEY);
    PointModel academyGetOk = CmdData.get(ACADEMY_GET_OK);
    private List<PointModel> shuYuans   ;
    public ShuyuanTaskElement(TaskModel taskModel) {
        super(taskModel);
        shuYuans = Util.getShuYanModel();
        if (shuYuans==null){
            shuYuans =new ArrayList<>();
        }
    }

    private boolean isInShuYuan;
    @Override
    protected boolean doTask() throws Exception{
//        if (checkTime( KEY_SHU_YUAN, ACache.TIME_DAY * 1)) {
//            return  true;
//        }
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
            AutoTool.execShellCmdChuFu();
            Thread.sleep(1200);
            return false;
        }else if (checkPage("府外")) {
            FuWaiHelper.init();
            if (!Util.checkColorAndClick(FuWaiHelper.shuYuan)){
                if (pageData.size()>0){
                AutoTool.execShellCmd(pageData.get(0).getRect());
                }
            }
            return false;
        } else   if (!checkPage("书院")) {

            if (check(12)) {
                resetStep();
                return true;
            }
            Thread.sleep(200);
            return false;
        }
        boolean isInit = SPUtils.getBoolean(CheckName.SHU_YUAN, false);
        if (!isInit){
            SPUtils.setBoolean(CheckName.SHU_YUAN, true);
            initPage();
        }
        if (Util.checkColorAndClick(oneKey)){
            Thread.sleep(1000);
            Util.checkColorAndClick(oneKey);
            Thread.sleep(1000);
            AutoTool.execShellCmd(academyGetOk);
            Thread.sleep(200);
        }else{
            for (PointModel model : shuYuans) {
                AutoTool.execShellCmd(model);
                Thread.sleep(1000);
                AutoTool.execShellCmd(academyGetOk);
                Thread.sleep(1000);
            }
        }
        AutoTool.execShellCmdClose();
       Thread. sleep(600);
        AutoTool.execShellCmdChuFu();
        Thread.  sleep(600);
        return true;
    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        int index = 0;
        oneKey.setX(0);
        oneKey.setY(0);
        for (Result.ItemsBean bean : result) {
            if (bean.getItemstring().endsWith("键完成")){
                bean.getItemcoord().setY(bean.getItemcoord().getY()-20);
                setNewCoord(oneKey,bean.getItemcoord());
                needSaveCoord  = true;
            }
            if (equals("已完成",bean.getItemstring()) || equals("请点击",bean.getItemstring())){
                if (shuYuans.size()>index){
                    setNewCoord( shuYuans.get(index),bean.getItemcoord());
                    index++;
                }
                needSaveCoord  = true;
            }
        }
    }
}
