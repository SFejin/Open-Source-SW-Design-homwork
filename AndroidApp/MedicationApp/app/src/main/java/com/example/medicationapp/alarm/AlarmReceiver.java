package com.example.medicationapp.alarm;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.medicationapp.R;
import com.example.medicationapp.local.AppDatabase;
import com.example.medicationapp.local.ScheduleEntity;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "medication_alarm_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        int scheduleId = intent.getIntExtra("scheduleId", -1);
        String itemName = intent.getStringExtra("itemName");

        showNotification(context, scheduleId, itemName);

        AppDatabase db = AppDatabase.getInstance(context);
        ScheduleEntity schedule = db.scheduleDao().getScheduleById(scheduleId);

        if (schedule != null && schedule.enabled) {
            AlarmScheduler.scheduleNextAlarm(context, schedule);
        }
    }

    private void showNotification(Context context, int scheduleId, String itemName) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("복약 알림")
                        .setContentText(itemName + " 복용할 시간입니다.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager != null) {
            manager.notify(scheduleId, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "복약 알림",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}