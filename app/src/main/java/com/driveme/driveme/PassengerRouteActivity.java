package com.driveme.driveme;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassengerRouteActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String driverId;
    String allowedPassengers;
    String currentPassengers;
    String startPlace;
    String endPlace;
    String startTime;
    String endTime;
    String driverEmail;
    String driverTelephone;
    String vehicleType;
    String isAC;
    String vehicleNumber;
    String pickupLocation;
    TextView txtpickuplocation;

    TextView txtPlace;
    TextView txtTime;

    Button btnremoveRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route);

        setTitle("My Route");
        db = FirebaseFirestore.getInstance();
        txtPlace = findViewById(R.id.txtPlace);
        txtTime = findViewById(R.id.txtTime);
        btnremoveRoute = findViewById(R.id.removeRoute);

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerRouteActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        CurrentUser cu = new CurrentUser();
        String passengerId = cu.getPassengerId();

        db.document("users/user/passenger/"+passengerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String driverId = documentSnapshot.getString("driverId");
                pickupLocation = "Pickup Location  :   "+documentSnapshot.getString("pickupLocation");
                getRouteDetails(driverId,dialog);
                getCheckpoints(driverId,dialog);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

        btnremoveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(PassengerRouteActivity.this);
//                builder2.setCancelable(false); // if you want user to wait for some process to finish,
//                builder2.setView(R.layout.layout_loading_dialog);
//                final AlertDialog dialog2 = builder2.create();
//                dialog2.show();

                CurrentUser cu = new CurrentUser();
                String passengerId = cu.getPassengerId();
                final String pId = passengerId;
                finish();
//                dialog2.dismiss();
                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Route Removed", Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();

                db.document("users/user/passenger/"+passengerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverId = documentSnapshot.getString("driverId");

                        db.collection("users/user/passenger/"+pId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                                    q.getReference().delete();
                                }
                            }
                        });


                        db.collection("users/user/driver/"+driverId+"/payments/"+pId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                                    q.getReference().delete();
                                }
                            }
                        });

                        db.document("users/user/passenger/"+pId).update("driverId", FieldValue.delete());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private void getRouteDetails(String driverId, final Dialog dialog){
        db.document("users/user/driver/"+driverId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String driverId = documentSnapshot.getId();
                allowedPassengers = String.valueOf(documentSnapshot.getDouble("allowedPassengers"));
                currentPassengers = String.valueOf(documentSnapshot.getDouble("currentPassengers"));
                startPlace = "Start Place  :  "+documentSnapshot.getString("startPlaceName");
                endPlace = "End Place  :  "+documentSnapshot.getString("endPlaceName");
                startTime = "Start Time  :  "+documentSnapshot.getString("startTime");
                endTime = "End Time  :  "+documentSnapshot.getString("endTime");
                driverEmail = "Driver Email  :  "+documentSnapshot.getString("email");
                driverTelephone = "Driver Telephone  :  "+documentSnapshot.getString("driverTelephone");
                vehicleType = "Vehicle Type  :  "+documentSnapshot.getString("vehicleType");
                if(documentSnapshot.getBoolean("isAC")!=null && documentSnapshot.getBoolean("isAC")){
                    isAC = "Air Condition  :  "+"Yes";
                }
                else{
                    isAC = "Air Condition  :  "+"No";
                }
                vehicleNumber = "Vehicle Number  :  "+documentSnapshot.getString("vehicleNumber");

                TextView etDriverId = findViewById(R.id.driverId);
                etDriverId.setText(driverId);
                TextView etAllowedPassengers = findViewById(R.id.allowedPassengers);
                etAllowedPassengers.setText(allowedPassengers);
                TextView etCurrentPassengers = findViewById(R.id.currentPassengers);
                etCurrentPassengers.setText(currentPassengers);
                TextView etStartPlace = findViewById(R.id.startPlace);
                etStartPlace.setText(startPlace);
                TextView etStartTime = findViewById(R.id.startTime);
                etStartTime.setText(startTime);
                TextView etEndPlace = findViewById(R.id.endPlace);
                etEndPlace.setText(endPlace);
                TextView etEndTime = findViewById(R.id.endTime);
                etEndTime.setText(endTime);
                TextView etDriverEmail = findViewById(R.id.driverEmail);
                etDriverEmail.setText(driverEmail);
                TextView etDriverTelephone = findViewById(R.id.driverTelephone);
                etDriverTelephone.setText(driverTelephone);
                TextView etvehicleType = findViewById(R.id.vehicleType);
                etvehicleType.setText(vehicleType);
                TextView etisAC = findViewById(R.id.isAC);
                etisAC.setText(isAC);
                TextView etvehicleNumber = findViewById(R.id.vehicleNumber);
                etvehicleNumber.setText(vehicleNumber);
                txtpickuplocation = findViewById(R.id.mypickupLocation);
                txtpickuplocation.setText(pickupLocation);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();
            }
        });
    }

    private void getCheckpoints(String driverId, final Dialog dialog){
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.checkpoint_list);
        db.collection("users/user/driver/"+driverId+"/checkpoints").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    if(documentSnapshot.getBoolean("isActive")) {
                        HashMap<String, String> map = new HashMap();
                        map.put("placeName", documentSnapshot.getString("placeName"));
                        map.put("time", documentSnapshot.getString("time"));
                        list.add(map);
                    }
                }
                int layout = R.layout.item_checkpoint;
                String[] cols = {"placeName","time"};
                int[] views = {R.id.txtPlace,R.id.txtTime};
                SimpleAdapter adapter = new SimpleAdapter(PassengerRouteActivity.this,list,layout,cols,views);
                lv.setAdapter(adapter);
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();
            }
        });
    }
}
