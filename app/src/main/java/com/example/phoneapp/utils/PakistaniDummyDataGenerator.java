package com.example.phoneapp.utils;

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

    private PakistaniDummyDataGenerator() { /* Utility */ }
}
