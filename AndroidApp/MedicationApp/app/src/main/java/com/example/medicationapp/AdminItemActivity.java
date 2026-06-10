package com.example.medicationapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.api.ApiClient;
import com.example.medicationapp.api.ApiService;
import com.example.medicationapp.dto.MedicationDetailResponse;
import com.example.medicationapp.dto.MedicationRequest;
import com.example.medicationapp.dto.SupplementDetailResponse;
import com.example.medicationapp.dto.SupplementRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminItemActivity extends AppCompatActivity {

    private long adminId;
    private ApiService apiService;

    private RadioButton radioMedication, radioSupplement;
    private EditText editItemId, editName, editEffect, editMethod, editCaution;
    private Button btnAddItem, btnEditItem, btnDeleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_item);

        adminId = getIntent().getLongExtra("adminId", -1L);
        apiService = ApiClient.getClient().create(ApiService.class);

        radioMedication = findViewById(R.id.radioMedication);
        radioSupplement = findViewById(R.id.radioSupplement);

        editItemId = findViewById(R.id.editItemId);
        editName = findViewById(R.id.editName);
        editEffect = findViewById(R.id.editEffect);
        editMethod = findViewById(R.id.editMethod);
        editCaution = findViewById(R.id.editCaution);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnEditItem = findViewById(R.id.btnEditItem);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);

        btnAddItem.setOnClickListener(v -> addItem());
        btnEditItem.setOnClickListener(v -> editItem());
        btnDeleteItem.setOnClickListener(v -> deleteItem());
    }

    private boolean isMedication() {
        return radioMedication.isChecked();
    }

    private boolean validateInputs(boolean needId) {
        if (needId && editItemId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editName.getText().toString().trim().isEmpty()
                || editEffect.getText().toString().trim().isEmpty()
                || editMethod.getText().toString().trim().isEmpty()
                || editCaution.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private MedicationRequest makeMedicationRequest() {
        return new MedicationRequest(
                editName.getText().toString().trim(),
                editEffect.getText().toString().trim(),
                editMethod.getText().toString().trim(),
                editCaution.getText().toString().trim()
        );
    }

    private SupplementRequest makeSupplementRequest() {
        return new SupplementRequest(
                editName.getText().toString().trim(),
                editEffect.getText().toString().trim(),
                editMethod.getText().toString().trim(),
                editCaution.getText().toString().trim()
        );
    }

    private Long getItemId() {
        return Long.parseLong(editItemId.getText().toString().trim());
    }

    private void clearInputs() {
        editItemId.setText("");
        editName.setText("");
        editEffect.setText("");
        editMethod.setText("");
        editCaution.setText("");
    }

    private void addItem() {
        if (!validateInputs(false)) return;

        if (isMedication()) {
            apiService.addMedication(adminId, makeMedicationRequest())
                    .enqueue(new Callback<MedicationDetailResponse>() {
                        @Override
                        public void onResponse(Call<MedicationDetailResponse> call, Response<MedicationDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "약 추가 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "약 추가 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MedicationDetailResponse> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            apiService.addSupplement(adminId, makeSupplementRequest())
                    .enqueue(new Callback<SupplementDetailResponse>() {
                        @Override
                        public void onResponse(Call<SupplementDetailResponse> call, Response<SupplementDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "영양제 추가 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "영양제 추가 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SupplementDetailResponse> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void editItem() {
        if (!validateInputs(true)) return;

        if (isMedication()) {
            apiService.editMedication(getItemId(), adminId, makeMedicationRequest())
                    .enqueue(new Callback<MedicationDetailResponse>() {
                        @Override
                        public void onResponse(Call<MedicationDetailResponse> call, Response<MedicationDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "약 수정 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "약 수정 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MedicationDetailResponse> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            apiService.editSupplement(getItemId(), adminId, makeSupplementRequest())
                    .enqueue(new Callback<SupplementDetailResponse>() {
                        @Override
                        public void onResponse(Call<SupplementDetailResponse> call, Response<SupplementDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "영양제 수정 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "영양제 수정 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SupplementDetailResponse> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void deleteItem() {
        if (editItemId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "삭제할 ID를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isMedication()) {
            apiService.deleteMedicationByAdmin(getItemId(), adminId)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "약 삭제 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "약 삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            apiService.deleteSupplementByAdmin(getItemId(), adminId)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AdminItemActivity.this, "영양제 삭제 성공", Toast.LENGTH_SHORT).show();
                                clearInputs();
                            } else {
                                Toast.makeText(AdminItemActivity.this, "영양제 삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(AdminItemActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}