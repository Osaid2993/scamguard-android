package com.osaid.scamguard;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanRecordDao {

    // Insert a new scan record after each analysis
    @Insert
    void insert(ScanRecord record);

    // Get all records, newest first
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    List<ScanRecord> getAllRecords();

    // Delete all history if user wants to clear it
    @Query("DELETE FROM scan_history")
    void clearAll();
}
