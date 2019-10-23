package com.example.administrator.opencvdemo.youtu;

import com.google.gson.Gson;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/18 14:53
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/18$
 * @updateDes ${TODO}
 */

public class StaticVal {
    public static final String APP_ID = "10115505";
    public static final String SECRET_ID = "AKIDY1JPWWtHpIbjhTpz60iJl273oNn1x0mN";
    public static final String SECRET_KEY = "a1GILJD7sSs6DAh3GI8mJjxzpstjSzMy";
    public static final String USER_ID = "10115505";
    public final static String API_YOUTU_END_POINT = "https://api.youtu.qq.com/youtu/";


    private static Youtu sYoutu;
    private static Gson gson;

    public static void init() {
        sYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, API_YOUTU_END_POINT, USER_ID);
        gson = new Gson();
    }
    public static Youtu getYoutu(){
        return sYoutu;
    }

    public static Gson getGson() {
        return gson;
    }
}
