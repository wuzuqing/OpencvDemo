package com.example.administrator.opencvdemo.notroot;

import java.util.List;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

/**
 * 作者：士元
 * 时间：2019/10/18 0018 18:37
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class ServiceHelper {
    @SuppressLint("StaticFieldLeak")
    private static ServiceHelper mInstance;
    private AccessibilityManager mAccessibilityManager;
    private Context mContext;


    public static ServiceHelper getInstance() {
        if (mInstance == null) {
            mInstance = new ServiceHelper();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }


    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    private boolean checkAccessibilityEnabled(String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            EventHelper.log(info.getId() + " , " + serviceName);
            String serviceId = info.getId();
            final int dot = serviceId.lastIndexOf(".");
            if (dot > 0) {
                serviceId = serviceId.substring(serviceId.lastIndexOf(".") + 1); // strip the package name
            }
            if (TextUtils.equals(serviceId, serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前往开启辅助服务界面
     */
    public boolean goAccess() {
        if (checkAccessibilityEnabled(TaskAccessibilityService.class.getSimpleName())) {
            Toast.makeText(mContext, "服务已启动", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        return true;
    }
}
