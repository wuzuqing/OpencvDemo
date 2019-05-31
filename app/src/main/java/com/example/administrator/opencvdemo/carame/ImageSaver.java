package com.example.administrator.opencvdemo.carame;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;

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
    Camera2Helper.OnCallBack callBack;
    public ImageSaver(Image image, int cameraID, File file,Camera2Helper.OnCallBack callBack) {
        mImage = image;
        mCameraID = cameraID;
        saveFile = file;
      this.  callBack = callBack;

    }

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
            if (callBack!=null){
                callBack.talePicture(saveFile.getAbsolutePath(),bitmap);
            }
        }

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
        if (bitmap==null){
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
