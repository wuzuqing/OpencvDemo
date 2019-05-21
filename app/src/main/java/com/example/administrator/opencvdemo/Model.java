package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;

import org.opencv.core.Rect;

public  class Model {
        private Rect rect;
        private String result;
        private Bitmap bitmap;

        public Model(Rect rect, String result, Bitmap bitmap) {
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

        public Model(Rect rect, String result) {
            this.rect = rect;
            this.result = result;
        }

        public Rect getRect() {
            return rect;
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
            return "Model{" +
                    "rect=" + rect.toString() +
                    ", result='" + result + '\'' +
                    '}';
        }
    }
