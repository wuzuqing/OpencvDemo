package com.example.administrator.opencvdemo.old;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MarkView extends View {

    private int markColor;
    private float ratio;
    private Paint vPaint;


    public MarkView(Context context) {
        this(context, null);
    }

    public MarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }
    private PorterDuffXfermode xfermode;
    private void initAttrs(Context context, AttributeSet attrs) {
        markColor = 0x80000000;
        ratio = 64 / 40f;
        vPaint = new Paint();
        vContentRect = new Rect();
        vPaint.setColor(markColor);
        xfermode=new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        vPaint.setAntiAlias(true);//设置抗锯齿
        vPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        vPaint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置
    }

    private Rect vContentRect;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boolean isLar = w > h;
        if (isLar) {
            int temp = w;
            w = h;
            h = temp;
        }
        int contentHeight = (int) (h * 0.8f);
        int contentWidth = (int) (contentHeight / ratio);
        vContentRect.left = (w - contentWidth) / 2;
        vContentRect.top = (h - contentHeight) / 2;
        vContentRect.right = vContentRect.left + contentWidth;
        vContentRect.bottom = vContentRect.top + contentHeight;
        vPath.reset();
        vPath.moveTo(0,0);
        vPath.lineTo(w,0);
        vPath.lineTo(w,h);
        vPath.lineTo(0,h);
        vPath.lineTo(vContentRect.left,vContentRect.bottom);
        vPath.lineTo(vContentRect.right,vContentRect.bottom);
        vPath.lineTo(vContentRect.right,vContentRect.top);
        vPath.lineTo(vContentRect.left,vContentRect.top);
        vPath.lineTo(vContentRect.left,vContentRect.bottom);
        vPath.close();
        vPath.lineTo(0,0);
        vPath.lineTo(0,h);
        vPath.lineTo(vContentRect.left,vContentRect.bottom);
        vPath.close();
    }
    Path vPath = new Path();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(vPath, vPaint);
    }
}
