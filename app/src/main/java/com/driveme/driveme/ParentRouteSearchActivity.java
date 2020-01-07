package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ParentRouteSearchActivity extends AppCompatActivity {

    private TextView startLocationtxt;
    private TextView endLocationtxt;

    private EditText etstartradius;
    private EditText etendradius;

    private float startradius;
    private float endradius;

    private Place startPlace = null;
    private Place endPlace = null;

    private Button btnSearch;
    private String userId;
    FirebaseFirestore db;

    final int count1[] = {0};
    final int count2[] = {0};

    final Location startLocation = new Location("start location");
    final Location endLocation = new Location("end location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_route_search);
        setTitle("Route Search");

        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        startLocationtxt = findViewById(R.id.btnstartlocation);
        endLocationtxt = findViewById(R.id.btnendlocation);
        btnSearch = findViewById(R.id.btnSearch);
        etstartradius = findViewById(R.id.etstartradius);
        etendradius = findViewById(R.id.etendradius);
        db = FirebaseFirestore.getInstance();

        final CurrentUser cu = new CurrentUser();
        userId = cu.getParentId();

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentRouteSearchActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();
        db.collection("users/user/driver").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final DocumentSnapshot d1:task.getResult()){
                        db.collection("users/user/driver").document(d1.getId()).collection("checkpoints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                for(DocumentSnapshot d2:task2.getResult()){
                                    count1[0]++;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

            }
        });


        startLocationtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,1);
            }
        });

        endLocationtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,2);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData(new FirestoreCallback() {
                    @Override
                    public void onCallback() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ParentRouteSearchActivity.this);
                        builder.setCancelable(false); // if you want user to wait for some process to finish,
                        builder.setView(R.layout.layout_loading_dialog);
                        final AlertDialog dialog = builder.create();
                        dialog.show();


                        for(final String driverId:driverId){
                            db.collection("users/user/driver").document(driverId).collection("checkpoints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                    int order = 0;
                                    boolean isStart = false;
                                    boolean isEnd = false;
                                    double startOrder=0;
                                    double endOrder=0;
                                    for(DocumentSnapshot d2:task2.getResult()){
                                        count2[0]++;
                                        order++;
                                        Location currlocation = new Location("current Location");
                                        try {
                                            currlocation.setLatitude(Double.parseDouble(d2.getString("placeLat")));
                                            currlocation.setLongitude(Double.parseDouble(d2.getString("placeLng")));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        float enddistance = getDistance(currlocation, endLocation);
                                        float startdistance = getDistance(currlocation, startLocation);
                                        if (d2.getBoolean("isActive")) {
                                            if (startPlace != null && endPlace != null) {
                                                if (!d2.getBoolean("isEnd")) {
                                                    if (startdistance <= startradius) {
                                                        isStart = true;
                                                        startOrder=d2.getDouble("order");
                                                    }
                                                }
                                                if (!d2.getBoolean("isStart")) {
                                                    if (enddistance <= endradius) {
                                                        isEnd = true;
                                                        endOrder=d2.getDouble("order");
                                                    }
                                                }
                                                if(isStart && isEnd && (endOrder>startOrder)){
                                                    filterRoute(driverId);
                                                }


                                            } else if (startPlace == null && endPlace != null) {
                                                if (!d2.getBoolean("isStart")) {
                                                    if (enddistance <= endradius) {
                                                        filterRoute(driverId);
                                                    }
                                                }
                                            } else if (startPlace != null && endPlace == null) {
                                                if (!d2.getBoolean("isEnd")) {
                                                    if (startdistance <= startradius) {
                                                        filterRoute(driverId);
                                                    }
                                                }
                                            } else {
                                                filterRoute(driverId);
                                            }
                                        }
                                        Log.e("counts",count1[0]+" "+count2[0]);
                                        if(count2[0]==count1[0]){
                                            Intent intent = new Intent(ParentRouteSearchActivity.this,ParentRouteSearch2Activity.class);
                                            intent.putStringArrayListExtra("filteredRoutes",filteredRoutes);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();

                                }
                            });
                        }
                    }
                });
            }
        });

    }

    ArrayList<String> driverId = new ArrayList<String>();


    private void readData(final FirestoreCallback firestoreCallback){

        String radius1 = etstartradius.getText().toString();
        String radius2 = etendradius.getText().toString();

        if(!radius1.isEmpty()){startradius = Float.parseFloat(radius1);}
        if(!radius2.isEmpty()){endradius = Float.parseFloat(radius2);}
        try {
            startLocation.setLatitude(startPlace.getLatLng().latitude);
            startLocation.setLongitude(startPlace.getLatLng().longitude);
        }
        catch (Exception e){ e.printStackTrace();}
        try{
            endLocation.setLatitude(endPlace.getLatLng().latitude);
            endLocation.setLongitude(endPlace.getLatLng().longitude);
        }
        catch (Exception e){ e.printStackTrace();}

        db.collection("users/user/driver").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final DocumentSnapshot d1:task.getResult()){
                        driverId.add(d1.getId());
                    }
                    firestoreCallback.onCallback();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private interface FirestoreCallback{
        void onCallback();
    }

    ArrayList<String> filteredRoutes = new ArrayList<String>();
    public void filterRoute(String routeSnapshot){
        if(!filteredRoutes.contains(routeSnapshot)){
            filteredRoutes.add(routeSnapshot);
        }
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
                startLocationtxt.setText(startPlace.getName().toString());
            }
        }
        if(requestCode==2){
            SelectedPlace s = new SelectedPlace();
            endPlace = s.getPlace();
            if(endPlace!=null) {
                endLocationtxt.setText(endPlace.getName().toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        count2[0]=0;
        driverId.clear();
        filteredRoutes.clear();
    }
}
