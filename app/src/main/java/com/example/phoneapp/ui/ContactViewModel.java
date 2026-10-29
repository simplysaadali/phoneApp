package com.example.phoneapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.phoneapp.PhoneApplication;
import com.example.phoneapp.data.Contact;
import com.example.phoneapp.repository.ContactRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactViewModel extends AndroidViewModel {

    private final ContactRepository repository;
    private final ExecutorService executor;

    // Search query drives which contacts are shown
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    // Favorites filter flag
    private final MutableLiveData<Boolean> showFavorites = new MutableLiveData<>(false);

    private final LiveData<List<Contact>> contacts;
    private final LiveData<List<Contact>> favoriteContacts;

    // Holds single contact loaded for detail / edit
    private final MutableLiveData<Contact> selectedContact = new MutableLiveData<>();

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = ((PhoneApplication) application).getRepository();
        executor = Executors.newSingleThreadExecutor();

        favoriteContacts = repository.getFavoriteContacts();

        // Switch between all-contacts, search results, and favorites based on flags
        contacts = Transformations.switchMap(showFavorites, isFav -> {
            if (Boolean.TRUE.equals(isFav)) {
                return repository.getFavoriteContacts();
            }
            return Transformations.switchMap(searchQuery, query -> {
                if (query == null || query.trim().isEmpty()) {
                    return repository.getAllContacts();
                } else {
                    return repository.searchContacts(query.trim());
                }
            });
        });
    }

    public LiveData<List<Contact>> getContacts() { return contacts; }
    public LiveData<List<Contact>> getFavoriteContacts() { return favoriteContacts; }
    public LiveData<Contact> getSelectedContact() { return selectedContact; }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setShowFavorites(boolean show) {
        showFavorites.setValue(show);
    }

    public void loadContactById(long id) {
        executor.execute(() -> {
            Contact contact = repository.getContactByIdSync(id);
            selectedContact.postValue(contact);
        });
    }

    public void insert(Contact contact) { repository.insert(contact); }

    public void update(Contact contact) { repository.update(contact); }

    public void delete(Contact contact) { repository.delete(contact); }

    public void toggleFavorite(long id, boolean isFavorite) {
        repository.toggleFavorite(id, isFavorite);
    }
}
