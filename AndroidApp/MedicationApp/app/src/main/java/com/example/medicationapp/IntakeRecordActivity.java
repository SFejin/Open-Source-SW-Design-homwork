package com.example.medicationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicationapp.local.AppDatabase;
import com.example.medicationapp.local.IntakeRecordEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IntakeRecordActivity extends AppCompatActivity {

    private EditText editItemName;
    private RadioButton radioTaken, radioNotTaken;
    private Button btnSaveRecord;
    private ListView listRecords;

    private AppDatabase db;

    private List<IntakeRecordEntity> recordList = new ArrayList<>();
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_record);

        editItemName = findViewById(R.id.editItemName);
        radioTaken = findViewById(R.id.radioTaken);
        radioNotTaken = findViewById(R.id.radioNotTaken);
        btnSaveRecord = findViewById(R.id.btnSaveRecord);
        listRecords = findViewById(R.id.listRecords);

        db = AppDatabase.getInstance(this);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        listRecords.setAdapter(adapter);

        btnSaveRecord.setOnClickListener(v -> saveRecord());

        loadRecords();
    }

    private void saveRecord() {
        String itemName = editItemName.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(this, "약/영양제 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        IntakeRecordEntity record = new IntakeRecordEntity();
        record.itemName = itemName;
        record.taken = radioTaken.isChecked();
        record.intakeTimeMillis = System.currentTimeMillis();
        record.scheduleId = -1;

        db.intakeRecordDao().insert(record);

        Toast.makeText(this, "복용 기록이 저장되었습니다.", Toast.LENGTH_SHORT).show();

        editItemName.setText("");
        radioTaken.setChecked(true);

        loadRecords();
    }

    private void loadRecords() {
        recordList.clear();
        displayList.clear();

        recordList.addAll(db.intakeRecordDao().getAllRecords());

        for (IntakeRecordEntity record : recordList) {
            String status = record.taken ? "복용함" : "미복용";
            String time = dateFormat.format(new Date(record.intakeTimeMillis));

            displayList.add(
                    "이름: " + record.itemName + "\n" +
                            "상태: " + status + "\n" +
                            "시간: " + time
            );
        }

        adapter.notifyDataSetChanged();

        if (recordList.isEmpty()) {
            Toast.makeText(this, "저장된 복용 기록이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}