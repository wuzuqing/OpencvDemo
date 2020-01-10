package com.example.administrator.opencvdemo.v2;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.util.GsonUtils;

import java.util.List;

import static com.example.administrator.opencvdemo.util.Constant.COORDINATE_KEY;

public class FuNeiHelper {
    public static PointModel huaAn = PointManagerV2.get(Constant.HUA_AN);
    public static PointModel shiYe = PointManagerV2.get(Constant.SHI_YE);
    public static PointModel Hongyzj= PointManagerV2.get(Constant.SHI_JI_ZHAO_HUAN);
    public static PointModel bottomMeiKe= PointManagerV2.get(Constant.BOTTOM_MK);
    public static PointModel bottomDaoju= PointManagerV2.get(Constant.BOTTOM_DJ);
    public static PointModel bottomRenwu= PointManagerV2.get(Constant.BOTTOM_RW);
    public static PointModel bottomChengJi= PointManagerV2.get(Constant.BOTTOM_CJ);
    public static PointModel bottomShangCheng= PointManagerV2.get(Constant.BOTTOM_SC);
    public static PointModel bottomFuli= PointManagerV2.get(Constant.BOTTOM_FL);
    public static boolean isIniting;
    private static boolean hasChange;
    protected static void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord) {
        Util.setNewCoord(model,coord);
        hasChange = true;
    }

    public static void init() {
       if (isIniting) {
           return;
       }
       boolean isInit = SPUtils.getBoolean(CheckName. FU_NEI, false);
       if (isInit) {
           return;
       }
        isIniting = true;
        hasChange = false;
        List<Result.ItemsBean> itemCoord = HttpManager.getItemCoord("fu_nei");
        if (itemCoord!=null && itemCoord.size()>0){
            forCoordList(itemCoord);
            SPUtils.setBoolean(CheckName.FU_NEI, true);
            isIniting = false;
            return;
        }
        ImageParse.getSyncData( new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) return;
                try {
                    forCoordList(result);
                    if (hasChange){
                        HttpManager.updatePageData("fu_nei", GsonUtils.toJson(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PointManagerV2.saveCoordinate();
                SPUtils.setBoolean(CheckName.FU_NEI, true);
                isIniting = false;
            }
        });
    }

    private static void forCoordList(List<Result.ItemsBean> result) {
        for (Result.ItemsBean itemsBean : result) {
            switch (itemsBean.getItemstring()) {
                case "出府":
                    PointModel chuFu = PointManagerV2.get("CHU_FU");
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
    }

    public static void resetInit() {
        isIniting = false;
        SPUtils.getBoolean(CheckName.FU_NEI, false);
        init();
    }
}
