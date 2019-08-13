package com.driveme.driveme;

public class CurrentUser {
    private static String currentuserID;

    public static String getCurrentuserID() {
        return currentuserID;
    }

    public static void setCurrentuserID(String currentuserID) {
        CurrentUser.currentuserID = currentuserID;
    }
}
