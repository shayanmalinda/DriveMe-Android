package com.driveme.driveme;

import com.google.android.libraries.places.api.model.Place;

public class SelectedPlace {
    public static Place getPlace() {
        return place;
    }

    public static void setPlace(Place place) {
        SelectedPlace.place = place;
    }

    private static Place place;
}
