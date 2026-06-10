package com.example.medicationapp.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules")
public class ScheduleEntity {

    @PrimaryKey(autoGenerate = true)
    public int scheduleId;

    public String itemName;

    public int hour;

    public int minute;

    public String cycle;

    public boolean enabled;
}