package com.example.administrator.opencvdemo.old;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 作者：士元
 * 时间：2019/5/22 17:58
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class PrivateMessageBarManager {

    private static PrivateMessageBarManager instance = new PrivateMessageBarManager();
    private Set<PrivateMessageBar> showList;
    private LinkedList<PrivateMessageBar> cacheList;
    private int MAX_CACHE_VIEW_SIZE = 3;
    private Handler vHandler;

    private PrivateMessageBarManager() {
        showList = new HashSet<>();
        cacheList = new LinkedList<>();
        vHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                cacheList.clear();
            }
        };
    }

    private OnHideBarListener vOnHideBarListener = new OnHideBarListener() {
        @Override
        public void onHide(PrivateMessageBar messageBar) {
            if (showList.isEmpty()) {
                vHandler.sendEmptyMessageDelayed(0, 10000);
            }
            if (cacheList.size() < MAX_CACHE_VIEW_SIZE) {
                cacheList.add(messageBar);
            }
        }
    };


    public static PrivateMessageBarManager getInstance() {
        return instance;
    }

    public  void show(View view, String objId, String avatar, String name, String content, final View.OnClickListener listener) {
        PrivateMessageBar messageBar = null;
        if (cacheList.isEmpty()) {
            messageBar = new PrivateMessageBar(view.getContext());
        } else {
            messageBar = cacheList.removeFirst();
        }
        setCancel();
        messageBar.setTag(objId);
        messageBar.getTvContent().setText(content);
        messageBar.getTvName().setText(name);
        messageBar.setHideBarListener(vOnHideBarListener);
        messageBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        messageBar.show(view);
    }

    private void setCancel() {
        if (!showList.isEmpty()) {
            for (PrivateMessageBar messageBar : showList) {
                messageBar.setCancel(true);
            }
        }
    }

}
