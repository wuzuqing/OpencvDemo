package com.example.administrator.opencvdemo.v2;

import android.content.Context;

import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;

import java.util.List;

public class TaskState implements Constant {

    private static final TaskState sTaskState = new TaskState();

    private TaskState() {

    }

    public static TaskState get() {
        return sTaskState;
    }


    // 当前账号索引
    private int mCurrentAccountIndex;
    // 当前任务索引
    private int currentTaskIndex;
    //当前页面
    public static String mCurrentPage;

    public static boolean isWorking;
    /**
     * 任务集合
     */
    private static List<TaskElement> mTaskModelList;
    /**
     * 用户账号集合
     */
    private static List<UserInfo> mUserInfoList;

    public void init(Context context) {
        SPUtils.init(context);
//        currentTaskIndex = SPUtils.getInt(CURRENT_TASK_STEP);
        mCurrentAccountIndex = SPUtils.getInt(CURRENT_USER_INFO);
        mUserInfoList = Util.getUserInfo();
        mTaskModelList = Util.getTaskElement();
        mCurrentAccountIndex = mCurrentAccountIndex % mUserInfoList.size();
    }

    public TaskElement getTask() {

        return mTaskModelList.get(currentTaskIndex);
    }

    public void saveNextTask() {
        currentTaskIndex++;
        if (currentTaskIndex == mTaskModelList.size()) {
            saveNextUserInfo();
        }
        currentTaskIndex = currentTaskIndex % mTaskModelList.size();
        SPUtils.setInt(CURRENT_TASK_STEP, currentTaskIndex);
    }

    public void saveNextUserInfo() {
        mCurrentAccountIndex++;
        mCurrentAccountIndex = mCurrentAccountIndex % mUserInfoList.size();
        SPUtils.setInt(CURRENT_USER_INFO, mCurrentAccountIndex);
    }

    public UserInfo getUserInfo() {
        return mUserInfoList.get(mCurrentAccountIndex);
    }

    public void resetStep() {
        currentTaskIndex = 0;
        SPUtils.setInt(CURRENT_TASK_STEP, currentTaskIndex);
    }


    public void setCurrentPage(String currentPage) {
        mCurrentPage = currentPage;
        SPUtils.setString(CURRENT_PAGE, currentPage);
    }

    public static void setIsWorking(boolean isWorking) {
        TaskState.isWorking = isWorking;
        if (isWorking) {
            LogUtils.logdAndToast("开始任务");
        } else {
            LogUtils.logdAndToast("已取消任务");
        }
    }

    public static int failCount = 0;
    public static boolean needContinue;

    public static void resetFail() {
        failCount = 0;
        needContinue = false;
    }

    public static boolean check(int failCount, int maxCount) {
        boolean check = failCount > maxCount;
        needContinue = check;
        TaskState.failCount++;
        return check;
    }
}
