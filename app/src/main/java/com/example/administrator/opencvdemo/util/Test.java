package com.example.administrator.opencvdemo.util;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.youtu.ImageParse;

import java.util.List;

public class Test {
    public static void test(Bitmap bitmap){
        final Bitmap finalBitmap = bitmap;
        HandlerUtil.async(new Runnable() {
            @Override
            public void run() {
                ImageParse.getSyncData(finalBitmap,new ImageParse.Call() {
                    @Override
                    public void call(List<Result.ItemsBean> result) {
                        if (result == null || result.size() == 0) return;
                        try {
                            for (Result.ItemsBean itemsBean : result) {
                                LogUtils.logd("JyzcTaskElement:"+itemsBean.getItemstring());
                                if (TextUtils.equals(itemsBean.getItemstring(),"经营")){

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SPUtils.setBoolean("jpzc_init",false);
                    }
                });
            }
        });
    }

    public static void testWork(){
        HandlerUtil.async(new Runnable() {
            @Override
            public void run() {
                try {
                    PointModel paiHang = CmdData.get(Constant. PAI_HANG_BANG);
                    AutoTool.execShellCmdChuFu();
                    Thread.sleep(800);
                    EventHelper.swipeHor(BaseApplication.getScreenWidth() - 50, 100,600);
                    Thread.sleep(800);
                    EventHelper.swipeHor(BaseApplication.getScreenWidth() - 50, 100,600);
                    Thread.sleep(800);
                    EventHelper.swipeHor(240, 800,600);
                    Thread.sleep(1600);
                    AutoTool.execShellCmd(paiHang);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
