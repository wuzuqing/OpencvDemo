package com.example.module_orc;

import android.graphics.Bitmap;

import org.opencv.core.Rect;
import org.opencv.core.Size;

public class NormalCardDiscern extends AbsDiscern {

    public NormalCardDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        super(bitmap1, langName, callback);
        mSize = new Size(1080/3, 1920/3);
    }

    @Override
    protected boolean ignoreRect(Rect rect) {
        return false;
    }
}
