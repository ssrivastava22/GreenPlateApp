package com.example.greenplate.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class User {

    private String id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.id = generateRandomId();
        this.username = username;
        this.password = password;
    }
    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void addUserToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(id).setValue(this);
    }

    private String generateRandomId() {
        // Generate a random UUID (Universally Unique Identifier)
        return UUID.randomUUID().toString();
    }
}
