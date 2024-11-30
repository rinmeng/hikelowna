package com.example.hikelowna.core;

public class UserManager {
    private static UserManager instance;
    private static boolean isLoggedInOnce;
    private User currentUser;


    private UserManager() {
        this.currentUser = new User();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public static boolean isLoggedInOnce() {
        return isLoggedInOnce;
    }

    public static void isLoggedInOnce(boolean isLoggedInOnce) {
        UserManager.isLoggedInOnce = isLoggedInOnce;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }

    public void updateUser(User user) {
        currentUser = user;
    }
}
