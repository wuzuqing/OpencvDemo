package com.example.module_orc;

import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class OpenCVHelper {

    private static final String TAG = "OpenCVHelper";

    public static void init(Context context) {
        init(context, new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.i(TAG, "OpenCV loaded successfully");

                        /* Now enable camera view to start receiving frames */
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        });
    }

    public static void init(Context context, LoaderCallbackInterface Callback) {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, Callback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            Callback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


}
