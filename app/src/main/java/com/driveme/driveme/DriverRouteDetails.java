package com.driveme.driveme;

import androidx.annotation.Keep;

import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONObject;

@Keep
public class DriverRouteDetails {

    public static Place startPlace;
    public static Place endPlace;
    public static String startTime;
    public static String endTime;
    public static JSONArray checkpoints = new JSONArray();

    public static JSONArray getCheckpoints() {
        return checkpoints;
    }

    public static void setCheckpoints(JSONArray checkpoints) {
        DriverRouteDetails.checkpoints = checkpoints;
    }

    public static Place getStartPlace() {
        return startPlace;
    }

    public static void setStartPlace(Place startPlace) {
        DriverRouteDetails.startPlace = startPlace;
    }

    public static Place getEndPlace() {
        return endPlace;
    }

    public static void setEndPlace(Place endPlace) {
        DriverRouteDetails.endPlace = endPlace;
    }

    public static String getStartTime() {
        return startTime;
    }

    public static void setStartTime(String startTime) {
        DriverRouteDetails.startTime = startTime;
    }

    public static String getEndTime() {
        return endTime;
    }

    public static void setEndTime(String endTime) {
        DriverRouteDetails.endTime = endTime;
    }
}
