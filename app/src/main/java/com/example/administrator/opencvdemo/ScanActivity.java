package com.example.administrator.opencvdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.opencvdemo.carame.CameraPreview;
import com.example.administrator.opencvdemo.carame.OnCallBack;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;

import java.util.List;

public class ScanActivity extends AppCompatActivity {

    private MarkView1 markView;
    private CameraPreview vSurfaceView;
    //    private Camera2Helper mCamera2Helper;
    private ImageView iv;
    private TextView vTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        vSurfaceView = findViewById(R.id.surfaceView);
        markView = findViewById(R.id.markView);
        iv = findViewById(R.id.img);
        vTv = findViewById(R.id.tvResult);
        vSurfaceView.setOnCallBack(new OnCallBack() {
            @Override
            public void talePicture(String path,final Bitmap bitmap) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       iv.setImageBitmap(bitmap);
                       orcBitmap(bitmap);
                   }
               });
            }
        });
    }

    private void orcBitmap(Bitmap bitmap){
        OrcHelper.getInstance().executeCallAsync("id", bitmap, "id3", new IDiscernCallback() {
            @Override
            public void call(final List<OrcModel> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vTv.setText(result.toString());
                    }
                });
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        vSurfaceView.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void shiBie(View view) {
        vSurfaceView.setCropRect(markView.getContentRect());
        vSurfaceView.takePicture();
    }

    public void toMain(View view) {
        startActivity(new Intent(this, MainActivity1.class));
    }
}
