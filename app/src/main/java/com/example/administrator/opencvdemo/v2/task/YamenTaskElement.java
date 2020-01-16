package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.MenKeFenShuHelper;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;

/**
 * 领取邮件
 */
public class YamenTaskElement extends AbsTaskElement {
    public YamenTaskElement(TaskModel taskModel) {
        super(taskModel);
    }
    private boolean isSwipe;

    @Override
    protected boolean doTaskBefore() {
        isSwipe = false;
        return super.doTaskBefore();
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) {
            return false;//检查网络环境
        }
        PointModel index = PointManagerV2.get(Constant.YA_MEN_INDEX);
        if (Util.checkColorAndClick(index) || Util.checkColorAndClick(FuWaiHelper.yaMen,"#4F3A38")){

        }else if (checkPage("府内")) {
            PointManagerV2.execShellCmdChuFuV2();
            Thread.sleep(1800);
            return false;
        } else if (checkPage("府外")) {
            if (!isSwipe){
                swipeToRight();
                isSwipe = true;
            }
            while (TaskState.isWorking){
                Util.getCapBitmapNew();
                if (Util.checkColorAndClick(index)){
                    break;
                }else if (Util.checkColorAndClick(FuWaiHelper.yaMen,"#4F3A38")) {
                    break;
                } else if (check(4)) {
                    FuWaiHelper.paiHangBangInit();
                }else if (check(15)){
                    return true;
                }
                Thread.sleep(800);
            }
           // return false;
        }
        PointModel wait = PointManagerV2.get(Constant.YA_MEN_WAIT);
        PointModel zhunZou = PointManagerV2.get(Constant.YA_MEN_ZHUN_ZOU);
        PointModel pkVs = PointManagerV2.get(Constant.YA_MEN_PK_VS);
        PointModel unBuyTemp = PointManagerV2.get(Constant.YA_MEN_PK_UN_BUY);
        PointModel tempDialogHide = PointManagerV2.get(Constant.YA_MEN_TEMP_DIALOG_HIDE);
        PointModel huangGongClose = PointManagerV2.get(HUANG_GONG_CLOSE);
        PointModel dialogClose = PointManagerV2.get(YA_MEN_TEMP_DIALOG_CLOSE);
        PointModel skip = PointManagerV2.get(YA_MEN_CHOOSE_SKIP);
        PointModel lianSheng = PointManagerV2.get(YA_MEN_LIAN_SHENG);
        PointModel end = PointManagerV2.get(YA_MEN_END);
        Thread.sleep(800);
        while (TaskState.isWorking) {
            Util.getCapBitmapNew();
            if (Util.checkColorAndClick(index) || Util.checkColorAndClick(zhunZou)) {

            } else if (Util.checkColorAndClick(pkVs)) {
                break;
            } else if (Util.checkColor(wait)) {

                click(huangGongClose);
                Util.sleep(800);
                //结束中
                PointManagerV2.execShellCmdChuFuV2();
                Util.sleep(800);
                return true;
            }
            Util.sleep(1200);
        }
        boolean isCheckUnBuy = false;
        int isHideDialog = 0;
        int tryCount = 0;
        while (TaskState.isWorking) {
            Util.getCapBitmapNew();
            //吃豆算法需要更新
            if (isHideDialog<2 && Util.checkColorAndClick(tempDialogHide)) {
                Util.sleep(200);
                click(dialogClose);
                Util.sleep(800);
                isHideDialog++;
                // isHideDialog = true;
            } else if (!isCheckUnBuy && Util.checkColorAndClick(unBuyTemp)) {
                Util.sleep(800);
                isCheckUnBuy = true;
            } else if (Util.checkColorAndClick(pkVs)) {
                PointModel pkModel = MenKeFenShuHelper.getInstance().getPkModel();
                if (pkModel == null) {
                    //结束了
                    click(huangGongClose);
                    Util.sleep(800);
                    //结束中
                    PointManagerV2.execShellCmdChuFuV2();
                    Util.sleep(800);
                    break;
                } else {
                    tryCount = 0;
                    click(pkModel);
                    Util.sleep(600);
                    click(skip);
                    Util.sleep(800);
                }
            } else if (Util.checkColorAndClick(lianSheng)) {
                Util.sleep(600);
            }else if (Util.checkColorAndClick(skip)) {
                Util.sleep(600);
            } else if (Util.checkColorAndClick(end)) {
                Util.sleep(400);
                click(huangGongClose);
                Util.sleep(800);
                //结束中
                PointManagerV2.execShellCmdChuFuV2();
                Util.sleep(800);
                break;
            } else if (tryCount>20){
                return true;
            }else{
                tryCount ++;
                click(pkVs);
                Util.sleep(600);
            }
        }
        return true;
    }
}
