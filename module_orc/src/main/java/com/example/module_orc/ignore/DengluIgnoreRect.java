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
 * 说明：登录
 */
public class DengluIgnoreRect implements IIgnoreRect {
    public static final Rect loginGame = new Rect(134, 366, 93, 20);
    public static final Rect loginGame1 = new Rect(135, 366, 91, 21);
    public static final Rect loginGame2 = new Rect(135, 366, 91, 21);

    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        for (Rect rect : rects) {
            if (rect.y>200){
                result.add(OrcConfig.append(rect));
            }
        }
        return result;
    }
}
