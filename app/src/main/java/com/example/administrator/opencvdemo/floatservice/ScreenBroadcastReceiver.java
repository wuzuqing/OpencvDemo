package com.example.administrator.opencvdemo.floatservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
* @author 吴祖清
* @version $Rev$
* @createTime 2018/4/10 17:55
* @des ${TODO}
* @updateAuthor $Author$
* @updateDate 2018/4/10$
* @updateDes ${TODO}
*/

public class ScreenBroadcastReceiver extends BroadcastReceiver {
   private String action = null;


   @Override
   public void onReceive(Context context, Intent intent) {
       action = intent.getAction();
       if (Intent.ACTION_SCREEN_ON.equals(action)) {
           // 开屏

       } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

           // 锁屏
       } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

           // 解锁
       }
   }
}
