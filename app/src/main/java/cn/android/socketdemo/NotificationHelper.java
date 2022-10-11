package cn.android.socketdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Random;

public class NotificationHelper {
    public static void makeSocketNotification(Context context, String title, String content, byte[] data){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("data", data);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Random random = new Random(System.currentTimeMillis());
        int id = random.nextInt(2000000000);

        Notification notification = new Notification.Builder(context, String.valueOf(id))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(String.valueOf(id), "123", importance);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(id, notification);
    }
}