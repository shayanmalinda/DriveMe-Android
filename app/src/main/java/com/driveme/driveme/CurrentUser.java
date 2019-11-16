package com.driveme.driveme;

public class CurrentUser {
    private static String currentuserID;

    private static String passengerId;
    private static String parentId;
    private static String driverId;
    private static String ownerID;

    public static String getPassengerId() {
        return passengerId;
    }

    public static void setPassengerId(String passengerId) {
        CurrentUser.passengerId = passengerId;
    }

    public static String getParentId() {
        return parentId;
    }

    public static void setParentId(String parentId) {
        CurrentUser.parentId = parentId;
    }

    public static String getDriverId() {
        return driverId;
    }

    public static void setDriverId(String driverId) {
        CurrentUser.driverId = driverId;
    }

    public static String getOwnerID() {
        return ownerID;
    }

    public static void setOwnerID(String ownerID) {
        CurrentUser.ownerID = ownerID;
    }

    public static String getuUserCredentialId() {
        return currentuserID;
    }

    public static void setUserCredentialId(String currentuserID) {
        CurrentUser.currentuserID = currentuserID;
    }
}
