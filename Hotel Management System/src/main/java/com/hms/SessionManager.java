package com.hms;

public class SessionManager<T> {
    private static SessionManager<?> instance;
    private T currentUser;

    private String currentUserType;

    private SessionManager() {  }

    public static <T> SessionManager<T> getInstance() {
        if (instance == null) {
            instance = new SessionManager<>();
        }
        return (SessionManager<T>) instance;
    }

    public String getCurrentUserType() {
        return currentUserType;
    }

    public void setCurrentUserType(String currentUserType) {
        this.currentUserType = currentUserType;
    }

    public void setCurrentUser(T user) {
        this.currentUser = user;
    }

    public T getCurrentUser() {
        return currentUser;
    }

    public void endSession() {
        currentUser = null;
    }

    @Override
    public String toString() {
        return String.format("SessionManager..%n"+
                currentUser.toString());
    }
}
