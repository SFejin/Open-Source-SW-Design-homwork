package com.example.medicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private long userId;
    private String name;
    private String role;

    private TextView textUserInfo;
    private Button btnSearch, btnReview, btnAdmin, btnLogout;
    private Button btnSchedule;
    private Button btnIntakeRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getLongExtra("userId", -1L);
        name = getIntent().getStringExtra("name");
        role = getIntent().getStringExtra("role");

        textUserInfo = findViewById(R.id.textUserInfo);
        btnSearch = findViewById(R.id.btnSearch);
        btnReview = findViewById(R.id.btnReview);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnLogout = findViewById(R.id.btnLogout);

        if (role == null) role = "GUEST";

        String displayName = name == null ? "비회원" : name;
        textUserInfo.setText(
                "Smart Medication\n\n" + "사용자: " + displayName + "\n" + "권한: " + role
        );

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btnSchedule = findViewById(R.id.btnSchedule);

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        });
        btnIntakeRecord = findViewById(R.id.btnIntakeRecord);

        btnIntakeRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, IntakeRecordActivity.class);
            startActivity(intent);
        });
    }
}