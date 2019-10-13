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
 * 说明：道具
 */
public class DaojuIgnoreRect implements IIgnoreRect {
  public static  final   Rect daojuClose = new Rect(298, 184, 59, 37);
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        result.add(OrcConfig.append(daojuClose));
        return result;
    }
}
