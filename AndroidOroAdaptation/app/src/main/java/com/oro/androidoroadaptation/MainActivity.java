package com.oro.androidoroadaptation;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
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
                Log.d("wxj", "MainActivity :onClick: ");
            }
        });

        Button btnStaticBroadcastWithSignature = findViewById(R.id.btn_broadcast_with_signature);
        btnStaticBroadcastWithSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Constants.BROADCAST_TEST);
                Log.d("wxj", "MainActivity :onClick: permission");
                sendBroadcast(i,"com.oro.androidoroadaptation.signature.permission");
            }
        });

        Button btnAddShortcutBroadcast = findViewById(R.id.btn_add_shortcut_broadcast);
        btnAddShortcutBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShortcut("123");
            }
        });

        Button btnAddShortcutAndroidOro = findViewById(R.id.btn_add_shortcut_android_oro);
        btnAddShortcutAndroidOro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShortcutOro("123");
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(reciver);
        super.onDestroy();
    }

    /**
     * oro以下，添加快捷方式的方法
     * @param name
     */
    private void addShortcut(String name) {
        Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 不允许重复创建
        // 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
        addShortcutIntent.putExtra("duplicate", false);

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(MainActivity.this,
                R.drawable.ic_launcher_background));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(MainActivity.this, MainActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        sendBroadcast(addShortcutIntent);
    }

    /**
     * oro 以上，添加快捷方式的方法
     * @param name
     */
    private void addShortcutOro(String name) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ShortcutManager scm = (ShortcutManager) getSystemService(SHORTCUT_SERVICE);
            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            launcherIntent.setClass(MainActivity.this, MainActivity.class);
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);


            ShortcutInfo  si = new ShortcutInfo.Builder(this, "dataroam")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_background))
                        .setShortLabel(name)
                        .setIntent(launcherIntent)
                        .build();
            scm.requestPinShortcut(si, null);
        }
    }
}
