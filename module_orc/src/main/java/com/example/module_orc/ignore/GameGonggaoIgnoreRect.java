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
 * 说明：游戏公告
 */
public class GameGonggaoIgnoreRect implements IIgnoreRect {
    public static final Rect gameNoice = new Rect(127, 123, 107, 30);
    public static final Rect close = new Rect(312, 137, 44, 23);

    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        // {127, 123, 107x30}  {312, 137, 44x23}
//        result.add(OrcConfig.append(gameNoice));
        result.add(OrcConfig.append(close));
        return result;
    }
}
