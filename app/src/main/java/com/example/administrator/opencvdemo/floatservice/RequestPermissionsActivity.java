package com.example.administrator.opencvdemo.floatservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.opencvdemo.util.ScreenCapture;

public class RequestPermissionsActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            start();
        } else {
            if (Settings.canDrawOverlays(this)) {
                start();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                Toast.makeText(RequestPermissionsActivity.this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void start() {
        if (Build.VERSION.SDK_INT >= 21) {
            //请求权限
            ScreenCapture.requestCapturePermission(this);
        } else {
            startMainService();
        }
    }
    private void startMainService() {
//        setResult(Activity.RESULT_OK);
        if (getIntent() != null && getIntent().hasExtra("hao")) {
            Intent intent1 = new Intent(RequestPermissionsActivity.this, MainService.class);
            intent1.putExtra("action", "ACTION_BOOT_COMPLETED");
            startService(intent1);
            // finish();
        } else {
            MainService.start(RequestPermissionsActivity.this);
        }
//        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ScreenCapture.onActivityResult(this, requestCode, resultCode, data)) {
            startMainService();
        }else{
            startMainService();
        }
    }

}
