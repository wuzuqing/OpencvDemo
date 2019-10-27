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

public class FuWaiHelper {
    public static PointModel shuYuan = CmdData.get(Constant.ACADEMY);
    public static PointModel huangGong = CmdData.get(Constant.HUANG_GONG);
    public static PointModel guoJia= CmdData.get(Constant.GUO_JIA);
    public static PointModel paiHangBang= CmdData.get(Constant.PAI_HANG_BANG);
    public static PointModel laoFang= CmdData.get(Constant.LAO_FANG_MAP);
    public static PointModel lianMeng= CmdData.get(Constant.LIAN_MENG);
    public static PointModel yaMen= CmdData.get(Constant.YA_MEN_MAP);
    public static PointModel youJian= CmdData.get(Constant.EMAIL_RED);

    public static boolean isIniting;

    protected static void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord) {
        Util.setNewCoord(model,coord);
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
        SPUtils.setBoolean(CheckName.FU_WAI, true);
        reqNet();
    }

    public static void paiHangBangInit(){
        boolean isInit = SPUtils.getBoolean(CheckName. FU_WAI_PAI_HANG_BANG, false);
        if (isInit) {
            return;
        }
        SPUtils.setBoolean(CheckName.FU_WAI_PAI_HANG_BANG, true);
        reqNet();
    }

    private static void reqNet() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ImageParse.getSyncData(ScreenCapture.get().getCurrentBitmap(), new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) return;
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                save();

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
