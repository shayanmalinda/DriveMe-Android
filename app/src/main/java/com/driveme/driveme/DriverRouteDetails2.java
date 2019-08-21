package com.driveme.driveme;

import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;

public class DriverRouteDetails2 {


    public String startPlaceId;
    public Double startPlaceLat;
    public Double startPlaceLng;
    public String startPlaceName;
    public String endPlaceId;
    public Double endPlaceLat;
    public Double endPlaceLng;
    public String endPlaceName;
    public String startTime;
    public String endTime;

    public DriverRouteDetails2(String startPlaceId, Double startPlaceLat, Double startPlaceLng, String startPlaceName, String endPlaceId, Double endPlaceLat, Double endPlaceLng, String endPlaceName, String startTime, String endTime) {
        this.startPlaceId = startPlaceId;
        this.startPlaceLat = startPlaceLat;
        this.startPlaceLng = startPlaceLng;
        this.startPlaceName = startPlaceName;
        this.endPlaceId = endPlaceId;
        this.endPlaceLat = endPlaceLat;
        this.endPlaceLng = endPlaceLng;
        this.endPlaceName = endPlaceName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartPlaceId() {
        return startPlaceId;
    }

    public Double getStartPlaceLat() {
        return startPlaceLat;
    }

    public Double getStartPlaceLng() {
        return startPlaceLng;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public String getEndPlaceId() {
        return endPlaceId;
    }

    public Double getEndPlaceLat() {
        return endPlaceLat;
    }

    public Double getEndPlaceLng() {
        return endPlaceLng;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
