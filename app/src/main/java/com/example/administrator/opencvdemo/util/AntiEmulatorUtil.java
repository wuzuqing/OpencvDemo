package com.example.administrator.opencvdemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 作者：士元
 * 时间：2019/10/23 0023 14:19
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class AntiEmulatorUtil {
    private static Boolean isEmulator;
    public static boolean isEmulator(Context mContext) {
        if (isEmulator!=null){
            return isEmulator;
        }
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canResolveIntent = intent.resolveActivity(mContext.getPackageManager()) != null;
        isEmulator = Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.toLowerCase().contains("vbox")
            || Build.FINGERPRINT.toLowerCase().contains("test-keys")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.SERIAL.equalsIgnoreCase("unknown")
            || Build.SERIAL.equalsIgnoreCase("android")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk".equals(Build.PRODUCT)
            || ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE))
            .getNetworkOperatorName().toLowerCase().equals("android")
            || !canResolveIntent;
        return isEmulator;
    }
}
