package com.example.greenplate.model;

public class UserSingleton {
    private static UserSingleton instance;
    private User user;

    private UserSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
