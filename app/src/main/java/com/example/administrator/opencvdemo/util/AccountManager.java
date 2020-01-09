package com.example.administrator.opencvdemo.util;

import java.util.ArrayList;
import java.util.List;
import android.text.TextUtils;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.google.gson.reflect.TypeToken;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 11:59
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class AccountManager {
    private static final String KEY_USER_INFO = "USER_INFO";
    private static final String KEY_USER_INDEX = "USER_INDEX";

    private static String sDefUserInfo =
        //            "ck52434333,520333&ck83250887,520333&ck84012149,520333&ck75077701,520333&ck74266770,520333&" +
        //            "ck56270983,520333&ck41351036,520333&" +
        //            "ck97381288,520333&ck68721497,520333&ck99371248,520333&ck21627506,520333&" +
        //             "wgl251&wgl252&wgl253&wgl254&wgl255&wgl256&wgl257&wgl258&wgl259&wgl260&wgl261&wgl262";
        "wgl243&wgl244&wgl245&wgl246&wgl247&wgl248&wgl249&wgl250&wgl251&wgl252&wgl253&wgl254&wgl255&wgl256&wgl257&wgl258&wgl259&wgl260&wgl261&wgl262";
    //            "ck69539153,520333&ck82369145,520333&ck19656822,520333&ck92984644,520333";

    public static void init() {
        String infoStr = Util.getFileStringAndSp(KEY_USER_INFO);
        if (TextUtils.isEmpty(infoStr)) {
            saveUserInfo(sDefUserInfo);
        }
    }

    public static List<UserInfo> getUserInfo() {
        String infoStr = Util.getFileStringAndSp(KEY_USER_INFO);
        if (TextUtils.isEmpty(infoStr)) {
            return null;
        }
        return (List<UserInfo>) JsonUtils.fromJson(infoStr, new TypeToken<List<UserInfo>>() {
        }.getType());
    }

    public static void saveUserInfo(String userInfo) {
        String[] userInfos = userInfo.split("&");
        if (userInfos.length > 0) {
            try {
                List<UserInfo> userInfoList = new ArrayList<>();
                for (String info : userInfos) {
                    if (info.contains(",")) {
                        String[] split = info.split(",");
                        userInfoList.add(new UserInfo(split[0], split[1]));
                    } else {
                        userInfoList.add(new UserInfo(info, ""));
                    }
                }
                saveUserInfo(userInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveUserInfo(List<UserInfo> userInfos) {
        Util.setFileStrAndSp(KEY_USER_INFO, JsonUtils.toJson(userInfos));
    }

    public static synchronized void saveUserInfoIndex(int index) {
        Util.setFileStrAndSp(KEY_USER_INDEX, String.valueOf(index));
    }

    public static int getUserIndex() {
        String userIndex = Util.getFileStringAndSp(KEY_USER_INDEX);
        if (TextUtils.isEmpty(userIndex)) {
            return 0;
        }
        return Integer.valueOf(userIndex);
    }
}
