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
 * 说明：俸禄
 */
public class HuanggongFengluIgnoreRect implements IIgnoreRect {
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        Rect rect1 = new Rect(220, 595, 132, 27);
        List<OrcModel> result = new ArrayList<>();
        for (Rect r : rects) {
            if (rect1.equals(r)) {
                result.add(OrcConfig.append(r));
            }
        }
        return result;
    }
}
