package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRouteActivity2 extends AppCompatActivity {

    private Place selectedPlace;
    private String selectedTime;
    private JSONArray checkpoints;
    private ListView list;

    private FirebaseFirestore db;
    private String documentId;

    private Button btnCheckpoints;
    private Button btnReset;
    private Button btnDone;
    final List<String> checkpointlist = new ArrayList<String>();


    String startPlaceId;
    Double startPlaceLat;
    Double startPlaceLng;
    String startPlaceName;
    String endPlaceId;
    Double endPlaceLat;
    Double endPlaceLng;
    String endPlaceName;
    String startTime;
    String endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_route2);

        setTitle("Driver Route");

        db = FirebaseFirestore.getInstance();

        DriverRouteDetails dr = new DriverRouteDetails();
        dr.setCheckpoints(null);

        SelectedTime st = new SelectedTime();
        st.setSelectedTime(null);

        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        checkpoints = new JSONArray();
        btnCheckpoints = findViewById(R.id.btnaddcheckpoints);
        btnReset = findViewById(R.id.btnReset);
        btnDone = findViewById(R.id.btnDone);

        btnCheckpoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRouteActivity2.this,MapFragmentActivity2.class);
                startActivityForResult(intent,10);

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverRouteActivity2.this.recreate();

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverRouteDetails dr = new DriverRouteDetails();

                Place startPlace = dr.getStartPlace();
                Place endPlace = dr.getEndPlace();

                startPlaceId = startPlace.getId();
                startPlaceLat = startPlace.getLatLng().latitude;
                startPlaceLng = startPlace.getLatLng().longitude;
                startPlaceName = startPlace.getName();

                endPlaceId = endPlace.getId();
                endPlaceLat = endPlace.getLatLng().latitude;
                endPlaceLng = endPlace.getLatLng().longitude;
                endPlaceName = endPlace.getName();

                startTime = dr.getStartTime();
                endTime = dr.getEndTime();

                Map<String, Object> map = new HashMap<>();

                map.put("startPlaceId",startPlaceId);
                map.put("startPlaceLat",startPlaceLat);
                map.put("startPlaceLng",startPlaceLng);
                map.put("startPlaceName",startPlaceName);
                map.put("endPlaceId",endPlaceId);
                map.put("endPlaceLat",endPlaceLat);
                map.put("endPlaceLng",endPlaceLng);
                map.put("endPlaceName",endPlaceName);
                map.put("startTime",startTime);
                map.put("endTime",endTime);

//                DriverRouteDetails2 dr2 = new DriverRouteDetails2(startPlaceId,startPlaceLat, startPlaceLng, startPlaceName, endPlaceId,
//                        endPlaceLat,endPlaceLng,endPlaceName,startTime,endTime);

                CurrentUser cu = new CurrentUser();
                String userId = cu.getCurrentuserID();

                db.collection("users/user/driver/").document(userId).update(map);

                addcheckpoints();


//                CollectionReference route = db.collection("users/user/driver/"+userId);
//
//                route.add(dr2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
////                        dialog.dismiss();
//                        documentId = documentReference.getId();
//                        Toast.makeText(DriverRouteActivity2.this, "Route Added 1", Toast.LENGTH_SHORT).show();
//                        addcheckpoints();
//                        try {
//                            DriverRouteActivity.fa.finish();
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                        dialog.dismiss();
//                        Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });

    }

    private void addcheckpoints(){



        AlertDialog.Builder builder = new AlertDialog.Builder(DriverRouteActivity2.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser cu = new CurrentUser();
        String userId = cu.getCurrentuserID();
        DriverRouteDetails dr = new DriverRouteDetails();
        checkpoints = dr.getCheckpoints();
        final CollectionReference route2 = db.collection("users/user/driver/"+userId+"/checkpoints");


        CurrentUser cu2 = new CurrentUser();
        final String userId2 = cu2.getCurrentuserID();

        route2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        db.collection("users/user/driver/"+userId2+"/checkpoints").document(d.getId()).update("isActive",false);
                    }
                }
                insertcheckpoints(route2);
                dialog.dismiss();
                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Route Added Successfully", Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public  void insertcheckpoints(CollectionReference route2){

        int order = 1;
        CheckpointDetails cd = null;

        //Adding startplace as a checkpoint
        try {
            cd = new CheckpointDetails(startPlaceId,startPlaceName,startPlaceLat.toString(),startPlaceLng.toString(),startTime, true,order,true,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        route2.add(cd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (checkpoints != null) {


            //adding checkpoints
            for(int n = 0; n < checkpoints.length(); n++) {

                order++;
                try {
                    JSONObject object = checkpoints.getJSONObject(n);
                    cd = new CheckpointDetails(object.getString("placeid"),
                            object.getString("placename"),
                            object.getString("placelat"),
                            object.getString("placelng"),
                            object.getString("time"), true,order,false,false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final int finalN = n;
                route2.add(cd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(finalN == checkpoints.length()-1){
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        order++;
        //Adding endplace as a checkpoint
        try {
            cd = new CheckpointDetails(endPlaceId,endPlaceName,endPlaceLat.toString(),endPlaceLng.toString(),endTime, true,order,false,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        route2.add(cd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            SelectedPlace sp = new SelectedPlace();
            SelectedTime st = new SelectedTime();
            selectedPlace = sp.getPlace();
            selectedTime = st.getSelectedTime();
            if(selectedTime!=null && selectedPlace!=null){
                JSONObject jo = new JSONObject();
                try {
                    jo.put("placeid",selectedPlace.getId());
                    jo.put("placename",selectedPlace.getName());
                    jo.put("placelat",selectedPlace.getLatLng().latitude);
                    jo.put("placelng",selectedPlace.getLatLng().longitude);
                    jo.put("time",selectedTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DriverRouteDetails dr = new DriverRouteDetails();
                JSONArray ja = new JSONArray();
                checkpoints = dr.getCheckpoints();


                list = findViewById(R.id.checkpointlist);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, checkpointlist);
                try {
                    list.setAdapter(arrayAdapter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                if(checkpoints!=null){
                    for(int n = 0; n < checkpoints.length(); n++)
                    {
                        try {
                            JSONObject object = checkpoints.getJSONObject(n);
                            ja.put(object);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                checkpointlist.add(selectedPlace.getName());
                arrayAdapter.notifyDataSetChanged();
                if(jo!=null){
                    ja.put(jo);
                    dr.setCheckpoints(ja);
                }

            }

        }
    }
}
