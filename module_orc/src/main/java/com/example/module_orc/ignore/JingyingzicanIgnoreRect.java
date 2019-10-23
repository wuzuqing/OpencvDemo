package com.example.module_orc.ignore;

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
            if (rect.contains(Zican.shangCanNum) || rect.contains(Zican.nongCanNum) || rect.contains(Zican.shiBingNum)) {
                rect.width /= 2;
                result.add(OrcConfig.append(rect, 3, 3, 0, 5));
            } else if (rect.contains(Zican.shangCan) || rect.contains(Zican.nongCan) || rect.contains(Zican.shiBing)) {
                result.add(OrcConfig.append(rect, 15, 2, 30, 10));
            }
        }
        return result;
    }

    public interface Zican {
        Point shangCanNum = new Point(25, 122);
        Point shangCan = new Point(242, 239);
        Point nongCanNum = new Point(25, 289);
        Point nongCan = new Point(242, 406);
        Point shiBingNum = new Point(25, 457);
        Point shiBing = new Point(242, 574);
    }
}
