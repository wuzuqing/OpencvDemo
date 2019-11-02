package com.example.administrator.opencvdemo.v2;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.ImageParse;

import java.util.List;

import static com.example.administrator.opencvdemo.util.Constant.COORDINATE_KEY;

public class FuNeiHelper {
    public static PointModel huaAn = CmdData.get(Constant.HUA_AN);
    public static PointModel shiYe = CmdData.get(Constant.SHI_YE);
    public static PointModel Hongyzj= CmdData.get(Constant.SHI_JI_ZHAO_HUAN);
    public static PointModel bottomMeiKe= CmdData.get(Constant.BOTTOM_MK);
    public static PointModel bottomDaoju= CmdData.get(Constant.BOTTOM_DJ);
    public static PointModel bottomRenwu= CmdData.get(Constant.BOTTOM_RW);
    public static PointModel bottomChengJi= CmdData.get(Constant.BOTTOM_CJ);
    public static PointModel bottomShangCheng= CmdData.get(Constant.BOTTOM_SC);
    public static PointModel bottomFuli= CmdData.get(Constant.BOTTOM_FL);
    public static boolean isIniting;

    protected static void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord) {
        Util.setNewCoord(model,coord);
    }

    public static void init() {
//        if (isIniting) {
//            return;
//        }
//        boolean isInit = SPUtils.getBoolean(CheckName. FU_NEI, false);
//        if (isInit) {
//            return;
//        }
        isIniting = true;
        ImageParse.getSyncData(ScreenCapture.get().getCurrentBitmap(), new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) return;
                try {
                    for (Result.ItemsBean itemsBean : result) {
                        switch (itemsBean.getItemstring()) {
                            case "出府":
                                PointModel chuFu = CmdData.get("CHU_FU");
                                setNewCoord(chuFu, itemsBean.getItemcoord());
                                break;
                            case "华":
                                setNewCoord(huaAn, itemsBean.getItemcoord());
                                break;
                            case "师新":
                            case "师":
                                setNewCoord(shiYe, itemsBean.getItemcoord());
                                break;
                            case "颜":
                                setNewCoord(Hongyzj, itemsBean.getItemcoord());
                                break;
                            case "门客":
                                setNewCoord(bottomMeiKe, itemsBean.getItemcoord());
                                break;
                            case "道具":
                                setNewCoord(bottomDaoju, itemsBean.getItemcoord());
                                break;
                            case "任务":
                                setNewCoord(bottomRenwu, itemsBean.getItemcoord());
                                break;
                            case "成就":
                                setNewCoord(bottomChengJi, itemsBean.getItemcoord());
                                break;
                            case "商城":
                                setNewCoord(bottomShangCheng, itemsBean.getItemcoord());
                                break;
                            case "福利":
                                setNewCoord(bottomFuli, itemsBean.getItemcoord());
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                save();
                SPUtils.setBoolean(CheckName.FU_NEI, true);
                isIniting = false;
            }
        });
    }

    public static void save() {
        String jsonList = JsonUtils.toJson(CmdData.coordinateList);
        SPUtils.setString(COORDINATE_KEY, jsonList);
    }

    public static void resetInit() {
        isIniting = false;
        SPUtils.getBoolean(CheckName.FU_NEI, false);
        init();
    }
}
