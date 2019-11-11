package com.example.administrator.opencvdemo.v2.task;

import android.text.TextUtils;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuNeiHelper;
import com.example.module_orc.OrcModel;
import com.example.module_orc.util.GsonUtils;
import com.google.gson.reflect.TypeToken;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

public class JyzcTaskElement extends AbsTaskElement {
    private boolean needClickZhengshou = true;
    private boolean isEnd;
    private boolean isFristInitPoint;
    List<PointModel> coordinateList;

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
    protected void doTaskBefore() {
        isCountOne = 0;
        isInJycz =false;
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (isInJycz){
            if (checkPage("道具使用")) {
                AutoTool.execShellCmd(pageData.get(0).getRect());
                Thread.sleep(800);
                return false;
            } else{
                if (isFristInitPoint) {
                    initPage();
                }
                if (needClickZhengshou) {
                    AutoTool.execShellCmd(CmdData.get(ZHENG_SHOU));
                    Thread.sleep(800);
                    needClickZhengshou = false;
                    return false;
                }
                if (isEnd) {
                    clickClose();
                    Thread.sleep(1000);
                    return true;
                }
                int count = 0;

//                if (!coordinateList.isEmpty()) {
//                    for (PointModel model : coordinateList) {
//                        if (Util.checkColor(model)) {
//                            EventHelper.click(model.getX(), model.getY());
//                            Thread.sleep(200);
//                            count++;
//                        }
//                    }
//                } else
                    {
                    for (OrcModel orcModel : pageData) {
                        if (TextUtils.equals("经营", orcModel.getResult())) {
                            Rect rect = orcModel.getRect();
                            AutoTool.execShellCmdXy(rect.x, rect.y);
                            Thread.sleep(200);
                            count++;
                        }
                    }
                }
                if (count == 1) {
                    isCountOne++;
                }
                Thread.sleep(400);
                isEnd = count == 0 || isCountOne > 3;
                LogUtils.logd(" count" + count);
            }
        }else if (FuNeiHelper.huaAn != null && Util.checkColor(FuNeiHelper.huaAn)) {
            AutoTool.execShellCmd(FuNeiHelper.huaAn);
            isEnd = false;
            Thread.sleep(1000);
        } else if (checkPage("府内")) {
//            FuNeiHelper.init();
            AutoTool.execShellCmd(pageData.get(0).getRect());
            isEnd = false;
            Thread.sleep(1000);
            return false;
        } else if (!checkPage("经营资产")) {
            if (check(12)) {
                resetStep();
                return true;
            }
            Thread.sleep(300);
            return false;
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
                model.setNormalColor(Util.getColor(ScreenCapture.get().getCurrentBitmap(), model.getX(), model.getY()));
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
