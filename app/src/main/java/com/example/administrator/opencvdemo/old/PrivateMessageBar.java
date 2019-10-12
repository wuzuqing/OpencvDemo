package com.example.administrator.opencvdemo.old;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.opencvdemo.R;

/**
 * 作者：士元
 * 时间：2019/5/22 16:59
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class PrivateMessageBar extends FrameLayout {
    private ImageView vIvAvatar;
    private TextView vTvName, vTvContent;

    public PrivateMessageBar(Context context) {
        super(context);
        initView();
    }

    public PrivateMessageBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PrivateMessageBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.private_message_bar, this, true);
        vTvName = findViewById(R.id.private_message_bar_name);
        vTvContent = findViewById(R.id.private_message_bar_content);
        vIvAvatar = findViewById(R.id.private_message_bar_avatar);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(1,60,getResources().getDisplayMetrics())));
        setBackgroundColor(Color.BLUE);
    }

    public ImageView getIvAvatar() {
        return vIvAvatar;
    }

    public TextView getTvName() {
        return vTvName;
    }


    public TextView getTvContent() {
        return vTvContent;
    }

    private ViewGroup parent;
    private boolean isShow;

    public void show(View view) {
        if (isShow) {
            return;
        }
        parent = findSuitableParent(view);
        if (parent == null) {
           return;
        }
        isShow = true;
        parent.addView(this);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewHeight = getHeight();
                showAnimator();
            }
        });
    }

    private int viewHeight = 0;
    private static final int SHOW_OR_HIDE_DURATION = 360;
    private static final int REMAIN_DURATION = 3000;
    private int top = 80;
    private void showAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", -viewHeight, top);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(SHOW_OR_HIDE_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isCancel) {
                    removeSelf();
                } else {
                    postDelayed(autoHideTask, REMAIN_DURATION);
                }
            }
        });
        animator.start();
    }

    private boolean isCancel;

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    private Runnable autoHideTask = new Runnable() {
        @Override
        public void run() {
            if (isCancel) {
                removeSelf();
                return;
            }
            hideAnimator();
        }
    };

    private void hideAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", top, -viewHeight);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(SHOW_OR_HIDE_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeSelf();
            }
        });
        animator.start();
    }

    private void removeSelf() {
        if (parent != null) {
            parent.removeView(this);
        }
        if (vHideBarListener!=null){
            vHideBarListener.onHide(this);
        }
        setOnClickListener(null);
    }


    private OnHideBarListener vHideBarListener;

    public void setHideBarListener(OnHideBarListener hideBarListener) {
        vHideBarListener = hideBarListener;
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }
}
