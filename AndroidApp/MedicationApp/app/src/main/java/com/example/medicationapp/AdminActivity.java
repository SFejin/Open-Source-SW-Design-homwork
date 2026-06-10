package com.example.medicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private long userId;
    private String role;

    private Button btnManageUsers, btnManageReviews, btnManageItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getLongExtra("userId", -1L);
        role = getIntent().getStringExtra("role");

        if (!"ADMIN".equals(role)) {
            Toast.makeText(this, "관리자만 접근할 수 있습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_admin);

        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageReviews = findViewById(R.id.btnManageReviews);
        btnManageItems = findViewById(R.id.btnManageItems);

        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminUserActivity.class);
            intent.putExtra("adminId", userId);
            startActivity(intent);
        });

        btnManageReviews.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminReviewActivity.class);
            intent.putExtra("adminId", userId);
            startActivity(intent);
        });

        btnManageItems.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminItemActivity.class);
            intent.putExtra("adminId", userId);
            startActivity(intent);
        });
    }
}