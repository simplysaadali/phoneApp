package com.example.phoneapp.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Contact.class, CallLog.class}, version = 2, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    private static volatile ContactDatabase INSTANCE;

    public abstract ContactDao contactDao();
    public abstract CallLogDao callLogDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `call_logs` ("
                    + "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "`contactId` INTEGER NOT NULL, "
                    + "`phoneNumber` TEXT, "
                    + "`contactName` TEXT, "
                    + "`callType` TEXT, "
                    + "`duration` INTEGER NOT NULL, "
                    + "`timestamp` INTEGER NOT NULL)");
        }
    };

    public static ContactDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ContactDatabase.class,
                            "phone_contact_database"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
