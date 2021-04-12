package edu.band148.wgumanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String PRIMARY_CHANNEL_ID = "notification_channel";
    static int NOTIFICATION_ID = 0;
    private NotificationManager notifyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("notification"),Toast.LENGTH_LONG).show();
        notifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Course Notification")
                .setContentText(intent.getStringExtra("notification"))
                .setSmallIcon(R.drawable.ic_book);
        notifyManager.notify(NOTIFICATION_ID++, notifyBuilder.build());
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Course Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifications for Courses");
            notifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
