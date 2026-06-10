package com.example.medicationapp.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IntakeRecordDao {

    @Insert
    long insert(IntakeRecordEntity record);

    @Query("SELECT * FROM intake_records ORDER BY intakeTimeMillis DESC")
    List<IntakeRecordEntity> getAllRecords();

    @Query("SELECT * FROM intake_records WHERE scheduleId = :scheduleId ORDER BY intakeTimeMillis DESC")
    List<IntakeRecordEntity> getRecordsByScheduleId(int scheduleId);
}