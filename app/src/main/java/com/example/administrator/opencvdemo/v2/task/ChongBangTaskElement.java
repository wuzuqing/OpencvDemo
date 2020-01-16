package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsHuoDongElement;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;

/**
 * 跨服奖励
 */
public class ChongBangTaskElement extends AbsHuoDongElement {
    private PointModel cbIndex = PointManagerV2.get(Constant.CHONG_BANG_INDEX);
    private PointModel cbEnter1 = PointManagerV2.get(Constant.CHONG_BANG_ENTER_1);
    private PointModel cbEnter2 = PointManagerV2.get(Constant.CHONG_BANG_ENTER_2);
    // private PointModel yiLingQU = PointManagerV2.get(Constant.KUAFU_YILINGQU);
    // private PointModel quFuTab = PointManagerV2.get(Constant.KUAFU_QUFU);
    private PointModel cbClose = PointManagerV2.get(Constant.CHONG_BANG_CLOSE);
    private PointModel kuaFuDialogClose = PointManagerV2.get(Constant.KUAFU_DIALOG_CLOSE);

    public ChongBangTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTaskBefore() {
        // 0-5
        row = 2;
        col = 0;
        return super.doTaskBefore();
    }

    private boolean doFirst;
    private boolean doSecond;

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) {
            return false;//检查网络环境
        } else if (checkPage("府内")) {
            PointModel pointModel = PointManagerV2.getPointModel(row, col);
            //第一排 第二排
            click(pointModel);
           sleep(800);
            return false;
        } else if (checkPage("府外")) {
            PointManagerV2.execShellCmdChuFuV2();
           sleep(800);
            return false;
        } else if (Util.checkColor(cbIndex)) {
            if (!doFirst && !Util.checkColor(cbEnter1)) { // F6EADD
                click(cbEnter1);
               sleep(1400);
                doFirst = true;
                doFirstBang();
            }
            if (!doSecond && !Util.checkColor(cbEnter2)) {
                click(cbEnter2);
               sleep(1400);
                doSecond = true;
                doFirstBang();
               sleep(1000);
                click(cbClose);
            } else {
                return false;
            }
            return true;
        } else if (check(6)) {
            return true;
        }
        return true;
    }

    private void doFirstBang() throws Exception {
       sleep(2200);
        PointManagerV2.execShellCmdChuFuV2();
       sleep(600);
    }
}
