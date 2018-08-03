package com.oro.androidoroadaptation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.oro.androidoroadaptation.MainActivity;

public class ServiceOrdinary extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wxj", "ServiceOrdinary :onCreate: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("wxj", "ServiceOrdinary :onUnbind: ");
        return super.onUnbind(intent);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wxj", "ServiceOrdinary :onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wxj", "ServiceOrdinary :onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("wxj", "ServiceOrdinary :onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("wxj", "ServiceOrdinary :onStart: ");
        super.onStart(intent, startId);
    }


}
