package com.example.administrator.opencvdemo.carame;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.Image;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@TargetApi(21)
public class ImageSaver implements Runnable {
    private Image mImage;
    private int mCameraID;
    private File saveFile;
    private RectF cropRect;
    OnCallBack callBack;

    public ImageSaver(Image image, int cameraID, File file, OnCallBack callBack) {
        this(image, cameraID, file, callBack, null);
    }

    public ImageSaver(Image image, int cameraID, File file, OnCallBack callBack, RectF crop) {
        mImage = image;
        mCameraID = cameraID;
        saveFile = file;
        this.callBack = callBack;
        this.cropRect = crop;

    }

    private static final String TAG = "ImageSaver";
    @Override
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (bitmap != null) {
            if (mCameraID == 1) {
                //前置摄像头拍的要先旋转180度
                bitmap = adjustPhotoRotation(bitmap, 180);
            }
            writeToFile(bitmap);
            if (callBack != null) {
                if (cropRect != null) {
                    Log.d(TAG,  "run: " +bitmap.getWidth() + "/"+bitmap.getHeight() + " : " + cropRect.toShortString());
                    bitmap = Bitmap.createScaledBitmap(bitmap,1080,1920,false);
                    bitmap = Bitmap.createBitmap(bitmap, (int) cropRect.left, (int) cropRect.top, (int) cropRect.width(), (int) cropRect.height());
                }
                callBack.talePicture(saveFile.getAbsolutePath(), bitmap);
            }
        }
        mImage.close();
    }

    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    private void writeToFile(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        try {
            OutputStream os = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
