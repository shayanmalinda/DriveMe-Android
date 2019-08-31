package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PassengerMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean zoomFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        messLocation();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void messLocation() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        String userId = cu.getCurrentuserID();

        db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String driverId = documentSnapshot.getString("driverId");

                DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId).child("l");

                currentDBcordinates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Create an array of markers
                        mMap.clear();
                        Double lat=0.0,lng=0.0;
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                            if(userSnapshot.getKey().equals("0")){
                                lat = Double.valueOf(userSnapshot.getValue().toString());
                            }
                            else{
                                lng = Double.valueOf(userSnapshot.getValue().toString());
                            }
                        }
                        LatLng driver = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(driver).title("Driver"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(driver));
                        if(zoomFlag){
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(17).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                        zoomFlag = false;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}
