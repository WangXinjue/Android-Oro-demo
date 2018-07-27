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
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder b = new Notification.Builder(c).setSmallIcon(R.drawable.ic_launcher_background);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = "notifation_name";
            String id = "notifation_id";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
            b.setContentText("You've received new messages.channel name=" + name )
                    .setContentTitle("AndroidOro New Message")
                    .setChannelId(id);
        } else{
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
