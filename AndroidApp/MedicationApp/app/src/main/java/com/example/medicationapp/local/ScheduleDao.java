package com.example.medicationapp.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Insert
    long insert(ScheduleEntity schedule);

    @Update
    void update(ScheduleEntity schedule);

    @Query("SELECT * FROM schedules ORDER BY hour, minute")
    List<ScheduleEntity> getAllSchedules();

    @Query("SELECT * FROM schedules WHERE scheduleId = :scheduleId LIMIT 1")
    ScheduleEntity getScheduleById(int scheduleId);

    @Query("DELETE FROM schedules WHERE scheduleId = :scheduleId")
    void deleteById(int scheduleId);
}