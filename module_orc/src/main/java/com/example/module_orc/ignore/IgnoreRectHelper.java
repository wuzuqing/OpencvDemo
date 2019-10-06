package com.example.module_orc.ignore;

import org.opencv.core.Rect;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：士元
 * 时间：2019/9/16 18:03
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class IgnoreRectHelper {
    private static IgnoreRectHelper instance = new IgnoreRectHelper();

    private Map<String, IIgnoreRect> iIgnoreRectMap = new HashMap<>();


    private IgnoreRectHelper() {
        addIgnoreRect("成就", new ChengjiujiangliIgnoreRect());
        addIgnoreRect("处理公务", new ChuligongwuIgnoreRect());
        addIgnoreRect("红颜知已", new HongyanzhijiIgnoreRect());
        addIgnoreRect("皇宫", new HuanggongIgnoreRect());
        addIgnoreRect("缉捕", new JibuIgnoreRect());
        addIgnoreRect("经营资产", new JingyingzicanIgnoreRect());
        addIgnoreRect("牢房", new LaofangIgnoreRect());
        addIgnoreRect("联盟", new LianmengIgnoreRect());
        addIgnoreRect("膜拜", new MobaijiangliIgnoreRect());
        addIgnoreRect("任务", new RenwuIgnoreRect());
        addIgnoreRect("任务奖励", new RenwujiangliIgnoreRect());
        addIgnoreRect("书院", new ShuyuanIgnoreRect());
        addIgnoreRect("我的子嗣", new WodizisiIgnoreRect());
        addIgnoreRect("寻访", new XunfangIgnoreRect());
        addIgnoreRect("邮件", new YoujianIgnoreRect());
        //////////////////////////////////////////////////
        addIgnoreRect("排行榜", new YoujianIgnoreRect());
        addIgnoreRect("跨服榜单", new YoujianIgnoreRect());
        addIgnoreRect("本服榜单", new YoujianIgnoreRect());
        addIgnoreRect("翰林院", new YoujianIgnoreRect());
        addIgnoreRect("讨伐", new YoujianIgnoreRect());
        addIgnoreRect("内阁", new YoujianIgnoreRect());
        addIgnoreRect("通商", new YoujianIgnoreRect());
        addIgnoreRect("联盟兑换", new YoujianIgnoreRect());
    }

    public static IgnoreRectHelper getInstance() {
        return instance;
    }

    public void addIgnoreRect(String key, IIgnoreRect iIgnoreRect) {
        if (!iIgnoreRectMap.containsKey(key)) {
            iIgnoreRectMap.put(key, iIgnoreRect);
        }
    }

    public IIgnoreRect getIgnoreRect(String key){
        return iIgnoreRectMap.get(key);
    }

    public boolean ignoreRect(String key, Rect rect) {
        IIgnoreRect ignoreRect = iIgnoreRectMap.get(key);
        if (ignoreRect != null) {
            return ignoreRect.ignoreRect(rect);
        }
        return false;
    }
}
