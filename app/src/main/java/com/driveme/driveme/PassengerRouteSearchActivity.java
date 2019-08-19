package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PassengerRouteSearchActivity extends AppCompatActivity {

    String TAG = "placeautocomplete";
    private Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route_search);
        final String[] startId = {null};
        final String[] endId = new String[1];
        btnSearch = findViewById(R.id.btnSearch);


        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyCEwsWLe6soyNWGG0JTqJVKk4KnGFx_Ax8");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment1 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocompletefragment1);

        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocompletefragment2);

        // Specify the types of place data to return.
        autocompleteFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment1.setHint("Start Location");


        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment2.setHint("End Location");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
//                Toast.makeText(PassengerRouteSearchActivity.this, ""+place.getName()    , Toast.LENGTH_SHORT).show();
//                txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                startId[0] = place.getId();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

        });


        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
//                Toast.makeText(PassengerRouteSearchActivity.this, ""+place.getName()    , Toast.LENGTH_SHORT).show();
//                txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                endId[0] = place.getId();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PassengerRouteSearchActivity.this, ""+startId[0]+"\n"+endId[0], Toast.LENGTH_SHORT).show();
            }
        });


    }
}
