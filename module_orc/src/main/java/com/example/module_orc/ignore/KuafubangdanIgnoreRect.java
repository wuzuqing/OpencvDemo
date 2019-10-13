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
 * 说明：跨服榜单
 */
public class KuafubangdanIgnoreRect implements IIgnoreRect {
    public static Rect moBai = new Rect(274,528,38,13);
    public static Rect lianMeng = new Rect(128,121,63,13);
    public static Rect menKe = new Rect(229,121,62,13);
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        //{274, 524, 38x13}
        //{222, 115, 50x13}
        // {126, 115, 50x13}
        Rect mb = new Rect();
        Rect gk = new Rect();
        Rect qm = new Rect();
        for (Rect rect : rects) {
            if (rect.equals(moBai)) {
                mb = rect;
            } else if (lianMeng.equals(rect)) {
                gk = rect;
            } else if (menKe.equals(rect)) {
                qm = rect;
            }
        }
        result.add(OrcConfig.append(mb));
        result.add(OrcConfig.append(gk));
        result.add(OrcConfig.append(qm));
        return result;
    }
}
