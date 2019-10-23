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
 * 说明：处理公务
 */
public class ChuligongwuIgnoreRect implements IIgnoreRect {
  public  static Point zhengwuNum = new Point(250, 216);
    public  static Point getZhengji = new Point(26, 586);
    public  static Point getOther = new Point(26, 530);

    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        for (Rect rect : rects) {
            if (rect.contains(zhengwuNum) || rect.contains(getZhengji) ||  rect.contains(getOther)) {
                result.add(OrcConfig.append(rect, 4, 0, 4, 0));
            }
        }
        return result;
    }
}
