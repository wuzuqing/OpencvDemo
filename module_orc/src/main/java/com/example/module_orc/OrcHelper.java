package com.example.module_orc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.module_orc.util.GsonUtils;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrcHelper {
    private static final String TAG = "OrcHelper";
    private static OrcHelper instance = new OrcHelper();
    private TessBaseAPI baseApi;

    private ExecutorService mExecutor;
    private Handler vHandler;

    public File rootDir;

    private OrcHelper() {
        mExecutor = Executors.newCachedThreadPool();
        vHandler = new Handler(Looper.getMainLooper());
        boolean isMoble = Build.BRAND.toUpperCase().contains("Oppo".toUpperCase());
        File directory = Environment.getExternalStoragePublicDirectory(isMoble ? Environment.DIRECTORY_DCIM : Environment.DIRECTORY_MOVIES);
        rootDir = new File(directory, isMoble ? "/Screenshots" : "/image");

    }

    public File getTargetFile(String target) {
        if (!TextUtils.isEmpty(target)) {
            return new File(rootDir, target);
        }
        return null;
    }

    public static OrcHelper getInstance() {
        return instance;
    }

    private String cacheDir;
    private Context mContext;

    public Mat loadResource(int id) {
        try {
            return Utils.loadResource(mContext, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        cacheDir = context.getExternalCacheDir().getAbsolutePath();
        OrcConfig.initFirst();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        OrcConfig.resetScreenSize(metrics.widthPixels, metrics.heightPixels);
        copyLanguagePackageToSDCard("zwp");
        copyLanguagePackageToSDCard("small");
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
//                        file.delete();
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

    private Map<String, TessBaseAPI> apis = new HashMap<>();

    /**
     * 对要识别的图像进行识别
     *
     * @param bitmap 要识别的bitmap
     */
    public String orcText(Bitmap bitmap, String langName) {
//        copyLanguagePackageToSDCard(langName);
        String result;
        TessBaseAPI baseApi = apis.get(langName);
        if (baseApi == null) {
            baseApi = new TessBaseAPI();
            baseApi.setDebug(true);
            baseApi.init(cacheDir, langName);
            apis.put(langName, baseApi);
        }
        baseApi.setImage(bitmap);
        //        baseApi.setVariable("tessedit_char_whitelist", "0123456789X");
        result = baseApi.getUTF8Text();
        result = result.replaceAll("\\s*", "");

        bitmap.recycle();
        return result;
    }

    public void executeCallAsync(final WorkMode mode, final Bitmap bitmap, String langName, final IDiscernCallback callback) {
        switch (mode) {
            case ID_CARD:
                mExecutor.execute(new IDCardDiscern(bitmap, langName, callback));
                break;
            case NORMAL:
                mExecutor.execute(new NormalCardDiscern(bitmap, langName, callback));
                break;
            case ONLY_BITMAP:
                mExecutor.execute(new OnlyCardDiscern(bitmap, langName, callback));
                break;
        }
    }

    public void executeCallAsync(final WorkMode mode, final Bitmap bitmap, String langName, String pex, final IDiscernCallback callback) {
        switch (mode) {
            case ID_CARD:
                mExecutor.execute(new IDCardDiscern(bitmap, langName, callback));
                break;
            case NORMAL:
                mExecutor.execute(new NormalCardDiscern(bitmap, langName, callback));
                break;
            case ONLY_BITMAP:
                mExecutor.execute(new OnlyCardDiscern(bitmap, langName, pex, callback));
                break;
        }
    }

    public List<OrcModel> executeCallSync(final Bitmap bitmap) {
        if (bitmap==null){
            return new ArrayList<>();
        }
        final List<OrcModel> result = new ArrayList<>();
        OnlyCardDiscern discern = new OnlyCardDiscern(bitmap, "zwp", "", new IDiscernCallback() {
            @Override
            public void call(List<OrcModel> item) {
                result.addAll(item);
            }
        });
        discern.run();
        return result;
    }

    public void executeCallAsyncV2(final String filePath, final String langName, final String pex, final IDiscernCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mExecutor.execute(new OnlyCardDiscern(filePath, langName, pex, callback));
            }
        });
    }

    public void executeCallAsyncV3(final String filePath, final String langName, final String pex, final IDiscernCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mExecutor.execute(new OnlyCardDiscern(BitmapFactory.decodeFile(filePath), langName, pex, callback));
            }
        });
    }

    public void fileToBitmap(final BaseCallBack1<Bitmap> bitmapCallable, final File... filePaths) {
        if (filePaths == null || filePaths.length == 0) {
            return;
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (File path : filePaths) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path.getAbsolutePath());
                    bitmapCallable.call(bitmap, path.getName());
                }
            }
        });
    }

    /**
     * 压缩文件
     */
    public void compress(File dir) {
        Log.e(TAG, "compress 1");
        dir = new File(dir, "/other1");
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        Log.e(TAG, "compress 2" + files.length);
        Mat gray = new Mat();
        int xishu = 6;
        long start = 0;
        Map<String, String> data = new HashMap<>();
        for (File file : files) {
            String name = file.getName();
            Log.e(TAG, "compress 3" + name);
            if (file.isDirectory()) {
                continue;
            }
            start = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Mat demo = new Mat();
            Utils.bitmapToMat(bitmap, demo);

            Imgproc.cvtColor(demo, gray, Imgproc.COLOR_BGRA2GRAY);
            Imgproc.threshold(gray, gray, OrcConfig.thresh, 255, OrcConfig.threshType);

            Imgproc.resize(gray, gray, OrcConfig.compressScreenSize);

            //            Imgcodecs.imwrite(new File(OrcHelper.getInstance().rootDir + "/scale", name.substring(0, name.indexOf(".")) + ".jpg").getAbsolutePath(), gray);
            Mat crop = new Mat(gray, OrcConfig.titleMidRect);
            String sign = OrcConfig.getSign(crop);
            File file1 = new File(OrcHelper.getInstance().rootDir + "/mid", "mid_" + name.substring(0, name.indexOf(".")) + ".jpg");
            data.put(sign, file1.getName());
            Imgcodecs.imwrite(file1.getAbsolutePath(), crop);
            Log.e(TAG, "used:" + (System.currentTimeMillis() - start) + " name:" + name);
        }
        try {
            File dataFile = new File(OrcHelper.getInstance().rootDir + "/mid", "data.txt");
            String json = GsonUtils.toJson(data);
            FileOutputStream stream = new FileOutputStream(dataFile);
            byte[] bytes = json.getBytes("gbk");
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     */
    public void compressPngToJpg(File dir) {
        dir = new File(dir, "/other");
        if (!dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        Log.e(TAG, "compress 2" + files.length);
        Rect rectCurrent = OrcConfig.titleMidRectCurrent;
        int x = rectCurrent.x;
        int y = rectCurrent.y;
        int width = rectCurrent.width;
        int height = rectCurrent.height;
        for (File file : files) {
            String name = file.getName();
            Log.e(TAG, "compress 3" + name);
            if (file.isDirectory()) {
                continue;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            try {
                Bitmap crop = Bitmap.createBitmap(bitmap, x, y, width, height);
                FileOutputStream outputStream = new FileOutputStream(new File(OrcHelper.getInstance().rootDir, name.substring(0, name.indexOf(".")) + ".jpg").getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
                outputStream = new FileOutputStream(new File(OrcHelper.getInstance().rootDir + "/mid1", "mid_" + name.substring(0, name.indexOf(".")) + ".jpg").getAbsolutePath());
                crop.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
