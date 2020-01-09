package com.example.administrator.opencvdemo.v2.task;

import android.text.TextUtils;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.module_orc.OrcModel;
import com.example.module_orc.util.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JyzcTaskElement extends AbsTaskElement {
    private boolean needClickZhengshou = true;
    private boolean isEnd;
    private boolean isFristInitPoint;
    List<PointModel> coordinateList;
    boolean usedDefault = false;

    public JyzcTaskElement(TaskModel taskModel) {
        super(taskModel);
        isFristInitPoint = SPUtils.getBoolean("jpzc_init", true);
        String jyzcModel = SPUtils.getString("jyzcModel");

        if (!TextUtils.isEmpty(jyzcModel)) {
            coordinateList = (List<PointModel>) JsonUtils.fromJson(jyzcModel,
                    new TypeToken<List<PointModel>>() {
                    }.getType());
        } else {
            coordinateList = new ArrayList<>();
        }
    }

    private int isCountOne;
    private boolean isInJycz;

    @Override
    protected boolean doTaskBefore() {
        isCountOne = 0;
        usedDefault = false;
        isInJycz = false;
        return true;
    }

    @Override
    protected boolean doTask() throws Exception {


        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境
        while (TaskState.isWorking && !isInJycz) {
            LogUtils.logd("step:1 cap screen");
            Util.getCapBitmapWithOffset();
            if (FuNeiHelper.huaAn != null && Util.checkColor(FuNeiHelper.huaAn)) {
                click(FuNeiHelper.huaAn);
                LogUtils.logd("step:2 click huaAn net");
                isEnd = false;
                isInJycz = true;
                break;
            }
            pageData = Util.getPageData();
            if (checkPage("府内")) {
                LogUtils.logd("step:2 click huaAn parse page");
                clickMid(pageData.get(0).getRect());
                isEnd = false;
                isInJycz = true;
            } else if (check(12)) {
                resetStep();
                return true;
            }
        }
        Thread.sleep(1000);
        while (TaskState.isWorking && isInJycz) {
            LogUtils.logd("step:3 cap screen");
            Util.getCapBitmapWithOffset();
            pageData = Util.getPageData();
            if (checkPage("道具使用")) {
                LogUtils.logd("step:4 click daoju");
                clickMid(pageData.get(0).getRect());
                Thread.sleep(800);
                continue;
            }
            if (needClickZhengshou) {
                click(PointManagerV2.get(JYZC_ZS));
                Thread.sleep(800);
                needClickZhengshou = false;
                continue;
            }
            if (isEnd) {
                clickClose();
                Thread.sleep(1000);
                return true;
            }
            int count = 0;
            List<PointModel> data = mTaskModel.getData();

            if (!usedDefault && data != null && !data.isEmpty()) {
                PointModel model = data.get(0);
                if (Util.checkColorAndOffset(model)) {
                    usedDefault = true;
                }
            }
            if (usedDefault) {
                for (PointModel model : data) {
                    if (Util.checkColorAndOffset(model)) {
                        click(model);
                        count++;
                        Thread.sleep(240);
                    }
                }
            } else {
               if (isFristInitPoint) {
                    LogUtils.logd("step:7 init net");
                    initPage();
                }
                if (!coordinateList.isEmpty()) {
                    LogUtils.logd("step:5 click get  net ");
                    for (PointModel model : coordinateList) {
                        if (Util.checkColor(model)) {
                            EventHelper.click(model.getX(), model.getY());
                            Thread.sleep(240);
                            count++;
                        }
                    }
                } else {
                    LogUtils.logd("step:6  click get parse page");
                    for (OrcModel orcModel : pageData) {
                        if (TextUtils.equals("经营", orcModel.getResult())) {
                            click(orcModel.getRect());
                            Thread.sleep(240);
                            count++;
                        }
                    }
                }
            }
            if (count == 1) {
                isCountOne++;
            }
            Thread.sleep(800);
            isEnd = count == 0 || isCountOne > 3;
            LogUtils.logd(" count" + count);
        }
        return false;
    }

    @Override
    protected void callBack(List<Result.ItemsBean> result) {
        List<PointModel> jyzcModel = new ArrayList<>();
        int index = 0;
        for (Result.ItemsBean itemsBean : result) {
            LogUtils.logd("JyzcTaskElement:" + itemsBean.getItemstring());
            if (TextUtils.equals(itemsBean.getItemstring(), "经营")) {
                PointModel model = new PointModel(String.valueOf(index), "经营");
                model.setX(itemsBean.getItemcoord().getX() + itemsBean.getItemcoord().getWidth() / 2);
                model.setY(itemsBean.getItemcoord().getY() + itemsBean.getItemcoord().getHeight() / 2);
                model.setNormalColor(Util.getColor( model.getX(), model.getY()));
                jyzcModel.add(model);
                index++;
            }
        }
        SPUtils.setString("jyzcModel", GsonUtils.toJson(jyzcModel));
        coordinateList = jyzcModel;
        LogUtils.logd("jyzcModel:" + jyzcModel.toString());
        SPUtils.setBoolean("jpzc_init", false);
        isFristInitPoint = false;
    }
}
