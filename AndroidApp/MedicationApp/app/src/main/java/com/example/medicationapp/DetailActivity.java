package com.example.medicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.api.ApiClient;
import com.example.medicationapp.api.ApiService;
import com.example.medicationapp.dto.MedicationDetailResponse;
import com.example.medicationapp.dto.SupplementDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView textType, textName, textEffect, textMethod, textCaution;
    private Button btnReview;

    private ApiService apiService;

    private String type;
    private Long id;
    private long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textType = findViewById(R.id.textType);
        textName = findViewById(R.id.textName);
        textEffect = findViewById(R.id.textEffect);
        textMethod = findViewById(R.id.textMethod);
        textCaution = findViewById(R.id.textCaution);
        btnReview = findViewById(R.id.btnReview);

        apiService = ApiClient.getClient().create(ApiService.class);

        type = getIntent().getStringExtra("type");
        id = getIntent().getLongExtra("id", -1L);
        userId = getIntent().getLongExtra("userId", -1L);
        role = getIntent().getStringExtra("role");

        if ("MEDICATION".equals(type)) {
            loadMedicationDetail(id);
        } else {
            loadSupplementDetail(id);
        }

        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("id", id);
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });
    }

    private void loadMedicationDetail(Long id) {
        apiService.getMedicationDetail(id).enqueue(new Callback<MedicationDetailResponse>() {
            @Override
            public void onResponse(Call<MedicationDetailResponse> call, Response<MedicationDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MedicationDetailResponse item = response.body();

                    textType.setText("분류: 약");
                    textName.setText(item.getName());
                    textEffect.setText("효능\n" + item.getEffect());
                    textMethod.setText("복용 방법\n" + item.getDosage());
                    textCaution.setText("주의사항\n" + item.getCaution());
                } else {
                    Toast.makeText(DetailActivity.this, "상세 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MedicationDetailResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSupplementDetail(Long id) {
        apiService.getSupplementDetail(id).enqueue(new Callback<SupplementDetailResponse>() {
            @Override
            public void onResponse(Call<SupplementDetailResponse> call, Response<SupplementDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupplementDetailResponse item = response.body();

                    textType.setText("분류: 영양제");
                    textName.setText(item.getName());
                    textEffect.setText("효능\n" + item.getEffect());
                    textMethod.setText("섭취 방법\n" + item.getIntakeMethod());
                    textCaution.setText("주의사항\n" + item.getCaution());
                } else {
                    Toast.makeText(DetailActivity.this, "상세 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SupplementDetailResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}