package com.oro.androidoroadaptation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class NotifactionHelper {

/**
 * 获取有渠道的通知，android oro 以上以下，都可以使用
 * @param c
 * @return
 */
public static Notification getNotifactionWithChannel(Context c){
    // 通知渠道的id String id = "my_channel_01"; // 用户可以看到的通知渠道的名字.
    Notification.Builder b = new Notification.Builder(c).setSmallIcon(R.drawable.ic_launcher_background);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //初始化channel的基本参数
        String name = "notifation_name";
        String id = "notifation_id";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        //创建channel
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);

        //同之前的方式，build
        b.setContentText("You've received new messages.channel name=" + name )
                .setContentTitle("AndroidOro New Message")
                .setChannelId(id);
    } else{
        //如果不是android o的机器，修改一下他的文案，这个算是适配的工作
        b.setContentText("You've received new messages,but Version number below AndroidOro")
                .setContentTitle("New Message");
    }
    Notification notification = b.build();
    return notification;
}

    /**
     * 获取没有渠道的通知，android oro 以下，才可以使用
     * @param c
     * @return
     */
    public static Notification getNotifactionWithoutChannel(Context c){
        Notification.Builder b = new Notification.Builder(c)
                .setContentTitle("New Message")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("You've received new messages,without channel");
        Notification notification = b.build();
        return notification;
    }
}
