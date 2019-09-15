package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView view1 = view.findViewById(R.id.img);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Bitmap bitmap = arguments.getParcelable("bitmap");
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bitmap_preview, container, false);
        return inflate;
    }
}
