package com.example.greenplate.model;

public class UserSingleton {
    public static UserSingleton instance;
    private User user;

    private UserSingleton() {
        // Private constructor to prevent instantiation
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            synchronized (UserSingleton.class) {
                if (instance == null) {
                    instance = new UserSingleton();
                }
            }
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
