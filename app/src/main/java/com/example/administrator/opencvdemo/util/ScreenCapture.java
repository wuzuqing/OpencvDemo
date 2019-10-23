package com.example.administrator.opencvdemo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.floatservice.RequestPermissionsActivity;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;
import com.example.module_orc.WorkMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/2/24 12:32
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/2/24$
 * @updateDes ${TODO}
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCapture {
    private static ScreenCapture INSTANCE = new ScreenCapture();
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;

    private Context mContext;
    private Intent mResultData = null;
    private int mScreenWidth;
    private int mScreenHeight;

    //系统保存截图的路径
    public static final String SCREENCAPTURE_PATH = "ScreenCapture" + File.separator + "Screenshots" + File.separator;
    //  public static final String SCREENCAPTURE_PATH = "ZAKER" + File.separator + "Screenshots" + File.separator;

    public static final String SCREENSHOT_NAME = "Screenshot";

    private static final String TAG = "FloatWindowsService";
    private static final int REQUEST_MEDIA_PROJECTION = 18;

    private int mScreenDensity;

    public static ScreenCapture get() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mScreenHeight = mScreenHeight > 1500 ? 1920 : mScreenHeight;
        Log.d(TAG, "mScreenWidth: " + mScreenWidth);
        Log.d(TAG, "mScreenHeight: " + mScreenHeight);
        Log.d(TAG, "densityDpi: " + metrics.densityDpi);
    }

    public static void init(Context context, Intent intent) {
        get().init(context);
        get().setIntent(intent);
    }

    public static void requestCapturePermission(Activity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return;
        }
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null) {
            context.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    public static boolean onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION && resultCode == -1 && data != null) {
            ScreenCapture.init(context, data);
            return true;
        }
        return false;
    }

    public void setIntent(Intent intent) {
        mResultData = intent;
        createImageReader();
    }

    /**
     * 同步
     */
    public static void startCaptureSync() {
        try {
            get()._startCapture(false, 50);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 同步
     */
    public static void startCaptureSync(long time) {
        get()._startCapture(false, time);
    }

    /**
     * 异步
     */
    public static void startCaptureAsyn() {
        get()._startCapture(true, 200);
    }

    public static void onDestroy() {

        get().tearDownMediaProjection();
        get().stopVirtual();
    }

    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror", mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mImageReader.getSurface(), null, null);
    }

    private void createImageReader() {
        try {
            mImageReader =
                ImageReader.newInstance(
                    mScreenWidth,
                    mScreenHeight,
                    SPUtils.getInt("PixelFormat",PixelFormat.RGBA_8888),
                    2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long startTime;

    private void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    private void setUpMediaProjection() {
        Log.d(TAG, "mResultData: " + mResultData);
        Log.d(TAG, "mMediaProjection: " + mMediaProjection);
        if (mResultData == null) {
            Intent intent = new Intent(mContext, RequestPermissionsActivity.class);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
        }
    }

    private void _startCapture(boolean isAsyn, long time) {
        try {
            if (mImageReader == null) {
                createImageReader();
            }
            Image image = mImageReader.acquireLatestImage();
            startTime = System.currentTimeMillis();
            if (image == null) {
                startScreenShot(isAsyn);
            } else {
                if (isAsyn) {
                    SaveTask mSaveTask = new SaveTask();
                    mSaveTask.execute(image);
                } else {
                    try {
                        Thread.sleep(time);
                        mCurrentBitmap = getBitmap(image);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            SPUtils.setInt("PixelFormat",PixelFormat.RGBX_8888);
        }
    }

    private void startScreenShot(boolean isAsyn) {
        if (isAsyn) {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                public void run() {
                    //start virtual
                    startVirtual();
                }
            }, 5);
            handler1.postDelayed(new Runnable() {
                public void run() {
                    //capture the screen
                    startCaptureSync();
                }
            }, 30);
        } else {
            startVirtual();
            try {
                Thread.sleep(50);
                startCaptureSync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean initFinish() {
        if (mImageReader == null) {
            createImageReader();
        }
        return mImageReader != null;
    }

    private Bitmap mCurrentBitmap;

    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return null;
            }
            Bitmap bitmap = getBitmap(params[0]);
            long time = System.currentTimeMillis() - startTime;
            Log.e("ryze", "获取图片成功" + " time:" + time);
            File fileImage = null;
            if (bitmap != null) {
                try {
                    fileImage = new File(getScreenShotsName(mContext));
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                        out.flush();
                        out.close();
                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fileImage);
                        media.setData(contentUri);
                        mContext.sendBroadcast(media);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }
            }

            if (fileImage != null) {
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //预览图片
        }
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    private static Bitmap getBitmap(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        //每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        //总的间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        // savePicture(bitmap);
        image.close();
        return bitmap;
    }

    private static void savePicture(Bitmap bitmap) {
        try {
            FileOutputStream stream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/aa.jpg");
            bitmap.compress( Bitmap.CompressFormat.JPEG,80,stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public Bitmap getCurrentBitmap() {
        return mCurrentBitmap;
    }

    public static void getPage() {
        OrcHelper.getInstance().executeCallAsync(WorkMode.ONLY_BITMAP, get().mCurrentBitmap, "zwp", "1", new IDiscernCallback() {
            @Override
            public void call(final List<OrcModel> result) {
                Log.d(TAG, "getPage: " + result.toString());
            }
        });
    }

    public static String getAppPath(Context context) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            return Environment.getExternalStorageDirectory().toString();
        } else {

            return context.getFilesDir().toString();
        }
    }

    public static String getScreenShots(Context context) {

        StringBuffer stringBuffer = new StringBuffer(getAppPath(context));
        stringBuffer.append(File.separator);

        stringBuffer.append(SCREENCAPTURE_PATH);

        File file = new File(stringBuffer.toString());

        if (!file.exists()) {
            file.mkdirs();
        }

        return stringBuffer.toString();
    }

    public static String getScreenShotsName(Context context) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        String date = simpleDateFormat.format(new Date());

        StringBuffer stringBuffer = new StringBuffer(getScreenShots(context));
        stringBuffer.append(SCREENSHOT_NAME);
        stringBuffer.append("_");
        stringBuffer.append(date);
        stringBuffer.append(".png");

        return stringBuffer.toString();
    }
}
