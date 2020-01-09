package com.example.administrator.opencvdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Util;

/**
 * 作者：士元
 * 时间：2019/10/23 0023 19:54
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class BitmapPreviewActivity extends AppCompatActivity {

    public static void show(Context context){
        Intent intent = new Intent(context,BitmapPreviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bitmap bitmap =   Util.getBitmap() ;
        if (bitmap==null){
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preivew);
        ImageView view = findViewById(R.id.img);
        view.setImageBitmap(bitmap);
    }

}
