package com.oro.androidoroadaptation;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.oro.androidoroadaptation.reciver.DynamicReciver;
import com.oro.androidoroadaptation.service.ServiceForAndroidOro;
import com.oro.androidoroadaptation.service.ServiceOrdinary;

import java.security.Provider;
import java.util.Locale;

public class MainActivity extends Activity {
    DynamicReciver reciver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reciver = new DynamicReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_TEST);
        registerReceiver(reciver,filter);

        //启动带channel的通知栏，android Oro能使用
        Button btnNotifactionWithChannel = findViewById(R.id.btn_notifaction_with_channel);
        btnNotifactionWithChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification notification =  NotifactionHelper.getNotifactionWithChannel(MainActivity.this);
                NotificationManager mNotificationManager = (NotificationManager) MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1,notification);
            }
        });

        //启动不带channel的通知栏，android Oro不能使用
        Button btnNotifactionWithoutChannel = findViewById(R.id.btn_notifaction_without_channel);
        btnNotifactionWithoutChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification notification =  NotifactionHelper.getNotifactionWithoutChannel(MainActivity.this);
                NotificationManager mNotificationManager = (NotificationManager) MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1,notification);
            }
        });

        //用startService方式，在前台启动service
        //无论版本，都可以使用，但是android oro以上，如果退出当前activity，service很快会销毁
        Button btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ServiceOrdinary.class);
                startService(i);
            }
        });

        //用startService方式，延迟70秒，启动service
        //android oro以上，会crash
        //android oro以下，正常使用
        Button btnStartServiceBackgound = findViewById(R.id.btn_start_service_backgound);
        btnStartServiceBackgound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("wxj", "ServiceOrdinary :run: startService ");
                        Intent i = new Intent();
                        i.setClass(MainActivity.this, ServiceOrdinary.class);
                        startService(i);
                    }
                },70 * 1000);
            }
        });

        //启动service,Oro
        Button btnStartServiceforeGroud = findViewById(R.id.btn_start_service_fore_gound);
        btnStartServiceforeGroud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ServiceForAndroidOro.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(i);
                }
            }
        });

        // 启动发送广播
        // android oro以上，只有动态注册可以收到广播
        // android oro以下，动态静态都可以收到广播
        Button btnStaticBroadcast = findViewById(R.id.btn_broadcast);
        btnStaticBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Constants.BROADCAST_TEST);
                sendBroadcast(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(reciver);
        super.onDestroy();
    }
}
