package com.driveme.driveme;

public class CheckpointDetails {

    public String placeId;
    public String placeName;
    public String placeLat;
    public String placeLng;
    public String time;
    public boolean isActive;
    public int order;
    public boolean isStart;
    public boolean isEnd;

public boolean isStart() {
        return isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public int getOrder() {
        return order;
    }

    public boolean isActive() {
        return isActive;
    }

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

    public CheckpointDetails(String placeId, String placeName, String placeLat, String placeLng, String time, boolean isActive,int order,boolean isStart,boolean isEnd) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.time = time;
        this.isActive = isActive;
        this.order = order;
    }
}
