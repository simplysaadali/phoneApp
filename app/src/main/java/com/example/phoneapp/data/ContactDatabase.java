package com.example.phoneapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    private static volatile ContactDatabase INSTANCE;

    public abstract ContactDao contactDao();

    public static ContactDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ContactDatabase.class,
                            "phone_contact_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
