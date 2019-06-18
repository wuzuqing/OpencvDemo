package com.example.administrator.opencvdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.module_orc.OpenCVHelper;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.utils.OpencvUtil;

import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private android.widget.ImageView ivimg;
    private android.widget.ImageView ivcrop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.ivcrop = (ImageView) findViewById(R.id.iv_crop);
        this.ivimg = (ImageView) findViewById(R.id.iv_img);

        ivimg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);


                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            ivimg.setImageURI(uri);
            cropImg(uri);
        }
    }

    private void cropImg(final Uri uri) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                     Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    bitmap =   Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);
                    Mat src = new Mat();
                    Utils.bitmapToMat(bitmap, src);
                    src=  OpencvUtil.houghLinesP(src.clone(), src);
                    bitmap = Bitmap.createBitmap(src.width(),src.height(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(src, bitmap);
                    final Bitmap finalBitmap = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivcrop.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVHelper.init(this);
    }
}
