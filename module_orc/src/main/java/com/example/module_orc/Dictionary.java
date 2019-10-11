package com.example.module_orc;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：士元
 * 时间：2019/9/28 0028 14:34
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class Dictionary {
    private static Map<String, String> dictionary = new HashMap<>();
    private static Map<String, String> signDict = new HashMap<>();

    static {
        dictionary.put("mid_bangdan_benfu.jpg", "本服榜单");
        dictionary.put("mid_bangdan_kuafu.jpg", "跨服榜单");
        dictionary.put("mid_bangdan_paihangbang.jpg", "排行榜");
        dictionary.put("mid_fenglu_end.jpg", "领取俸禄");
        dictionary.put("mid_hanlinyuan.jpg", "翰林院");
        dictionary.put("mid_laofang.jpg", "牢房");
        dictionary.put("mid_lianmeng_index.jpg", "联盟");
        dictionary.put("mid_lianmeng_choose.jpg", "联盟");
        dictionary.put("mid_lianmeng_duihuan.jpg", "联盟兑换");
        dictionary.put("mid_tongshang.jpg", "通商");
        dictionary.put("mid_yamen_jibu.jpg", "缉捕");
        dictionary.put("mid_neige.jpg", "内阁");
        dictionary.put("mid_hongyanzhiji.jpg", "红颜知己");

        dictionary.put("mid_chengjiu.jpg", "成就");
        dictionary.put("mid_chuligongwu.jpg", "处理公务");
        dictionary.put("mid_jingyingzichan.jpg", "经营资产");
        dictionary.put("mid_renwu.jpg", "任务");
        dictionary.put("mid_shuyuan.jpg", "书院");
        dictionary.put("mid_taofa.jpg", "讨伐");
        dictionary.put("mid_wodizisi.jpg", "我的子嗣");
        dictionary.put("mid_xunfang.jpg", "寻访");
    }

    public static String getTitle(String key){
       return dictionary.get(key);
    }

    private static final String TAG = "Dictionary";
    public static String getSignTitle(String sign){
       Log.e(TAG, "getSignTitle: "+sign);
        return signDict.get(sign);
    }
    public static void putSign(String sign,String name){
        signDict.put(sign,name);
    }

    public static List<String > getDefaultPage(){
        List<String> names = new ArrayList<>();
        names.add("成就");
        names.add("处理公务");
        names.add("红颜知己");
        names.add("经营资产");
        names.add("内阁");
        names.add("任务");
        names.add("书院");
        names.add("讨伐");
        names.add("我的子嗣");
        names.add("寻访");
        return names;
    }
}
