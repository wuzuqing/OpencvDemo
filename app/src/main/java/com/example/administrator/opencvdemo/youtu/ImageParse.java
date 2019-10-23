package com.example.administrator.opencvdemo.youtu;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.util.CmdData;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.TaskUtil;
import com.example.administrator.opencvdemo.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/17 21:35
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/17$
 * @updateDes ${TODO}
 */

public class ImageParse {


    public static void parseImg(boolean isAsync, final boolean isOnePax, final Call call) {
        if (isAsync) {
            HandlerUtil.async(new Runnable() {
                @Override
                public void run() {
                    getData(call, isOnePax);
                }
            });
        } else {
            getData(call, isOnePax);
        }
    }
    public static void getSyncData(Call call) {
        LogUtils.logd("start");
        Util.getCapBitmapNew();
        JSONObject respose = null;
        try {
            respose = StaticVal.getYoutu().GeneralOcr(ScreenCapture.getBitmapByte(TaskUtil.bitmap));
            Result res = StaticVal.getGson().fromJson(respose.toString(), Result.class);
            if (call != null) {
                LogUtils.logd("end" + res.getItems());
                call.call(res.getItems());
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (call != null) {
            call.call(new ArrayList<Result.ItemsBean>());
        }
    }

    private static void getData(Call call, boolean isOnePax) {
        LogUtils.logd("start");
        JSONObject respose = null;
        try {
            File file = isOnePax ? Luban.get(BaseApplication.getAppContext()).firstCompress(CmdData.saveFilePath) : Luban.get(BaseApplication.getAppContext()).thirdCompress
                    (CmdData.saveFilePath);
             respose = StaticVal.getYoutu().GeneralOcr(file.getAbsolutePath());
            Result res = StaticVal.getGson().fromJson(respose.toString(), Result.class);
            if (call != null) {
                LogUtils.logd("end" + res.getItems());
                call.call(res.getItems());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (call != null) {
            call.call(new ArrayList<Result.ItemsBean>());
        }
    }

    public interface Call {
        void call(List<Result.ItemsBean> result);
    }

}
