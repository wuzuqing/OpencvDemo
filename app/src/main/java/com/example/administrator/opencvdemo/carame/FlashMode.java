package com.example.administrator.opencvdemo.carame;

import android.annotation.TargetApi;
import android.hardware.camera2.CaptureRequest;

@TargetApi(21)
public enum FlashMode {
    AUTO(CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH, CaptureRequest.FLASH_MODE_OFF),
    CLOSE(CaptureRequest.CONTROL_AE_MODE_OFF, CaptureRequest.FLASH_MODE_OFF),
    OPEN(CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH, CaptureRequest.FLASH_MODE_OFF),
    KEEP_OPEN(CaptureRequest.CONTROL_AE_MODE_ON, CaptureRequest.FLASH_MODE_TORCH);
    private int controlMode;
    private int flashModel;

    FlashMode(int controlMode, int flashModel) {
        this.controlMode = controlMode;
        this.flashModel = flashModel;
    }

    public int getControlMode() {
        return controlMode;
    }

    public int getFlashModel() {
        return flashModel;
    }
}
