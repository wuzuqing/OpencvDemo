package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;

import java.util.List;


/**
 * 限时奖励
 */
public class XsjlTaskElement extends AbsTaskElement {
    public XsjlTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {

            if (Util.checkColorAndClick(FuWaiHelper.youJian)) {
                sleep(800);
                //收邮件
                PointModel close = PointManagerV2.get(EMAIL_DIALOG_CLOSE);
                PointModel emailOk = PointManagerV2.get(EMAIL_OK);
                List<PointModel> emails = PointManagerV2.getEmail();
                int count;
                while (true) {
                    Util.getCapBitmapNew();
                    count = 0;
                    for (int i = 0; i < emails.size(); i++) {
                        PointModel email = emails.get(i);
                        if (!Util.checkColor(email)) {
                            count++;
                            click(email);
                            sleep(800);
                            click(emailOk);
                            sleep(1200);
                            click(close);
                            sleep(800);
                        }
                    }
                    if (checkExp(netPoint, "当前网络异常")) {
                        continue;
                    }
                    click(close);
                    sleep(800);
                    if (count == 0 || count<emails.size()) {
                        break;
                    }
//                    exCount++;
//                    if (exCount>5){
//                        break;
//                    }
                    click(FuWaiHelper.youJian);
                    sleep(800);
                }
                PointManagerV2.execShellCmdChuFuV2();
//                clickMid(pageData.get(0).getRect());
                return true;
            } else if (check(4)) {
                return true;
            }


            sleep(1800);
            return false;
        } else if (checkPage("府外")) {
            PointManagerV2.execShellCmdChuFuV2();
            sleep(800);
            return false;
        }
        return true;
    }
}
