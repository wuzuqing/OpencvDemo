package com.example.administrator.opencvdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.example.administrator.opencvdemo.carame.Camera2Helper;

public class ScanActivity extends AppCompatActivity {

    private MarkView1 markView;
    private SurfaceView vSurfaceView;
    private Camera2Helper mCamera2Helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        vSurfaceView = findViewById(R.id.surfaceView);
        mCamera2Helper = new Camera2Helper(vSurfaceView);
    }




}
