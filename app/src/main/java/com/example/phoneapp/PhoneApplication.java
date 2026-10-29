package com.example.phoneapp;

import android.app.Application;

import com.example.phoneapp.data.ContactDatabase;
import com.example.phoneapp.repository.ContactRepository;

public class PhoneApplication extends Application {

    private ContactDatabase database;
    private ContactRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        database = ContactDatabase.getDatabase(this);
        repository = new ContactRepository(database.contactDao());
    }

    public ContactRepository getRepository() {
        return repository;
    }
}
