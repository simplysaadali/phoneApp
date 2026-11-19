package com.example.phoneapp.repository;

import androidx.lifecycle.LiveData;

import com.example.phoneapp.data.CallLog;
import com.example.phoneapp.data.CallLogDao;
import com.example.phoneapp.data.Contact;
import com.example.phoneapp.data.ContactDao;
import com.example.phoneapp.utils.PakistaniDummyDataGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactRepository {

    private final ContactDao contactDao;
    private final CallLogDao callLogDao;
    private final ExecutorService executor;

    private final LiveData<List<Contact>> allContacts;
    private final LiveData<List<Contact>> favoriteContacts;
    private final LiveData<List<CallLog>> allCallLogs;

    public ContactRepository(ContactDao contactDao, CallLogDao callLogDao) {
        this.contactDao = contactDao;
        this.callLogDao = callLogDao;
        this.executor = Executors.newSingleThreadExecutor();

        this.allContacts = contactDao.getAllContacts();
        this.favoriteContacts = contactDao.getFavoriteContacts();
        this.allCallLogs = callLogDao.getAllCallLogs();

        // Pre-populate dummy contacts and call logs
        executor.execute(() -> {
            if (contactDao.getContactCount() < 10) {
                contactDao.insertAll(PakistaniDummyDataGenerator.generate100PakistaniContacts());
            }
            if (callLogDao.getCallLogCount() == 0) {
                callLogDao.insertAll(PakistaniDummyDataGenerator.generateDummyCallLogs());
            }
        });
    }

    // --- Contact Methods ---

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public LiveData<List<Contact>> getFavoriteContacts() {
        return favoriteContacts;
    }

    public LiveData<List<Contact>> searchContacts(String query) {
        return contactDao.searchContacts(query);
    }

    public Contact getContactByIdSync(long id) {
        return contactDao.getContactByIdSync(id);
    }

    public void insert(Contact contact) {
        executor.execute(() -> contactDao.insertContact(contact));
    }

    public void update(Contact contact) {
        executor.execute(() -> contactDao.updateContact(contact));
    }

    public void delete(Contact contact) {
        executor.execute(() -> contactDao.deleteContact(contact));
    }

    public void toggleFavorite(long id, boolean isFavorite) {
        executor.execute(() -> contactDao.updateFavoriteStatus(id, isFavorite));
    }

    // --- Call Log Methods ---

    public LiveData<List<CallLog>> getAllCallLogs() {
        return allCallLogs;
    }

    public void insertCallLog(CallLog callLog) {
        executor.execute(() -> callLogDao.insertCallLog(callLog));
    }

    public void deleteCallLog(CallLog callLog) {
        executor.execute(() -> callLogDao.deleteCallLog(callLog));
    }

    public void clearCallLogs() {
        executor.execute(() -> callLogDao.deleteAllCallLogs());
    }
}
