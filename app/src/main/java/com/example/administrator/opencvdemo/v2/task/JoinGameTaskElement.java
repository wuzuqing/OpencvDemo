package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;

import java.util.List;

import static com.example.administrator.opencvdemo.config.CheckName.GAME_NOTICE_BTN_NAME;


public class JoinGameTaskElement extends AbsTaskElement {
    private PointModel startPoint = CmdData.get(START_GAME);
    private PointModel gameNoticePoint = CmdData.get(DIALOG_CLOSE2);
    public JoinGameTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        // 检查是否有更新
        PointModel pointModel = CmdData.get(LOGIN_GAME);
        while (TaskState.isWorking) {
            Util.getCapBitmapNew();
            if (Util.checkColor(startPoint)){
                AutoTool.execShellCmd(startPoint);
                Thread.sleep(1200);
                break;
            }
            pageData = Util.getPageData();
            if (checkPage("进入游戏")) {
                setNewCoord(startPoint,pageData.get(0).getRect());
                AutoTool.execShellCmd(pageData.get(0).getRect());  //进入游戏
                Thread.sleep(1200);
                break;
            }else if (Util.checkColor(pointModel)){
                AutoTool.execShellCmd(pointModel);  //进入游戏
                Thread.sleep(1200);
            }else  if (checkPage("登录")) {
                AutoTool.execShellCmd(pageData.get(0).getRect()); //点击登录
                Thread.sleep(1200);
                break;
            }else if (check(8)) {
                AutoTool.killApp();
                resetStep();
                return true;
            }else{
                boolean isInit = SPUtils.getBoolean(CheckName.START_BTN_VERSION, false);
                if (!isInit){
//                    SPUtils.getBoolean(CheckName.START_BTN_VERSION, true);
                    initPage();
                }
            }
            TaskUtil.sleep(400);
        }

        while (TaskState.isWorking) {
            Util.getCapBitmapNew();
            if (Util.checkColor(gameNoticePoint)){
                AutoTool.execShellCmd(gameNoticePoint);
                break;
            }
            //检查 通告对话框的环境
            pageData = Util.getPageData();
            if (checkPage("游戏公告")) {
                boolean isInit = SPUtils.getBoolean(CheckName.GAME_NOTICE_BTN_VERSION, false);
                if (!isInit){
                    initPage();
                }
                AutoTool.execShellCmdXy(pageData.get(0).getRect().x,pageData.get(0).getRect().y );  //关闭通告对话框
                break;
            } else if (check(8)) {
                resetStep();
                return true;
            }else{
                boolean isInit = SPUtils.getBoolean(CheckName.GAME_NOTICE_BTN_VERSION, false);
                if (!isInit){
                    initPage();
                }
            }
        }
        TaskUtil.sleep(1200);
        while (TaskState.isWorking){
            Util.getCapBitmapNew();
            if (FuNeiHelper.huaAn!=null && Util.checkColor(FuNeiHelper.huaAn)){
                break;
            }else if (check(8)){
                AutoTool.killApp();
                resetStep();
                return true;
            }else{
                FuNeiHelper.init();
            }
        }
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
