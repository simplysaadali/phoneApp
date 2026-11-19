package com.example.phoneapp.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "call_logs")
public class CallLog {

    public enum CallType {
        INCOMING, OUTGOING, MISSED
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long contactId; // 0 if unknown
    private String phoneNumber;
    private String contactName; // cached name, "" if unknown
    private String callType; // CallType.name()
    private long duration; // seconds
    private long timestamp; // millis

    // Primary constructor for Room
    public CallLog(long id, long contactId, String phoneNumber, String contactName,
                   String callType, long duration, long timestamp) {
        this.id = id;
        this.contactId = contactId;
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.contactName = contactName != null ? contactName : "";
        this.callType = callType != null ? callType : CallType.INCOMING.name();
        this.duration = duration;
        this.timestamp = timestamp;
    }

    // Convenience constructor for creating new call logs
    @Ignore
    public CallLog(long contactId, String phoneNumber, String contactName,
                   String callType, long duration, long timestamp) {
        this.contactId = contactId;
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.contactName = contactName != null ? contactName : "";
        this.callType = callType != null ? callType : CallType.INCOMING.name();
        this.duration = duration;
        this.timestamp = timestamp;
    }

    @Ignore
    public CallLog(String phoneNumber, String contactName, CallType callType,
                   long duration, long timestamp) {
        this(0, phoneNumber, contactName, callType.name(), duration, timestamp);
    }

    // --- Getters ---
    public long getId() { return id; }
    public long getContactId() { return contactId; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getContactName() { return contactName; }
    public String getCallType() { return callType; }
    public long getDuration() { return duration; }
    public long getTimestamp() { return timestamp; }

    // --- Setters ---
    public void setId(long id) { this.id = id; }
    public void setContactId(long contactId) { this.contactId = contactId; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setCallType(String callType) { this.callType = callType; }
    public void setDuration(long duration) { this.duration = duration; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // --- Helpers ---
    public String getDisplayName() {
        if (contactName != null && !contactName.trim().isEmpty()) {
            return contactName.trim();
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            return phoneNumber.trim();
        }
        return "Unknown";
    }

    public String getInitials() {
        String name = getDisplayName();
        if (name == null || name.isEmpty() || Character.isDigit(name.charAt(0)) || name.startsWith("+")) {
            return "#";
        }
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        if (parts.length > 0 && !parts[0].isEmpty()) {
            sb.append(Character.toUpperCase(parts[0].charAt(0)));
        }
        if (parts.length > 1 && !parts[1].isEmpty()) {
            sb.append(Character.toUpperCase(parts[1].charAt(0)));
        }
        return sb.length() > 0 ? sb.toString() : "?";
    }

    public CallType getCallTypeEnum() {
        try {
            return CallType.valueOf(callType);
        } catch (Exception e) {
            return CallType.INCOMING;
        }
    }

    public String getFormattedDuration() {
        if (duration <= 0) return "";
        long minutes = duration / 60;
        long seconds = duration % 60;
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }
        return seconds + "s";
    }

    public String getRelativeTime() {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        if (diff < 0) diff = 0;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + "m ago";
        } else if (hours < 24) {
            return hours + "h ago";
        } else if (days == 1) {
            return "Yesterday";
        } else if (days < 7) {
            return days + "d ago";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}
