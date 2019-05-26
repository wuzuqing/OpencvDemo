package com.example.administrator.opencvdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

public class ScanActivity extends AppCompatActivity {

    private MarkView1 markView;
    private SurfaceView vSurfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        vSurfaceView = findViewById(R.id.surfaceView);

    }


}
