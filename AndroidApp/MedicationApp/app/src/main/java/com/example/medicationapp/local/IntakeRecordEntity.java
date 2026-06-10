package com.example.medicationapp.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "intake_records")
public class IntakeRecordEntity {

    @PrimaryKey(autoGenerate = true)
    public int recordId;

    public String itemName;

    public boolean taken;

    public long intakeTimeMillis;

    public int scheduleId;
}