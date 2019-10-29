package com.example.administrator.opencvdemo.util;

import android.graphics.Bitmap;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;

import java.util.List;

import static com.example.module_orc.WorkMode.ONLY_BITMAP;

public class Test {
    public static void test(Bitmap bitmap){
        final Bitmap finalBitmap = bitmap;
        OrcHelper.getInstance().executeCallAsync(ONLY_BITMAP, bitmap, "zwp", "test", new IDiscernCallback() {
            @Override
            public void call(final List<OrcModel> result) {
                if (result.size()>0){
                    OrcModel orcModel = result.get(0);
                    AutoTool.execShellCmd(orcModel.getRect());

                }
            }
        });

//        HandlerUtil.async(new Runnable() {
//            @Override
//            public void run() {
//                ImageParse.getSyncData(finalBitmap,new ImageParse.Call() {
//                    @Override
//                    public void call(List<Result.ItemsBean> result) {
//                        if (result == null || result.size() == 0) return;
//                        try {
//                            for (Result.ItemsBean itemsBean : result) {
//                                LogUtils.logd("JyzcTaskElement:"+itemsBean.getItemstring());
//                                if (TextUtils.equals(itemsBean.getItemstring(),"经营")){
//
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        SPUtils.setBoolean("jpzc_init",false);
//                    }
//                });
//            }
//        });
    }

    public static void testWork(){
        HandlerUtil.async(new Runnable() {
            @Override
            public void run() {
                EventHelper.swipeHor(BaseApplication.getScreenWidth() - 50, 100,600);
                try {
                    Thread.sleep(800);
                    EventHelper.swipeHor(BaseApplication.getScreenWidth() - 50, 100,600);
                    Thread.sleep(800);
                    EventHelper.swipeHor(240, 800,600);
                    Thread.sleep(3200);
                    Util.getCapBitmapNew();
                    FuWaiHelper.resetInit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
