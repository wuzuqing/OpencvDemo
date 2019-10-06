package com.example.module_orc.util;

import com.google.gson.Gson;

import java.util.Map;

public class GsonUtils {
    private static Gson sGson = new Gson();
    public static String toJson(Object object){
      return   sGson.toJson(object);
    }

    public static Map<String,String> toMap(String str){
        Map map = sGson.fromJson(str, Map.class);
        return map;
    }
}
