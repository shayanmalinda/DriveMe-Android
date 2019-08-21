package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PassengerRouteSearchActivity extends AppCompatActivity {

    private TextView startLocation;
    private TextView endLocation;

    private EditText etstartradius;
    private EditText etendradius;

    private float startradius;
    private float endradius;

    private Place startPlace;
    private Place endPlace;

    private Button btnSearch;
    private String userId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route_search);

        setTitle("Route Search");

        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        startLocation = findViewById(R.id.btnstartlocation);
        endLocation = findViewById(R.id.btnendlocation);
        btnSearch = findViewById(R.id.btnSearch);
        etstartradius = findViewById(R.id.etstartradius);
        etendradius = findViewById(R.id.etendradius);
        db = FirebaseFirestore.getInstance();

        CurrentUser cu = new CurrentUser();
        userId = cu.getCurrentuserID();

        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,1);
            }
        });

        endLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,2);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String radius1 = etstartradius.getText().toString();
                String radius2 = etendradius.getText().toString();

                if(!radius1.isEmpty()){
                    startradius = Float.parseFloat(radius1);
                }
                if(!radius2.isEmpty()){
                    endradius = Float.parseFloat(radius2);
                }


                final Location startLocation = new Location("start location");
                final Location endLocation = new Location("end location");
                try {
                    startLocation.setLatitude(startPlace.getLatLng().latitude);
                    startLocation.setLongitude(startPlace.getLatLng().longitude);
                    endLocation.setLatitude(endPlace.getLatLng().latitude);
                    endLocation.setLongitude(endPlace.getLatLng().longitude);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                db.collection("users/user/driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                        if(!queryDocumentSnapshots1.isEmpty()){
                            List<DocumentSnapshot> list1 = queryDocumentSnapshots1.getDocuments();
                            for(DocumentSnapshot d1: list1){
                                String driverId = d1.getId();
                                db.collection("users/user/driver").document(driverId).collection("checkpoints").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                                                if(!queryDocumentSnapshots2.isEmpty()){
                                                    List<DocumentSnapshot> list2 = queryDocumentSnapshots2.getDocuments();
                                                    int order = 0;
                                                    for(DocumentSnapshot d2: list2) {
                                                        Log.e("Documentsnapshot",d2.getString("placeName"));
                                                        order++;
                                                        Location currlocation = new Location("current Location");
                                                        try {
                                                            currlocation.setLatitude(Double.parseDouble(d2.getString("placeLat")));
                                                            currlocation.setLongitude(Double.parseDouble(d2.getString("placeLng")));
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                        float startdistance = getDistance(currlocation,startLocation);
                                                        float enddistance = getDistance(currlocation,endLocation);

                                                        if(startPlace!=null && endPlace!=null){

                                                        }
                                                        else if(startPlace==null && endPlace!=null){

                                                        }
                                                        else if (startPlace!=null && endPlace==null){
                                                            if(!d2.getBoolean("isEnd")){
                                                                if(startdistance<=startradius){
                                                                    Log.e("Documentcorrect",d2.getString("placeName"));
                                                                    Toast.makeText(PassengerRouteSearchActivity.this, d2.getString("placeName")+"", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                        else{

                                                        }

                                                    }
                                                }
                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

    public float getDistance(Location l1, Location l2){
        float distance = 0;
        distance = l1.distanceTo(l2);
        return distance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            SelectedPlace s = new SelectedPlace();
            startPlace = s.getPlace();
            if(startPlace!=null){
                startLocation.setText(startPlace.getName().toString());
            }
        }
        if(requestCode==2){
            SelectedPlace s = new SelectedPlace();
            endPlace = s.getPlace();
            if(endPlace!=null) {
                endLocation.setText(endPlace.getName().toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
