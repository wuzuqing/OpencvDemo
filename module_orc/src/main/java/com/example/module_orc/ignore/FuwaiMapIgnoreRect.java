package com.example.module_orc.ignore;

import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：士元
 * 时间：2019/9/16 18:07
 * 邮箱：wuzuqing@linghit.com
 * 说明：府外
 */
public class FuwaiMapIgnoreRect implements IIgnoreRect {
    public static  Rect huangGongIndex = new Rect(134, 366, 93, 20);
    public static  Rect shuYuan = new Rect(134, 366, 93, 20);
    public static  Rect guoJia = new Rect(134, 366, 93, 20);

    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        for (Rect rect : rects) {
            if (rect.width == 15 && rect.height == 15){
                // 皇宫入口
                huangGongIndex = rect;
                result.add(OrcConfig.append(rect));
            }else if (rect.width == 28 &&rect.height == 23){
                //国家
                guoJia = rect;
                result.add(OrcConfig.append(rect));
            }else if (rect.width == 145 && rect.height == 36){
                //书院
                shuYuan = rect;
                result.add(OrcConfig.append(rect));
            }
        }
//        result.add(OrcConfig.append(loginGame));
        return result;
    }
}
