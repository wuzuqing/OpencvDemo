package com.example.administrator.opencvdemo.v2;

import android.text.TextUtils;

import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.ImageParse;

import java.util.List;

public class FuNeiHelper {
    private static final String KEY_POSITION = "funei_init";
    public static PointModel huaAn;
    public static PointModel zhengWu;
    public static PointModel Hongyzj;
    //    public static PointModel huaAn=CmdData.get(Constant.HUA_AN);
//    public static PointModel zhengWu  =CmdData.get(Constant.SHI_YE);
//    public static PointModel Hongyzj=CmdData.get(Constant.SHI_YE);
    public static boolean isIniting;

    static {
        boolean isInit = SPUtils.getBoolean(KEY_POSITION, false);
        if (isInit) {
            huaAn = CmdData.get(Constant.HUA_AN);
            zhengWu = CmdData.get(Constant.SHI_YE);
        }
    }

    public static void init() {
        if (isIniting) {
            return;
        }
        boolean isInit = SPUtils.getBoolean(KEY_POSITION, false);
        if (isInit) {
            return;
        }
        isIniting = true;
        ImageParse.getSyncData(ScreenCapture.get().getCurrentBitmap(), new ImageParse.Call() {
            @Override
            public void call(List<Result.ItemsBean> result) {
                if (result == null || result.size() == 0) return;
                try {
                    for (Result.ItemsBean itemsBean : result) {
                        if (TextUtils.equals("华", itemsBean.getItemstring())) {
                            huaAn = new PointModel("huaan", "华安");
                            huaAn.setX(itemsBean.getItemcoord().getX() + itemsBean.getItemcoord().getWidth() / 2);
                            huaAn.setY(itemsBean.getItemcoord().getY() + itemsBean.getItemcoord().getHeight() / 2);
                            huaAn.setNormalColor(Util.getColor(huaAn));
                        } else if (TextUtils.equals("师新", itemsBean.getItemstring())) {
                            zhengWu = new PointModel("zhengWu", "师爷");
                            zhengWu.setX(itemsBean.getItemcoord().getX() + itemsBean.getItemcoord().getWidth() );
                            zhengWu.setY(itemsBean.getItemcoord().getY() + itemsBean.getItemcoord().getHeight() / 2);
                            zhengWu.setNormalColor(Util.getColor(zhengWu));
                        } else if (TextUtils.equals("颜", itemsBean.getItemstring())) {
                            Hongyzj = new PointModel("hongyan", "红颜");
                            Hongyzj.setX(itemsBean.getItemcoord().getX() + itemsBean.getItemcoord().getWidth()/2);
                            Hongyzj.setY(itemsBean.getItemcoord().getY() + itemsBean.getItemcoord().getHeight() / 2);
                            Hongyzj.setNormalColor(Util.getColor(Hongyzj));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SPUtils.setBoolean(KEY_POSITION, true);
                isIniting = false;
            }
        });
    }

    public static void save(){
        CmdData.get(Constant.HUA_AN) .reset(huaAn);
        CmdData.get(Constant.SHI_YE) .reset(zhengWu);
    }
}
