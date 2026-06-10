package com.example.medicationapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.medicationapp.R;
import com.example.medicationapp.local.ScheduleEntity;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {

    public interface OnDeleteClickListener {
        void onDelete(ScheduleEntity schedule);
    }

    private final Context context;
    private final List<ScheduleEntity> scheduleList;
    private final OnDeleteClickListener listener;

    public ScheduleAdapter(Context context, List<ScheduleEntity> scheduleList, OnDeleteClickListener listener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scheduleList.get(position).scheduleId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_schedule, parent, false);
        }

        ScheduleEntity schedule = scheduleList.get(position);

        TextView textScheduleInfo = convertView.findViewById(R.id.textScheduleInfo);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        textScheduleInfo.setText(
                "ID: " + schedule.scheduleId + "\n" +
                        "이름: " + schedule.itemName + "\n" +
                        "시간: " + String.format("%02d:%02d", schedule.hour, schedule.minute) + "\n" +
                        "주기: " + schedule.cycle + "\n" +
                        "알림 활성화: " + schedule.enabled
        );

        btnDelete.setOnClickListener(v -> listener.onDelete(schedule));

        return convertView;
    }
}
