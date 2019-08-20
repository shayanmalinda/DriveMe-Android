package com.driveme.driveme;

import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;

public class DriverRouteDetails2 {


    public Place startPlace;
    public Place endPlace;
    public String startTime;
    public String endTime;


    public Place getStartPlace() {
        return startPlace;
    }

    public Place getEndPlace() {
        return endPlace;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public DriverRouteDetails2(Place startPlace, Place endPlace, String startTime, String endTime) {
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
