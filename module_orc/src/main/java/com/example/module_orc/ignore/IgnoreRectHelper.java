package com.example.module_orc.ignore;

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
        addIgnoreRect("处理公务", new ChuligongwuIgnoreRect());  //1
        addIgnoreRect("红颜知已", new HongyanzhijiIgnoreRect());
        addIgnoreRect("皇宫", new HuanggongIgnoreRect());   // 1
        addIgnoreRect("皇宫俸禄", new HuanggongFengluIgnoreRect()); //1
        addIgnoreRect("缉捕", new JibuIgnoreRect());
        addIgnoreRect("经营资产", new JingyingzicanIgnoreRect());  //1
        addIgnoreRect("牢房", new LaofangIgnoreRect());
        addIgnoreRect("联盟", new LianmengIgnoreRect());
        addIgnoreRect("膜拜", new MobaijiangliIgnoreRect());  //1
        addIgnoreRect("任务", new RenwuIgnoreRect());
        addIgnoreRect("任务奖励", new RenwujiangliIgnoreRect());
        addIgnoreRect("书院", new ShuyuanIgnoreRect());
        addIgnoreRect("我的子嗣", new WodizisiIgnoreRect());
        addIgnoreRect("寻访", new XunfangIgnoreRect());
        addIgnoreRect("邮件", new YoujianIgnoreRect());
        //////////////////////////////////////////////////
        addIgnoreRect("排行榜", new PaihangbangIgnoreRect());      //1
        addIgnoreRect("跨服榜单", new BenfubangdanIgnoreRect());    //1
        addIgnoreRect("本服榜单", new BenfubangdanIgnoreRect());        //1
        addIgnoreRect("翰林院", new HanlinyuanIgnoreRect());
        addIgnoreRect("讨伐", new TaofaIgnoreRect());
        addIgnoreRect("内阁", new NeigeIgnoreRect());
        addIgnoreRect("通商", new TongshangIgnoreRect());
        addIgnoreRect("联盟兑换", new LianmengduihuanIgnoreRect());
        addIgnoreRect("登录", new DengluIgnoreRect()); // 1
        addIgnoreRect("进入游戏", new StartGameIgnoreRect()); //1
        addIgnoreRect("游戏公告", new GameGonggaoIgnoreRect()); //1
        addIgnoreRect("府内", new FuneiIgnoreRect());   //1
        addIgnoreRect("府外", new FuwaiMapIgnoreRect()); //1
        addIgnoreRect("道具使用", new DaojuIgnoreRect());//1
    }

    public static IgnoreRectHelper getInstance() {
        return instance;
    }

    public void addIgnoreRect(String key, IIgnoreRect iIgnoreRect) {
        if (!iIgnoreRectMap.containsKey(key)) {
            iIgnoreRectMap.put(key, iIgnoreRect);
        }
    }

    public IIgnoreRect getIgnoreRect(String key) {
        return iIgnoreRectMap.get(key);
    }

//    public boolean ignoreRect(String key, Rect rect) {
//        IIgnoreRect ignoreRect = iIgnoreRectMap.get(key);
//        if (ignoreRect != null) {
//            return ignoreRect.ignoreRect(rect);
//        }
//        return false;
//    }
}
