package com.example.medicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.api.ApiClient;
import com.example.medicationapp.api.ApiService;
import com.example.medicationapp.dto.SearchItemResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText editKeyword;
    private Button btnSearch;
    private ListView listSearchResult;

    private ApiService apiService;

    private List<SearchItemResponse> itemList = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userId = getIntent().getLongExtra("userId", -1L);
        role = getIntent().getStringExtra("role");

        editKeyword = findViewById(R.id.editKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        listSearchResult = findViewById(R.id.listSearchResult);

        apiService = ApiClient.getClient().create(ApiService.class);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listSearchResult.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> search());

        listSearchResult.setOnItemClickListener((parent, view, position, id) -> {
            SearchItemResponse item = itemList.get(position);

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("type", item.getType());
            intent.putExtra("id", item.getId());
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });
    }

    private void search() {
        String keyword = editKeyword.getText().toString().trim();

        if (keyword.isEmpty()) {
            Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.searchItems(keyword).enqueue(new Callback<List<SearchItemResponse>>() {
            @Override
            public void onResponse(Call<List<SearchItemResponse>> call, Response<List<SearchItemResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemList.clear();
                    displayList.clear();

                    itemList.addAll(response.body());

                    for (SearchItemResponse item : itemList) {
                        String typeText = item.getType().equals("MEDICATION") ? "약" : "영양제";
                        displayList.add(
                                "[" + typeText + "] " + item.getName() + "\n" + item.getEffect()
                        );
                    }

                    adapter.notifyDataSetChanged();

                    if (itemList.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "검색 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchItemResponse>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
