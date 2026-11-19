package com.example.phoneapp.utils;

import com.example.phoneapp.data.CallLog;
import com.example.phoneapp.data.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PakistaniDummyDataGenerator {

    private static final String[] MALE_FIRST_NAMES = {
        "Muhammad", "Ali", "Ahmed", "Usman", "Bilal", "Hamza", "Omar", "Hassan", "Hussain", "Saad",
        "Zain", "Abdullah", "Shahzaib", "Arslan", "Haris", "Daniyal", "Faisal", "Farhan", "Taha", "Rayyan",
        "Kashif", "Nabeel", "Imran", "Waqas", "Zeeshan", "Tariq", "Shoaib", "Asad", "Babar", "Rizwan",
        "Fahad", "Yasir", "Shehryar", "Sameer", "Ahsan", "Talha", "Zubair", "Adeel", "Nouman", "Umair",
        "Rashid", "Salman", "Zahid", "Danish", "Kamran", "Khurram", "Naveed", "Atif", "Sohaib", "Owais"
    };

    private static final String[] FEMALE_FIRST_NAMES = {
        "Fatima", "Ayesha", "Zainab", "Maryam", "Sana", "Iqra", "Hira", "Sadia", "Anum", "Mahnoor",
        "Laiba", "Noor", "Rabia", "Kiran", "Sidra", "Nimra", "Bushra", "Farah", "Mehwish", "Rimsha",
        "Bisma", "Tayyaba", "Zunaira", "Alishba", "Maria", "Sobia", "Sundas", "Javaria", "Saman", "Saba",
        "Amna", "Kinza", "Mubeen", "Komal", "Nadia", "Sumaira", "Syeda", "Fariha", "Saima", "Ghazala",
        "Tehmina", "Uzma", "Nazia", "Shaista", "Romaisa", "Bakhtawar", "Hania", "Urwa", "Mawra", "Yashma"
    };

    private static final String[] LAST_NAMES = {
        "Khan", "Ahmed", "Ali", "Malik", "Chaudhry", "Sheikh", "Hussain", "Shah", "Raja", "Qureshi",
        "Siddiqui", "Abbasi", "Bhatti", "Mughal", "Rajput", "Farooqui", "Jutt", "Syed", "Awan", "Mirza",
        "Rehmani", "Tareen", "Lodhi", "Gill", "Gujjar", "Bajwa", "Cheema", "Ansari", "Dar", "Rathore",
        "Hashmi", "Khattak", "Afridi", "Niazi", "Durrani", "Janjua", "Rana", "Gillani", "Baig", "Mazari",
        "Wattoo", "Khosa", "Leghari", "Tarar", "Soomro", "Junejo", "Memon", "Jatoi", "Khuhro", "Talpur"
    };

    private static final String[] MOBILE_PREFIXES = {
        "300", "301", "302", "303", "304", "305", "306", "307", "308", "309",
        "310", "311", "312", "313", "314", "315", "316", "317", "318",
        "320", "321", "322", "323", "324",
        "331", "332", "333", "334", "335", "336",
        "340", "341", "342", "343", "344", "345", "346", "347"
    };

    private static final String[] LOCATIONS = {
        "Gulberg III, Lahore", "DHA Phase 5, Karachi", "Sector F-8/2, Islamabad", "Saddar, Rawalpindi",
        "University Town, Peshawar", "People's Colony, Faisalabad", "Cantt, Multan", "Model Town, Quetta",
        "Paris Road, Sialkot", "Satellite Town, Gujranwala", "Auto Bhan Road, Hyderabad", "Model Town A, Bahawalpur",
        "Jinnahabad, Abbottabad", "Civil Lines, Sargodha", "Military Road, Sukkur"
    };

    private static final String[] DOMAINS = {
        "gmail.com", "yahoo.com", "outlook.com", "hotmail.com"
    };

    private static final String[] OCCUPATIONS = {
        "Software Engineer", "Doctor", "Teacher", "Business Partner", "College Friend",
        "University Classmate", "Family Friend", "Accountant", "Architect", "Graphic Designer",
        "Project Manager", "Consultant", "Neighbor", "Lawyer", "Marketing Specialist"
    };

    public static List<Contact> generate100PakistaniContacts() {
        List<Contact> contacts = new ArrayList<>();
        Random random = new Random();

        // 50 Males
        for (int i = 0; i < 50; i++) {
            String firstName = MALE_FIRST_NAMES[i % MALE_FIRST_NAMES.length];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            contacts.add(createContact(firstName, lastName, random));
        }

        // 50 Females
        for (int i = 0; i < 50; i++) {
            String firstName = FEMALE_FIRST_NAMES[i % FEMALE_FIRST_NAMES.length];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            contacts.add(createContact(firstName, lastName, random));
        }

        return contacts;
    }

    private static Contact createContact(String firstName, String lastName, Random random) {
        String prefix = MOBILE_PREFIXES[random.nextInt(MOBILE_PREFIXES.length)];
        int randomNumberPart = 1000000 + random.nextInt(9000000); // 7 digit number
        String phone = "+92 " + prefix + " " + randomNumberPart;

        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() +
                (10 + random.nextInt(90)) + "@" + domain;

        String address = (random.nextInt(100) + 1) + ", " + LOCATIONS[random.nextInt(LOCATIONS.length)];
        String notes = OCCUPATIONS[random.nextInt(OCCUPATIONS.length)];

        Contact contact = new Contact(firstName, lastName, phone, email, address, notes);
        if (random.nextFloat() < 0.15f) { // ~15% marked as favorite
            contact.setFavorite(true);
        }
        return contact;
    }

    public static List<CallLog> generateDummyCallLogs() {
        List<CallLog> logs = new ArrayList<>();
        long now = System.currentTimeMillis();
        long minute = 60 * 1000L;
        long hour = 60 * minute;
        long day = 24 * hour;

        // 15 realistic recent calls
        logs.add(new CallLog(0, "+92 300 1234567", "Muhammad Khan", CallLog.CallType.INCOMING.name(), 184, now - 12 * minute));
        logs.add(new CallLog(0, "+92 321 9876543", "Fatima Ali", CallLog.CallType.OUTGOING.name(), 65, now - 45 * minute));
        logs.add(new CallLog(0, "+92 333 4567890", "Usman Malik", CallLog.CallType.MISSED.name(), 0, now - 2 * hour));
        logs.add(new CallLog(0, "+92 345 6789012", "Ayesha Sheikh", CallLog.CallType.INCOMING.name(), 340, now - 5 * hour));
        logs.add(new CallLog(0, "+92 312 8765432", "Saad Chaudhry", CallLog.CallType.OUTGOING.name(), 120, now - 9 * hour));
        logs.add(new CallLog(0, "+92 301 2345678", "Zainab Shah", CallLog.CallType.MISSED.name(), 0, now - 18 * hour));
        logs.add(new CallLog(0, "+92 322 3456789", "Hamza Qureshi", CallLog.CallType.INCOMING.name(), 210, now - 1 * day - 2 * hour));
        logs.add(new CallLog(0, "+92 308 5551234", "+92 308 5551234", CallLog.CallType.MISSED.name(), 0, now - 1 * day - 5 * hour));
        logs.add(new CallLog(0, "+92 334 5678901", "Bilal Ahmed", CallLog.CallType.OUTGOING.name(), 85, now - 2 * day));
        logs.add(new CallLog(0, "+92 315 9012345", "Maryam Siddiqui", CallLog.CallType.INCOMING.name(), 512, now - 2 * day - 4 * hour));
        logs.add(new CallLog(0, "+92 302 7890123", "Omar Bhatti", CallLog.CallType.OUTGOING.name(), 42, now - 3 * day));
        logs.add(new CallLog(0, "+92 346 1122334", "+92 346 1122334", CallLog.CallType.INCOMING.name(), 95, now - 4 * day));
        logs.add(new CallLog(0, "+92 323 9988776", "Hassan Rajput", CallLog.CallType.MISSED.name(), 0, now - 5 * day));
        logs.add(new CallLog(0, "+92 311 4455667", "Sana Abbasi", CallLog.CallType.OUTGOING.name(), 175, now - 6 * day));
        logs.add(new CallLog(0, "+92 304 6677889", "Ali Syed", CallLog.CallType.INCOMING.name(), 260, now - 7 * day));

        return logs;
    }

    private PakistaniDummyDataGenerator() { /* Utility */ }
}
