package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.util.LaunchManager;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;

import java.util.List;

import static com.example.administrator.opencvdemo.config.CheckName.GAME_NOTICE_BTN_NAME;


public class JoinGameTaskElement extends AbsTaskElement {
    private PointModel startPoint = PointManagerV2.get(START_GAME);
    private PointModel gameNoticePoint = PointManagerV2.get(GAME_NOTICE_DIALOG_CLOSE);
    public JoinGameTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查是否有更新
        PointModel pointModel = PointManagerV2.get(LOGIN_GAME);
        Thread.sleep(1400);
        while (TaskState.isWorking) {
            LogUtils.logd("step:1 capScreen");
            Util.getCapBitmapWithOffset();
            if (Util.checkColorAndOffset(startPoint)){
                LogUtils.logd("step:2 click default");
                click(startPoint);
                break;
            }
            pageData = Util.getPageData();
            if (checkPage("进入游戏")) {
                setNewCoord(startPoint,pageData.get(0).getRect());
                click(pageData.get(0).getRect());  //进入游戏
                LogUtils.logd("step:3 click parse page");
                break;
            }else if (Util.checkColor(pointModel)){
                LogUtils.logd("step:4 click login default");
                click(pointModel); //点击登录
                Thread.sleep(1200);
            }else  if (checkPage("登录")) {
                LogUtils.logd("step:4 click login  parse page");
                clickMid(pageData.get(0).getRect()); //点击登录
                Thread.sleep(1200);
                break;
            }else if (check(8)) {
                LaunchManager.killApp();
                resetStep();
                return true;
            }else{
                boolean isInit = SPUtils.getBoolean(CheckName.START_BTN_VERSION, false);
                if (!isInit){
                    Util.sleep(400);
                    SPUtils.getBoolean(CheckName.START_BTN_VERSION, true);
                    initPage();
                }
            }
            Util.sleep(400);
        }
        LogUtils.logd("step:5 wait");
        Thread.sleep(3500);
        while (TaskState.isWorking) {
            Util.getCapBitmapWithOffset();
            if (Util.checkColorAndOffset(gameNoticePoint)){
                click(gameNoticePoint);
                LogUtils.logd("step:6 click default");
                break;
            }
            //检查 通告对话框的环境
            pageData = Util.getPageData();
            if (checkPage("游戏公告")) {
                boolean isInit = SPUtils.getBoolean(CheckName.GAME_NOTICE_BTN_VERSION, false);
                if (!isInit){
                    initPage();
                }
                if (Util.checkColor(gameNoticePoint)){
                    click(gameNoticePoint);
                    LogUtils.logd("step:7 click default");
                }else{
                    clickMid(pageData.get(0).getRect());
                    LogUtils.logd("step:8 click  parse page");
                }
                break;
            } else if (check(8)) {
                resetStep();
                return true;
            }else{
                Thread.sleep(400);
//                boolean isInit = SPUtils.getBoolean(CheckName.GAME_NOTICE_BTN_VERSION, false);
//                if (!isInit){
//                    initPage();
//                }
            }
        }
        Util.sleep(800);
        LogUtils.logd("step:9 wait");
        while (TaskState.isWorking){
            Util.getCapBitmapNew();
            if (FuNeiHelper.huaAn!=null && Util.checkColor(FuNeiHelper.huaAn)){
                break;
            }else if (check(8)){
                LaunchManager.killApp();
                resetStep();
                return true;
            }else{
                FuNeiHelper.init();
                LogUtils.logd("step:10 init");
            }
        }
        LogUtils.logd("step:11 end");
        return true;
    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        for (Result.ItemsBean bean : result) {
            if (equals(bean.getItemstring(),CheckName.START_BTN_NAME)){
                // 重新设置坐标信息
                setNewCoord(startPoint,bean.getItemcoord());
                SPUtils.setBoolean(CheckName.START_BTN_VERSION,true);
                needSaveCoord = true;
                return;
            }else if (equals(GAME_NOTICE_BTN_NAME,bean.getItemstring())){
                SPUtils.setBoolean(CheckName.GAME_NOTICE_BTN_VERSION,true);
                gameNoticePoint.setY(bean.getItemcoord().getY()+bean.getItemcoord().getHeight()-10);
                gameNoticePoint.setX((int) (BaseApplication.getScreenWidth()*0.9065));
                gameNoticePoint.setNormalColor(Util.getColor(gameNoticePoint));
                needSaveCoord = true;
            }
        }
    }
}
