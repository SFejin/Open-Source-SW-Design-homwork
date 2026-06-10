package com.example.medicationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.api.ApiClient;
import com.example.medicationapp.api.ApiService;
import com.example.medicationapp.dto.ReviewHideRequest;
import com.example.medicationapp.dto.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReviewActivity extends AppCompatActivity {

    private long adminId;
    private ApiService apiService;

    private ListView listAdminReviews;
    private TextView textSelectedReview;
    private EditText editHideReason;
    private Button btnHideReview, btnUnhideReview, btnDeleteReview;

    private List<ReviewResponse> reviewList = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private ReviewResponse selectedReview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review);

        adminId = getIntent().getLongExtra("adminId", -1L);
        apiService = ApiClient.getClient().create(ApiService.class);

        listAdminReviews = findViewById(R.id.listAdminReviews);
        textSelectedReview = findViewById(R.id.textSelectedReview);
        editHideReason = findViewById(R.id.editHideReason);
        btnHideReview = findViewById(R.id.btnHideReview);
        btnUnhideReview = findViewById(R.id.btnUnhideReview);
        btnDeleteReview = findViewById(R.id.btnDeleteReview);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listAdminReviews.setAdapter(adapter);

        listAdminReviews.setOnItemClickListener((parent, view, position, id) -> {
            selectedReview = reviewList.get(position);

            textSelectedReview.setText(
                    "선택된 리뷰 ID: " + selectedReview.getReviewId() +
                            "\n작성자: " + selectedReview.getUserName() +
                            "\n숨김 여부: " + selectedReview.getHidden() +
                            "\n숨김 사유: " + selectedReview.getHiddenReason()
            );
        });

        btnHideReview.setOnClickListener(v -> hideReview());
        btnUnhideReview.setOnClickListener(v -> unhideReview());
        btnDeleteReview.setOnClickListener(v -> deleteReview());

        loadReviews();
    }

    private boolean checkSelected() {
        if (selectedReview == null) {
            Toast.makeText(this, "리뷰를 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadReviews() {
        apiService.getAllReviews(adminId).enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    displayList.clear();

                    reviewList.addAll(response.body());

                    for (ReviewResponse review : reviewList) {
                        displayList.add(
                                "리뷰 ID: " + review.getReviewId() + "\n" +
                                        "작성자: " + review.getUserName() + "\n" +
                                        "항목: " + review.getItemType() + " / " + review.getItemId() + "\n" +
                                        "별점: " + review.getRating() + "\n" +
                                        "내용: " + review.getContent() + "\n" +
                                        "숨김: " + review.getHidden() + "\n" +
                                        "숨김 사유: " + review.getHiddenReason() + "\n" +
                                        "숨김 관리자: " + review.getHiddenByAdminName() + "\n" +
                                        "숨김 시간: " + review.getHiddenAt()
                        );
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminReviewActivity.this, "리뷰 목록 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                Toast.makeText(AdminReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideReview() {
        if (!checkSelected()) return;

        String reason = editHideReason.getText().toString().trim();

        if (reason.isEmpty()) {
            Toast.makeText(this, "숨김 사유를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ReviewHideRequest request = new ReviewHideRequest(adminId, reason);

        apiService.hideReviewByAdmin(selectedReview.getReviewId(), request)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 숨김 성공", Toast.LENGTH_SHORT).show();
                            selectedReview = null;
                            editHideReason.setText("");
                            textSelectedReview.setText("선택된 리뷰 없음");
                            loadReviews();
                        } else {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 숨김 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        Toast.makeText(AdminReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unhideReview() {
        if (!checkSelected()) return;

        apiService.unhideReviewByAdmin(selectedReview.getReviewId(), adminId)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 숨김 해제 성공", Toast.LENGTH_SHORT).show();
                            selectedReview = null;
                            editHideReason.setText("");
                            textSelectedReview.setText("선택된 리뷰 없음");
                            loadReviews();
                        } else {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 숨김 해제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        Toast.makeText(AdminReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteReview() {
        if (!checkSelected()) return;

        apiService.deleteReviewByAdmin(selectedReview.getReviewId(), adminId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 삭제 성공", Toast.LENGTH_SHORT).show();
                            selectedReview = null;
                            editHideReason.setText("");
                            textSelectedReview.setText("선택된 리뷰 없음");
                            loadReviews();
                        } else {
                            Toast.makeText(AdminReviewActivity.this, "리뷰 삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(AdminReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}