package edu.wpi.cs3733.D21.teamF.utils;

public class CurrentUser {

    private static CurrentUser currentUser = new CurrentUser();
    private String currentUserName;

    private CurrentUser() {}

    public static CurrentUser getCurrentUser() { return currentUser; }

    public String getCurrentUserName() { return currentUserName; }

}
