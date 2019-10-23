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
 * 说明：进入游戏
 */
public class StartGameIgnoreRect implements IIgnoreRect {
    public static final Rect startGame = new Rect(106, 525, 147, 32);
    public static final Rect huanQu = new Rect(250, 446, 41, 14);
    @Override
    public List<OrcModel> ignoreRect(List<Rect> rects) {
        List<OrcModel> result = new ArrayList<>();
        result.add(OrcConfig.append(startGame));
        result.add(OrcConfig.append(huanQu));
        return result;
    }
}