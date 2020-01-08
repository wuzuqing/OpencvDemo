package com.example.administrator.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.Util;

public class GetPointColorActivity extends NoAnimatorActivity {
    Bitmap mBitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_point);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bbb);
    }


    private String color;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        final int x = (int) event.getRawX();
                        final int y = (int) event.getRawY();
                        color = Util.getColor(mBitmap, x, y);
                        LogUtils.logd("color:" + color + " x:" + x + " Y:" + y);
                        if (TextUtils.isEmpty(color)) return;

                    }
                });
                break;
        }
        return super.onTouchEvent(event);
    }

}
