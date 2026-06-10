package com.example.medicationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.api.ApiClient;
import com.example.medicationapp.api.ApiService;
import com.example.medicationapp.dto.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserActivity extends AppCompatActivity {

    private long adminId;
    private ApiService apiService;

    private ListView listUsers;
    private TextView textSelectedUser;
    private Button btnMakeAdmin, btnMakeUser, btnBlockReview, btnUnblockReview, btnDeleteUser;

    private List<UserResponse> userList = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private UserResponse selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        adminId = getIntent().getLongExtra("adminId", -1L);
        apiService = ApiClient.getClient().create(ApiService.class);

        listUsers = findViewById(R.id.listUsers);
        textSelectedUser = findViewById(R.id.textSelectedUser);
        btnMakeAdmin = findViewById(R.id.btnMakeAdmin);
        btnMakeUser = findViewById(R.id.btnMakeUser);
        btnBlockReview = findViewById(R.id.btnBlockReview);
        btnUnblockReview = findViewById(R.id.btnUnblockReview);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listUsers.setAdapter(adapter);

        listUsers.setOnItemClickListener((parent, view, position, id) -> {
            selectedUser = userList.get(position);
            textSelectedUser.setText(
                    "선택된 사용자: " + selectedUser.getName() +
                            "\nID: " + selectedUser.getUserId() +
                            "\nEmail: " + selectedUser.getEmail() +
                            "\nRole: " + selectedUser.getRole() +
                            "\nReview Blocked: " + selectedUser.getReviewBlocked()
            );
        });

        btnMakeAdmin.setOnClickListener(v -> updateRole("ADMIN"));
        btnMakeUser.setOnClickListener(v -> updateRole("USER"));
        btnBlockReview.setOnClickListener(v -> updateReviewBlocked(true));
        btnUnblockReview.setOnClickListener(v -> updateReviewBlocked(false));
        btnDeleteUser.setOnClickListener(v -> deleteUser());

        loadUsers();
    }

    private boolean checkSelected() {
        if (selectedUser == null) {
            Toast.makeText(this, "사용자를 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadUsers() {
        apiService.getUserList(adminId).enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    displayList.clear();

                    userList.addAll(response.body());

                    for (UserResponse user : userList) {
                        displayList.add(
                                "ID: " + user.getUserId() + "\n" +
                                        "이름: " + user.getName() + "\n" +
                                        "이메일: " + user.getEmail() + "\n" +
                                        "권한: " + user.getRole() + "\n" +
                                        "리뷰 정지: " + user.getReviewBlocked()
                        );
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminUserActivity.this, "사용자 목록 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Toast.makeText(AdminUserActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRole(String role) {
        if (!checkSelected()) return;

        apiService.updateUserRole(selectedUser.getUserId(), adminId, role)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminUserActivity.this, "권한 변경 성공", Toast.LENGTH_SHORT).show();
                            selectedUser = null;
                            textSelectedUser.setText("선택된 사용자 없음");
                            loadUsers();
                        } else {
                            Toast.makeText(AdminUserActivity.this, "권한 변경 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(AdminUserActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateReviewBlocked(Boolean blocked) {
        if (!checkSelected()) return;

        apiService.updateReviewBlocked(selectedUser.getUserId(), adminId, blocked)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminUserActivity.this, "리뷰 작성 상태 변경 성공", Toast.LENGTH_SHORT).show();
                            selectedUser = null;
                            textSelectedUser.setText("선택된 사용자 없음");
                            loadUsers();
                        } else {
                            Toast.makeText(AdminUserActivity.this, "리뷰 작성 상태 변경 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(AdminUserActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteUser() {
        if (!checkSelected()) return;

        apiService.deleteUserByAdmin(selectedUser.getUserId(), adminId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminUserActivity.this, "사용자 삭제 성공", Toast.LENGTH_SHORT).show();
                            selectedUser = null;
                            textSelectedUser.setText("선택된 사용자 없음");
                            loadUsers();
                        } else {
                            Toast.makeText(AdminUserActivity.this, "사용자 삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(AdminUserActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}