package com.osaid.scamguard;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScanRecord.class}, version = 1)
public abstract class ScamGuardDatabase extends RoomDatabase {

    // Singleton instance so the database is only created once
    private static ScamGuardDatabase instance;

    public abstract ScanRecordDao scanRecordDao();

    // Thread-safe singleton access
    public static synchronized ScamGuardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ScamGuardDatabase.class,
                    "scamguard_db"
            ).build();
        }
        return instance;
    }
}
