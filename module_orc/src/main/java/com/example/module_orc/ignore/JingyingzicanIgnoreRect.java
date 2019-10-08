package com.example.module_orc.ignore;


import android.util.Log;

import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：士元
 * 时间：2019/9/16 18:07
 * 邮箱：wuzuqing@linghit.com
 * 说明：经营资产
 */
public class JingyingzicanIgnoreRect implements IIgnoreRect {
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        for (Rect rect : rects) {
            Log.d(TAG, "ignoreRect: "+rect.toString());
            if (rect.contains(Zican.shangCanNum)) {
                rect.width /=2;
                result.add(OrcConfig.append(rect));
            } else if (rect.contains(Zican.nongCanNum)) {
                rect.width /=2;
               result.add(OrcConfig.append(rect));
            } else if (rect.contains(Zican.shiBingNum)) {
                rect.width /=2;
               result.add(OrcConfig.append(rect));
            } else if (rect.contains(Zican.shangCan)) {
               result.add(OrcConfig.append(rect));
            } else if (rect.contains(Zican.nongCan)) {
               result.add(OrcConfig.append(rect));
            } else if (rect.contains(Zican.shiBing)) {
               result.add(OrcConfig.append(rect));
            }
        }
        return result;
    }
    public interface Zican {
//        Rect shangCanNum = new Rect(new double[]{25,122,100,16});
//        Rect shangCan = new Rect(new double[]{242,239,60,12});
//        Rect nongCanNum = new Rect(new double[]{25,122,100,16});
//        Rect nongCan =  new Rect(new double[]{242,406,60,12});
//        Rect shiBingNum = new Rect(new double[]{25,122,100,16});
//        Rect shiBing =new Rect(new double[]{242,574,100,16});
        Point shangCanNum = new Point(25,122);
        Point shangCan = new Point(242,239);
        Point nongCanNum = new Point(25,289);
        Point nongCan = new Point(242,406);
        Point shiBingNum = new Point(25,457);
        Point shiBing = new Point(242,574);
    }

    private static final String TAG = "JingyingzicanIgnoreRect";
}
