package com.oro.androidoroadaptation.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.oro.androidoroadaptation.NotifactionHelper;

public class ServiceForAndroidOro extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wxj", "ServiceForAndroidOro :onCreate: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("wxj", "ServiceForAndroidOro :onUnbind: ");
        return super.onUnbind(intent);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wxj", "ServiceForAndroidOro :onBind: ");
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wxj", "ServiceForAndroidOro :onStartCommand: ");
        //这里调用startForeground，在前台生成一个通知栏，就可以了
        Notification notification = NotifactionHelper.getNotifactionWithChannel(this);
        startForeground(1,notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("wxj", "ServiceForAndroidOro :onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("wxj", "ServiceForAndroidOro :onStart: ");
        super.onStart(intent, startId);
    }
}
