package com.example.medicationapp.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.medicationapp.local.AppDatabase;
import com.example.medicationapp.local.IntakeRecordEntity;

public class IntakeActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int scheduleId = intent.getIntExtra("scheduleId", -1);
        String itemName = intent.getStringExtra("itemName");

        if (itemName == null) {
            itemName = "알 수 없음";
        }

        AppDatabase db = AppDatabase.getInstance(context);

        IntakeRecordEntity record = new IntakeRecordEntity();
        record.itemName = itemName;
        record.taken = true;
        record.intakeTimeMillis = System.currentTimeMillis();
        record.scheduleId = scheduleId;

        db.intakeRecordDao().insert(record);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager != null && scheduleId != -1) {
            manager.cancel(scheduleId);
        }

        Toast.makeText(context, itemName + " 복용 기록이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
}