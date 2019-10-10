package com.example.module_orc.ignore;

import android.util.Log;

import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：士元
 * 时间：2019/9/16 18:07
 * 邮箱：wuzuqing@linghit.com
 * 说明：书院
 */
public class ShuyuanIgnoreRect implements IIgnoreRect {
    private static final String TAG = "ShuyuanIgnoreRect";
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        result.add(OrcConfig.append(new Rect(284,129,74,16)));
        for (Rect rect : rects) {
            Log.d(TAG, "ignoreRect: "+rect.toString());
            if (rect.y == 617 || rect.y == 459 || rect.y ==301){
                rect.y -=24;
                result.add(OrcConfig.append(rect));
            }else if ( rect.height==14 && ( rect.width==81 || rect.width==80) ){
                rect.y -=24;
                result.add(OrcConfig.append(rect));
            }else if (rect.x == 43 && (rect.width>95 && rect.width<115) ){
                rect.height -=15;
                result.add(OrcConfig.append(rect));
            }else if (rect.x == 220 && (rect.width>95 && rect.width<115) ){
                rect.height -=4;
                result.add(OrcConfig.append(rect));
            }
        }
        return result;
    }
}
