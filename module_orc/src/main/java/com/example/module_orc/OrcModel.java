package com.example.module_orc;

import android.graphics.Bitmap;

import org.opencv.core.Rect;

public class OrcModel {
    private Rect rect;
    private String result;
    private Bitmap bitmap;

    public OrcModel(Rect rect, String result, Bitmap bitmap) {
        this.rect = rect;
        this.result = result;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public OrcModel(Rect rect, String result) {
        this.rect = rect;
        this.result = result;
    }

    public OrcModel() {
    }

    public Rect getRect() {
        return rect;
    }

    public Rect getSmallRect() {
        Rect clone = rect.clone();
        clone.x += 5;
//        clone.y += 2;
//        clone.width -= 8;
//        clone.height -= 5;
        return clone;
    }


    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OrcModel{" +
                "rect=" + rect +
                ", result='" + result + '\'' +
                '}';
    }
}
