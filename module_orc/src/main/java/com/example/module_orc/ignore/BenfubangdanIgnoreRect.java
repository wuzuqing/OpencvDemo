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
 * 说明：本服榜单
 */
public class BenfubangdanIgnoreRect implements IIgnoreRect {
    public static Rect moBai = new Rect(274, 524, 38, 13);
    public static Rect moBaiMax = new Rect(274*3, 524*3, 38*3, 13*3);
    public static Rect guanKa = new Rect(126, 118, 50, 13);
    public static Rect qinMi = new Rect(222, 118, 50, 13);
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        //{274, 524, 38x13}
        //{222, 115, 50x13}
        // {126, 115, 50x13}
        Rect mb = new Rect();
        for (Rect rect : rects) {
            if (rect.width == moBai.width && rect.height == moBai.height && rect.x ==moBai.x) {
                mb = rect;
            }
        }
        result.add(OrcConfig.append(mb));
        result.add(OrcConfig.append(guanKa));
        result.add(OrcConfig.append(qinMi));
        return result;
    }
}
