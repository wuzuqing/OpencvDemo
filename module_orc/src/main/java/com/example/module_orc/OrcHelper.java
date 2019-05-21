package com.example.module_orc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrcHelper {
    private static OrcHelper instance = new OrcHelper();
    private TessBaseAPI baseApi;

    private ExecutorService mExecutor;
    private Handler vHandler;

    private OrcHelper() {
        mExecutor = Executors.newCachedThreadPool();
        vHandler = new Handler(Looper.getMainLooper());
    }

    public static OrcHelper getInstance() {
        return instance;
    }

    private String cacheDir;
    private Context mContext;

    public void init(Context context) {
        mContext = context.getApplicationContext();
        cacheDir = context.getExternalCacheDir().getAbsolutePath();
        copyLanguagePackageToSDCard("id2");
    }

    private String copyLanguagePackageToSDCard(String langName) {

        String dirPath = cacheDir + "/tessdata";
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String filePath = dirPath + "/" + langName + ".traineddata";
        File file = new File(filePath);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        InputStream inputStream = null;
        try {
            OutputStream outputStream = new FileOutputStream(file);
            inputStream = mContext.getAssets().open(langName + ".traineddata");
            byte[] buffer = new byte[2048];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 对要识别的图像进行识别
     *
     * @param bitmap 要识别的bitmap
     * @return
     */
    public String orcText(Bitmap bitmap, String langName) {
        copyLanguagePackageToSDCard(langName);
        String result;
        if (baseApi == null) {
            baseApi = new TessBaseAPI();
            baseApi.setDebug(true);
            baseApi.init(cacheDir, langName);
        } else {
            if (!langName.equals(baseApi.getInitLanguagesAsString())) {
                baseApi.end();
                baseApi.init(cacheDir, langName);
            }
        }
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);
//        baseApi.setVariable("tessedit_char_whitelist", "0123456789X");
        result = baseApi.getUTF8Text();
        result = result.replaceAll("\\s*", "");

        bitmap.recycle();
        return result;
    }

    public void executeCallSysn(final String type, final Bitmap bitmap, final IDiscernCallback callback) {
        switch (type) {
            case "id":
                mExecutor.execute(new IDCardDiscern(bitmap, "id2", callback));
                break;
        }

    }

}
