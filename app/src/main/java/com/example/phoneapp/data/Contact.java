package com.example.phoneapp.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String notes;
    private boolean isFavorite;
    private long createdAt;

    // Main constructor used by Room
    public Contact(long id, String firstName, String lastName, String phone,
                   String email, String address, String notes,
                   boolean isFavorite, long createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.notes = notes;
        this.isFavorite = isFavorite;
        this.createdAt = createdAt;
    }

    // Convenience constructor for creating new contacts
    @Ignore
    public Contact(String firstName, String lastName, String phone,
                   String email, String address, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email != null ? email : "";
        this.address = address != null ? address : "";
        this.notes = notes != null ? notes : "";
        this.isFavorite = false;
        this.createdAt = System.currentTimeMillis();
    }

    // --- Getters ---
    public long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public boolean isFavorite() { return isFavorite; }
    public long getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setId(long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    // --- Helper methods ---
    public String getFullName() {
        String name = (firstName + " " + lastName).trim();
        return name.isEmpty() ? "Unknown" : name;
    }

    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            initials.append(Character.toUpperCase(firstName.charAt(0)));
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials.append(Character.toUpperCase(lastName.charAt(0)));
        }
        return initials.length() > 0 ? initials.toString() : "?";
    }
}
