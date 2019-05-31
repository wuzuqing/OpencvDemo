package com.example.administrator.opencvdemo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.opencvdemo.carame.Camera2Helper;

public class ScanActivity extends AppCompatActivity {

    private MarkView1 markView;
    private SurfaceView vSurfaceView;
    private Camera2Helper mCamera2Helper;
    private ImageView iv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        vSurfaceView = findViewById(R.id.surfaceView);
        iv = findViewById(R.id.img);
        mCamera2Helper = new Camera2Helper(vSurfaceView);
        mCamera2Helper.setOnCallBack(new Camera2Helper.OnCallBack() {
            @Override
            public void talePicture(String path,final Bitmap bitmap) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       iv.setImageBitmap(bitmap);
                   }
               });
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCamera2Helper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @TargetApi(21)
    public void shiBie(View view) {
        mCamera2Helper.takePreview();
    }

    public void toMain(View view) {
    }
}
