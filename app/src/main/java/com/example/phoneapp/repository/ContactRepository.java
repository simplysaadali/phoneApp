package com.example.phoneapp.repository;

import androidx.lifecycle.LiveData;

import com.example.phoneapp.data.Contact;
import com.example.phoneapp.data.ContactDao;
import com.example.phoneapp.utils.PakistaniDummyDataGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactRepository {

    private final ContactDao contactDao;
    private final ExecutorService executor;

    private final LiveData<List<Contact>> allContacts;
    private final LiveData<List<Contact>> favoriteContacts;

    public ContactRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
        this.executor = Executors.newSingleThreadExecutor();
        this.allContacts = contactDao.getAllContacts();
        this.favoriteContacts = contactDao.getFavoriteContacts();

        // Pre-populate 100 Pakistani contacts if database has fewer than 10 contacts
        executor.execute(() -> {
            if (contactDao.getContactCount() < 10) {
                contactDao.insertAll(PakistaniDummyDataGenerator.generate100PakistaniContacts());
            }
        });
    }

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
        // Called on background thread from ViewModel
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
}
