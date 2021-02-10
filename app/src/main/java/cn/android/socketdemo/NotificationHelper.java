package cn.android.socketdemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Random;

public class NotificationHelper {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public static void makeSocketNotification(Context context,
                                              String tickerText, String title, String content,
                                              byte[] data){

        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("data", data);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setWhen(System.currentTimeMillis())
                .build();

        notification.tickerText = tickerText;

        Random random = new Random(System.currentTimeMillis());
        int id = random.nextInt(2000000000);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
}