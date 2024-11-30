package com.example.hikelowna.core;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
