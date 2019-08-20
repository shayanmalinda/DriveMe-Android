package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                DriverRouteDetails2 dr2 = new DriverRouteDetails2(dr.getStartPlace(),dr.getEndPlace(),dr.getStartTime(),dr.getEndTime());

                CurrentUser cu = new CurrentUser();
                String userId = cu.getCurrentuserID();


                CollectionReference route = db.collection("users/user/driver/"+userId+"/routes");

                route.add(dr2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        dialog.dismiss();
                        documentId = documentReference.getId();
                        Toast.makeText(DriverRouteActivity2.this, "Route Added 1", Toast.LENGTH_SHORT).show();
                        addcheckpoints();
                        try {
                            DriverRouteActivity.fa.finish();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        dialog.dismiss();
                        Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void addcheckpoints(){

        CurrentUser cu = new CurrentUser();
        String userId = cu.getCurrentuserID();
        DriverRouteDetails dr = new DriverRouteDetails();
        checkpoints = dr.getCheckpoints();
        CollectionReference route2 = db.collection("users/user/driver/"+userId+"/routes/"+documentId+"/checkpoints");

        for(int n = 0; n < checkpoints.length(); n++){

            CheckpointDetails cd = null;

            try {
                JSONObject object = checkpoints.getJSONObject(n);
                cd = new CheckpointDetails(object.getString("placeid"),
                        object.getString("placename"),
                        object.getString("placelat"),
                        object.getString("placelng"),
                        object.getString("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            route2.add(cd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
//                        dialog.dismiss();
                    Toast.makeText(DriverRouteActivity2.this, "Route Added 2", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        dialog.dismiss();
                    Toast.makeText(DriverRouteActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
