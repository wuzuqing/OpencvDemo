package com.example.administrator.opencvdemo.v2.task;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsHuoDongElement;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;

/**
 * 小榜
 */
public class MenKeLeiTaiTaskElement extends AbsHuoDongElement {
    private PointModel yiLingQU = PointManagerV2.get(Constant.LEI_TAI_LINGQU);
    private PointModel duiWu = PointManagerV2.get(Constant.LEI_TAI_DUIWU_JL);

    public MenKeLeiTaiTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTaskBefore() {
        // 0-5
        row = 1;
        col = 2;
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
            sleep(1400);
            return false;
        } else if (checkPage("府外")) {
            PointManagerV2.execShellCmdChuFuV2();
            sleep(800);
            return false;
        } else if (Util.checkColor(duiWu) || Util.checkColor(yiLingQU)) {
            int count = 0;
            while (true) {
                Util.getCapBitmapNew();
                if (Util.checkColor(yiLingQU)) {
                    click(duiWu);
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
            PointManagerV2.execShellCmdChuFuV2();
            sleep(800);
            return true;
        } else if (check(6)) {
            return true;
        }
        return true;
    }
}
