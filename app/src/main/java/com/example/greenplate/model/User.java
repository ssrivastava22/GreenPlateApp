package com.example.greenplate.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String username;
    private String password;

    private String height;
    private String weight;
    private String gender;

    private static User instance;

    private User(String username, String password) {
        this.height = "0";
        this.weight = "0";
        this.gender = " ";
        this.username = username;
        this.password = password;
    }

    private User(String height, String weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.username = " ";
        this.password = " ";
    }
    private User(String username, String password, String height, String weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getGender() { return gender; }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void addUserToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        // Use the username as the key in the database
        usersRef.child(username).setValue(this);
    }

    private User() {
        // Private constructor to prevent instantiation
    }
    public static User getInstance() {
        if (instance == null) {
            synchronized (UserSingleton.class) {
                if (instance == null) {
                    instance = new User();
                }
            }
        }
        return instance;
    }
}
