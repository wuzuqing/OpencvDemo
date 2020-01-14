package com.example.administrator.opencvdemo.v2;

import com.example.administrator.opencvdemo.config.CheckName;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.util.GsonUtils;

import java.util.List;
import android.text.TextUtils;

public class FuWaiHelper {
    public static PointModel shuYuan = PointManagerV2.get(Constant.ACADEMY);
    public static PointModel huangGong = PointManagerV2.get(Constant.HUANG_GONG);
    public static PointModel guoJia= PointManagerV2.get(Constant.GUO_JIA);
    public static PointModel paiHangBang= PointManagerV2.get(Constant.PAI_HANG_BANG);
    public static PointModel laoFang= PointManagerV2.get(Constant.LAO_FANG_MAP);
    public static PointModel lianMeng= PointManagerV2.get(Constant.LIAN_MENG);
    public static PointModel yaMen= PointManagerV2.get(Constant.YA_MEN_MAP);
    public static PointModel youJian= PointManagerV2.get(Constant.EMAIL_RED);

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
        boolean isInit = SPUtils.getBoolean(CheckName. FU_WAI, false);
        if (isInit) {
            return;
        }
        isIniting = true;
        reqNet("fu_wai_center");
    }

    public static void paiHangBangInit(){
        boolean isInit = SPUtils.getBoolean(CheckName. FU_WAI_PAI_HANG_BANG, false);
        if (isInit) {
            return;
        }
        reqNet("fu_wai_right");
    }

    private static void reqNet(final String page) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hasChange = false;
        // List<Result.ItemsBean> itemCoord = HttpManager.getItemCoord(page);
        // if (itemCoord!=null && itemCoord.size()>0){
        //     if (TextUtils.equals(page,"fu_wai_center")){
        //         forCoordList(itemCoord);
        //     }else if (TextUtils.equals(page,"fu_wai_right")){
        //         forCoordList1(itemCoord);
        //     }
        //     isIniting = false;
        //     return;
        // }
        Util.getCapBitmapNew();
        ImageParse.getSyncData(  new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) return;
                try {
                    if (TextUtils.equals(page,"fu_wai_center")){
                      if (forCoordList(result)){
                          SPUtils.setBoolean(CheckName.FU_WAI, true);
                      }
                    }else if (TextUtils.equals(page,"fu_wai_right")){
                        if (forCoordList1(result)){
                            SPUtils.setBoolean(CheckName.FU_WAI_PAI_HANG_BANG, true);
                        }
                    }
                    if (hasChange){
                        HttpManager.updatePageData(page, GsonUtils.toJson(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PointManagerV2.saveCoordinate();

                isIniting = false;
            }
        });
    }

    private static boolean forCoordList(List<Result.ItemsBean> result) {
        for (Result.ItemsBean itemsBean : result) {
            switch (itemsBean.getItemstring()) {
                case "院":
                case "广院":
                case "书":
                    setNewCoord(shuYuan, itemsBean.getItemcoord());
                    Result.ItemsBean.ItemcoordBean itemcoord = itemsBean.getItemcoord();
                    itemcoord.setY(itemcoord.getY()-140);
                    setNewCoord(huangGong, itemcoord);
                    break;
                case "国":
                case "家上":
                case "家":
                    setNewCoord(guoJia, itemsBean.getItemcoord());
                    break;
                case "皇":
                    setNewCoord(huangGong, itemsBean.getItemcoord());
                    break;
                case "邮":
                case "件":
                    setNewCoord(youJian, itemsBean.getItemcoord());
                    break;
            }
        }
        return hasChange;
    }


    private static boolean forCoordList1(List<Result.ItemsBean> result) {
        for (Result.ItemsBean itemsBean : result) {
            switch (itemsBean.getItemstring()) {
                case "排":
                case "行":
                    setNewCoord(paiHangBang, itemsBean.getItemcoord());
                    break;
                case "联":
                case "盟":
                    setNewCoord(lianMeng, itemsBean.getItemcoord());
                    break;
                case "牢":
                    setNewCoord(laoFang, itemsBean.getItemcoord());
                    break;
                case "邮":
                case "件":
                    setNewCoord(youJian, itemsBean.getItemcoord());
                    break;
                case "衙":
                case "街":
                    setNewCoord(yaMen, itemsBean.getItemcoord());
                    break;
            }
        }
        return hasChange;
    }
    public static void resetInit() {
        isIniting = false;
        SPUtils.getBoolean(CheckName.FU_NEI, false);
        init();
    }
}
