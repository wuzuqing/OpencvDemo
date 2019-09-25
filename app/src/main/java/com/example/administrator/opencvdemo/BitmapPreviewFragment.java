package com.example.administrator.opencvdemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.module_orc.OrcConfig;

public class BitmapPreviewFragment extends Fragment {

    public static void show(FragmentActivity activity, Bitmap bitmap) {
        BitmapPreviewFragment fragment = new BitmapPreviewFragment();
        Bundle args = new Bundle();
        args.putParcelable("bitmap", bitmap);
        fragment.setArguments(args);
        activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String TAG = "BitmapPreviewFragment";
    Bitmap bitmap;
    int widthPixels;
    int heightPixels;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView view1 = view.findViewById(R.id.img);
        Bundle arguments = getArguments();
        if (arguments != null) {
            bitmap = arguments.getParcelable("bitmap");
            view1.setImageBitmap(bitmap);
        }
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.getSupportFragmentManager().beginTransaction().remove(BitmapPreviewFragment.this).commitAllowingStateLoss();
                }
            }
        });
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        view1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    if (bitmap != null) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        Log.d(TAG, "onTouch: bitmap:"+width + "x"+height);
                        int color = bitmap.getPixel((int)( rawX/OrcConfig.topColorXishu), (int) (rawY/OrcConfig.topColorXishu));
//                        Color.
                        float roaitX = rawX / widthPixels;
                        float roaitY = rawY / heightPixels;
                        Log.d(TAG, "onTouch: " + rawX + "," + rawY + "  width:" + widthPixels + "x" + heightPixels +
                                " color:" + ("#"+Integer.toHexString(color)) + " roait:" + roaitX + "," + roaitY);
                    }
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bitmap_preview, container, false);
        return inflate;
    }
}
