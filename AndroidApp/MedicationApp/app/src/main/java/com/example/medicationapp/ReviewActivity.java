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
import com.example.medicationapp.dto.ReviewCreateRequest;
import com.example.medicationapp.dto.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.medicationapp.dto.ReviewUpdateRequest;

public class ReviewActivity extends AppCompatActivity {

    private TextView textReviewTitle;
    private ListView listReviews;
    private EditText editRating, editContent;
    private Button btnWriteReview, btnUpdateReview, btnDeleteReview;
    private Long selectedReviewId = null;

    private ApiService apiService;

    private String type;
    private Long id;
    private long userId;
    private String role;

    private List<ReviewResponse> reviewList = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        textReviewTitle = findViewById(R.id.textReviewTitle);
        listReviews = findViewById(R.id.listReviews);
        editRating = findViewById(R.id.editRating);
        editContent = findViewById(R.id.editContent);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        btnUpdateReview = findViewById(R.id.btnUpdateReview);
        btnDeleteReview = findViewById(R.id.btnDeleteReview);
        apiService = ApiClient.getClient().create(ApiService.class);

        type = getIntent().getStringExtra("type");
        id = getIntent().getLongExtra("id", -1L);
        userId = getIntent().getLongExtra("userId", -1L);
        role = getIntent().getStringExtra("role");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        listReviews.setAdapter(adapter);

        if (type == null || id == -1L) {
            textReviewTitle.setText("리뷰 관리");
            Toast.makeText(this, "상세 화면에서 리뷰를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            btnWriteReview.setEnabled(false);
            return;
        }

        textReviewTitle.setText("리뷰 목록");
        loadReviews();

        btnWriteReview.setOnClickListener(v -> createReview());

        listReviews.setOnItemLongClickListener((parent, view, position, rowId) -> {
            ReviewResponse review = reviewList.get(position);

            if (review.getUserId() != null && review.getUserId() == userId) {
                deleteReview(review.getReviewId());
            } else {
                Toast.makeText(this, "본인이 작성한 리뷰만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }

            return true;
        });
        listReviews.setOnItemClickListener((parent, view, position, rowId) -> {
            ReviewResponse review = reviewList.get(position);

            if (review.getUserId() != null && review.getUserId().equals(userId)) {
                selectedReviewId = review.getReviewId();

                editRating.setText(String.valueOf(review.getRating()));
                editContent.setText(review.getContent());

                Toast.makeText(this, "수정/삭제할 리뷰가 선택되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                selectedReviewId = null;
                Toast.makeText(this, "본인이 작성한 리뷰만 수정/삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        btnWriteReview.setOnClickListener(v -> createReview());

        btnUpdateReview.setOnClickListener(v -> {
            if (selectedReviewId == null) {
                Toast.makeText(this, "수정할 리뷰를 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateReview(selectedReviewId);
        });

        btnDeleteReview.setOnClickListener(v -> {
            if (selectedReviewId == null) {
                Toast.makeText(this, "삭제할 리뷰를 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteReview(selectedReviewId);
        });
    }

    private void loadReviews() {
        Call<List<ReviewResponse>> call;

        if ("MEDICATION".equals(type)) {
            call = apiService.getMedicationReviews(id);
        } else {
            call = apiService.getSupplementReviews(id);
        }

        call.enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    displayList.clear();

                    reviewList.addAll(response.body());

                    for (ReviewResponse review : reviewList) {
                        displayList.add(
                                "작성자: " + review.getUserName() + "\n" +
                                        "별점: " + review.getRating() + "\n" +
                                        "내용: " + review.getContent()
                        );
                    }

                    adapter.notifyDataSetChanged();

                    if (reviewList.isEmpty()) {
                        Toast.makeText(ReviewActivity.this, "리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createReview() {
        if (userId == -1L || "GUEST".equals(role)) {
            Toast.makeText(this, "리뷰 작성은 로그인 후 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String ratingText = editRating.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (ratingText.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "별점과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int rating = Integer.parseInt(ratingText);

        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "별점은 1~5 사이로 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Long medicationId = null;
        Long supplementId = null;

        if ("MEDICATION".equals(type)) {
            medicationId = id;
        } else {
            supplementId = id;
        }

        ReviewCreateRequest request = new ReviewCreateRequest(
                userId,
                medicationId,
                supplementId,
                rating,
                content
        );

        apiService.createReview(request).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "리뷰 작성 성공", Toast.LENGTH_SHORT).show();
                    editRating.setText("");
                    editContent.setText("");
                    loadReviews();
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 작성 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteReview(Long reviewId) {
        apiService.deleteReview(reviewId, userId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "리뷰 삭제 성공", Toast.LENGTH_SHORT).show();

                    selectedReviewId = null;
                    editRating.setText("");
                    editContent.setText("");

                    loadReviews();
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateReview(Long reviewId) {
        String ratingText = editRating.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (ratingText.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "별점과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int rating = Integer.parseInt(ratingText);

        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "별점은 1~5 사이로 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ReviewUpdateRequest request = new ReviewUpdateRequest(
                userId,
                rating,
                content
        );

        apiService.updateReview(reviewId, request).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "리뷰 수정 성공", Toast.LENGTH_SHORT).show();

                    selectedReviewId = null;
                    editRating.setText("");
                    editContent.setText("");

                    loadReviews();
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 수정 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}