package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MapFragmentActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng location = new LatLng(-8.579892, 116.095239);
    private String locationtitle = null;
    String locationId;
    String TAG = "placeautocomplete";
    private MapFragment mapFragment;
    Place myPlace;
    private Button btnselect;
    private Button btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment2);

        btncancel = findViewById(R.id.btncancel);


        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        SelectedTime st = new SelectedTime();
        st.setSelectedTime(null);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        btnselect = findViewById(R.id.btnselect);
        setupAutoCompleteFragment();



        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedPlace s = new SelectedPlace();
                s.setPlace(myPlace);
                Intent intent = new Intent(MapFragmentActivity2.this,ChooseTimeActivity2.class);
                startActivityForResult(intent,11);

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(MapFragmentActivity2.this,DriverRouteActivity2.class);
                finish();
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11){
            finish();
        }
    }



    private void setupAutoCompleteFragment() {
        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCEwsWLe6soyNWGG0JTqJVKk4KnGFx_Ax8");
        }
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment1 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);



        autocompleteFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                myPlace = place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                location = place.getLatLng();
                locationId = place.getId();
                locationtitle = place.getName();
                mapFragment.getMapAsync(MapFragmentActivity2.this);



            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
        btnselect.setVisibility(View.VISIBLE);

        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(locationtitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
}
