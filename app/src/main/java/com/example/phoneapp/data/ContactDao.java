package com.example.phoneapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY firstName ASC, lastName ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contacts WHERE isFavorite = 1 ORDER BY firstName ASC, lastName ASC")
    LiveData<List<Contact>> getFavoriteContacts();

    @Query("SELECT * FROM contacts WHERE " +
           "firstName LIKE '%' || :query || '%' OR " +
           "lastName  LIKE '%' || :query || '%' OR " +
           "phone     LIKE '%' || :query || '%' OR " +
           "email     LIKE '%' || :query || '%' " +
           "ORDER BY firstName ASC, lastName ASC")
    LiveData<List<Contact>> searchContacts(String query);

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    Contact getContactByIdSync(long id);

    @Query("SELECT COUNT(*) FROM contacts")
    int getContactCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contact> contacts);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("UPDATE contacts SET isFavorite = :isFavorite WHERE id = :id")
    void updateFavoriteStatus(long id, boolean isFavorite);
}
