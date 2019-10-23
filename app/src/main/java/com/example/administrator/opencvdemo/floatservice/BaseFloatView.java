package com.example.administrator.opencvdemo.floatservice;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 作者：士元
 * 时间：2019/10/23 0023 10:03
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public abstract class BaseFloatView extends FrameLayout {

    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private int halfViewSize;
    //状态栏高度.
    private int statusBarHeight = -1;

    private boolean isShowFloatView;

    public BaseFloatView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BaseFloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseFloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this, true);
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        halfViewSize = 120 / 2;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        bindView();
    }

    private static boolean rom() {
        return Build.MANUFACTURER.equals("Xiaomi");
    }

    public void showFloatView() {
        if (isShowFloatView) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.params.type = 2038;
            } else {
                this.params.type = 2002;
            }
        } else if (rom()) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Build.VERSION.SDK_INT >= 26) {
                    this.params.type = 2038;
                } else {
                    this.params.type = 2002;
                }
            } else {
                this.params.type = 2002;
            }
        } else {
            this.params.type = 2002;
        }
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = -2;
        params.height = -2;

        params.format = 1;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 表示Window不需要获取焦点
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;//可以监听MotionEvent的ACTION_OUTSIDE事件
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版限制--即允许在可见的屏幕之外

        params.alpha = 1.0f;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        params.gravity = Gravity.START | Gravity.TOP;   //调整悬浮窗口至左上角

        try {
            windowManager.addView(this, params);
            isShowFloatView = true;
        } catch (Exception e) {
            windowManager.removeView(this);
            e.printStackTrace();
        }
    }

    public void hideFloatView() {
        if (isShowFloatView) {
            isShowFloatView = false;
            windowManager.removeView(this);
        }
    }

    @Override
    protected final void onFinishInflate() {
        super.onFinishInflate();
    }

    protected abstract int getLayoutId();

    protected abstract void bindView();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                params.x = (int) event.getRawX() - halfViewSize;
                params.y = (int) event.getRawY() - halfViewSize - statusBarHeight;
                windowManager.updateViewLayout(this, params);
            case MotionEvent.ACTION_UP:
                break;
        }
        Log.d(TAG, "onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
    }

    private static final String TAG = "BaseFloatView";
}
