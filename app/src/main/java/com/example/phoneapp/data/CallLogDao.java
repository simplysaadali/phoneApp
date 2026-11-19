package com.example.phoneapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CallLogDao {

    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    LiveData<List<CallLog>> getAllCallLogs();

    @Query("SELECT * FROM call_logs WHERE callType = :callType ORDER BY timestamp DESC")
    LiveData<List<CallLog>> getCallLogsByType(String callType);

    @Query("SELECT COUNT(*) FROM call_logs")
    int getCallLogCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCallLog(CallLog callLog);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CallLog> callLogs);

    @Delete
    void deleteCallLog(CallLog callLog);

    @Query("DELETE FROM call_logs")
    void deleteAllCallLogs();
}
