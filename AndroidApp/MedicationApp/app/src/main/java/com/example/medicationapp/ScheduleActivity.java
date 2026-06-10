package com.example.medicationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.medicationapp.adapter.ScheduleAdapter;
import com.example.medicationapp.alarm.AlarmScheduler;
import com.example.medicationapp.local.AppDatabase;
import com.example.medicationapp.local.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private EditText editItemName, editCycle;
    private TimePicker timePicker;
    private Button btnSaveSchedule;
    private ListView listSchedules;

    private AppDatabase db;

    private List<ScheduleEntity> scheduleList = new ArrayList<>();
    private ScheduleAdapter adapter;

    private static final int REQ_NOTIFICATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        editItemName = findViewById(R.id.editItemName);
        editCycle = findViewById(R.id.editCycle);
        timePicker = findViewById(R.id.timePicker);
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule);
        listSchedules = findViewById(R.id.listSchedules);

        timePicker.setIs24HourView(true);

        db = AppDatabase.getInstance(this);

        adapter = new ScheduleAdapter(
                this,
                scheduleList,
                schedule -> {
                    AlarmScheduler.cancelAlarm(this, schedule.scheduleId);
                    db.scheduleDao().deleteById(schedule.scheduleId);

                    Toast.makeText(this, "일정과 알림이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                    loadSchedules();
                }
        );

        listSchedules.setAdapter(adapter);

        requestNotificationPermission();
        requestExactAlarmPermissionIfNeeded();

        btnSaveSchedule.setOnClickListener(v -> saveSchedule());

        loadSchedules();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIFICATION
                );
            }
        }
    }

    private void requestExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            android.app.AlarmManager alarmManager =
                    (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void saveSchedule() {
        String itemName = editItemName.getText().toString().trim();
        String cycle = editCycle.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(this, "약/영양제 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.itemName = itemName;
        schedule.hour = hour;
        schedule.minute = minute;
        schedule.cycle = cycle.isEmpty() ? "매일" : cycle;
        schedule.enabled = true;

        long id = db.scheduleDao().insert(schedule);
        schedule.scheduleId = (int) id;

        AlarmScheduler.scheduleNextAlarm(this, schedule);

        Toast.makeText(this, "일정 저장 및 알림 등록 완료", Toast.LENGTH_SHORT).show();

        editItemName.setText("");
        loadSchedules();
    }

    private void loadSchedules() {
        scheduleList.clear();
        scheduleList.addAll(db.scheduleDao().getAllSchedules());
        adapter.notifyDataSetChanged();
    }
}