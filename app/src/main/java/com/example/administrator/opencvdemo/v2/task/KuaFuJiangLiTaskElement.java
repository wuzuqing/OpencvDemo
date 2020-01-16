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
public class KuaFuJiangLiTaskElement extends AbsHuoDongElement {

    private PointModel kaFuIndex = PointManagerV2.get(Constant.KUAFU_INDEX);
    private PointModel kuaFuEnter = PointManagerV2.get(Constant.KUAFU_ENTER);
    private PointModel yiLingQU = PointManagerV2.get(Constant.KUAFU_YILINGQU);
    private PointModel quFuTab = PointManagerV2.get(Constant.KUAFU_QUFU);
    private PointModel kuaFuDialogClose = PointManagerV2.get(Constant.KUAFU_DIALOG_CLOSE);

    public KuaFuJiangLiTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTaskBefore() {
        // 0-5
        row = 1;
        col = 1;
        return super.doTaskBefore();
    }

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
        } else if (Util.checkColor(kaFuIndex)) {
            int count = 0;
            if (!Util.checkColor(kuaFuEnter)) { // F6EADD
                click(kuaFuEnter);
               sleep(1400);
            } else {
                return false;
            }

            while (true) {
                Util.getCapBitmapNew();
                if (Util.checkColor(yiLingQU)) {
                    click(quFuTab);
                    count++;
                } else {
                    count++;
                    click(yiLingQU);
                   sleep(800);
                    click(yiLingQU);
                }
                if (count == 2) {
                    break;
                }
               sleep(800);
            }
            PointModel huangGongClose = PointManagerV2.get(HUANG_GONG_CLOSE);
            click(kuaFuDialogClose);
           sleep(800);
            click(huangGongClose);
            // PointManagerV2.execShellCmdChuFuV2();
           sleep(800);
            return true;
        } else if (check(6)) {
            return true;
        }
        return true;
    }
}
