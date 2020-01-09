// package com.example.administrator.opencvdemo.old;
//
// import android.content.Context;
// import android.graphics.Bitmap;
// import android.graphics.Canvas;
// import android.graphics.Color;
// import android.graphics.Paint;
// import android.graphics.PorterDuff;
// import android.graphics.PorterDuffXfermode;
// import android.graphics.RectF;
// import android.graphics.Xfermode;
// import android.util.AttributeSet;
// import android.view.View;
//
// public class MarkView1 extends View {
//
//     private int markColor;
//     private float ratio;
//     private Paint vPaint;
//     private Xfermode mXferMode;
//
//     private Bitmap fullBitmap;
//     private Bitmap contentBitmap;
//     private RectF vContentRect;
//
//
//     public MarkView1(Context context) {
//         this(context, null);
//     }
//
//     public MarkView1(Context context, AttributeSet attrs) {
//         this(context, attrs, 0);
//     }
//
//     public MarkView1(Context context, AttributeSet attrs, int defStyleAttr) {
//         super(context, attrs, defStyleAttr);
//         initAttrs(context, attrs);
//     }
//
//     private void initAttrs(Context context, AttributeSet attrs) {
//         markColor = 0xff7d7d7d;
//         ratio = 64 / 40f;
//         vPaint = new Paint();
//         vContentRect = new RectF();
//         vPaint.setColor(markColor);
//         mXferMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
//         vPaint.setAntiAlias(true);//设置抗锯齿
//         vPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
//         vPaint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置
//     }
//
//
//     @Override
//     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//         super.onSizeChanged(w, h, oldw, oldh);
//         float viewRatio = w / h;
//
//         int contentWidth = 0;
//         int contentHeight = 0;
//
//         if (viewRatio < ratio) {
//             contentHeight = (int) (h * 0.8f);
//             contentWidth = (int) (contentHeight * ratio);
//         } else {
//             contentWidth = (int) (w * 0.8f);
//             contentHeight = (int) (contentWidth / ratio);
//         }
//
//         vContentRect.left = (w - contentWidth) / 2;
//         vContentRect.top = (h - contentHeight) / 2;
//         vContentRect.right = vContentRect.left + contentWidth;
//         vContentRect.bottom = vContentRect.top + contentHeight;
//         createFullBitmap(w, h);
//         createContentBitmap(contentWidth, contentHeight);
//     }
//
//     private void createContentBitmap(int contentWidth, int contentHeight) {
//         contentBitmap = Bitmap.createBitmap(contentWidth, contentHeight, Bitmap.Config.ARGB_8888);
//         Canvas canvas = new Canvas(contentBitmap);
//         vPaint.setColor(Color.BLACK);
//         RectF rectF = new RectF();
//         rectF.set(0, 0, contentWidth, contentHeight);
//         canvas.drawRoundRect(rectF, 8, 8, vPaint);
//     }
//
//     private void createFullBitmap(int w, int h) {
//         fullBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//         Canvas canvas = new Canvas(fullBitmap);
//         canvas.drawColor(markColor);
//     }
//
//     @Override
//     protected void onDraw(Canvas canvas) {
//         super.onDraw(canvas);
//         int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), vPaint, Canvas.ALL_SAVE_FLAG);
//         vPaint.reset();
//         canvas.drawBitmap(fullBitmap, 0, 0, vPaint);
//         vPaint.setXfermode(mXferMode);
//         canvas.drawBitmap(contentBitmap, vContentRect.left, vContentRect.top, vPaint);
//         vPaint.setXfermode(null);
//         canvas.restoreToCount(sc);
//     }
//
//     public RectF getContentRect() {
//         return vContentRect;
//     }
// }
