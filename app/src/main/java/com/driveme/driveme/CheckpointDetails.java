package com.driveme.driveme;

public class CheckpointDetails {

    public String placeId;
    public String placeName;
    public String placeLat;
    public String placeLng;
    public String time;

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceLat() {
        return placeLat;
    }

    public String getPlaceLng() {
        return placeLng;
    }

    public String getTime() {
        return time;
    }

    public CheckpointDetails(String placeId, String placeName, String placeLat, String placeLng, String time) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.time = time;
    }
}
