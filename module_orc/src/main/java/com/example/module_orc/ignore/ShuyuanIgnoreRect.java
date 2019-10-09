package com.example.module_orc.ignore;

import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

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
            if ( rect.height==14 && ( rect.width==81 || rect.width==80)){
                rect.y -=24;
                result.add(OrcConfig.append(rect));
            }
        }
        return result;
    }
}
