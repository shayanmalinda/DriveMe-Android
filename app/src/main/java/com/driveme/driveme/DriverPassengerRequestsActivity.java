package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverPassengerRequestsActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_passenger_requests);setTitle("Passenger Requests");

        getPassengerRequestList();
    }



    public void getPassengerRequestList(){

        db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        final String userId = cu.getDriverId();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.passenger_request_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverPassengerRequestsActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        db.collection("users/user/passenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean flag = false;
                if(!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                        if(querySnapshot.getString("tempDriverId")!=null && querySnapshot.getString("tempDriverId").equals(userId)){
                            HashMap<String,String> map  = new HashMap();
                            flag = true;
                            map.put("passengerId",querySnapshot.getId());
                            map.put("tempDriverId",querySnapshot.getString("tempDriverId"));
                            map.put("name","Name  :   "+querySnapshot.getString("name"));
                            map.put("email","Email  :   "+querySnapshot.getString("email"));
                            map.put("address","Address  :   "+querySnapshot.getString("address"));
                            map.put("phone","Phone  :   "+querySnapshot.getString("phone"));
                            map.put("pickupLocation","Pickup Location  :   "+querySnapshot.getString("pickupLocation"));
                            list.add(map);
                        }
                        int layout = R.layout.item_passenger_request;
                        String[] cols = {"tempDriverId","passengerId","name","email","address","phone","pickupLocation"};
                        int[] views = {R.id.tempDriverId,R.id.passengerId,R.id.passengerName,R.id.passengerEmail,R.id.passengerAddress,R.id.passengerPhone,R.id.pickupLocation};
                        SimpleAdapter adapter = new SimpleAdapter(DriverPassengerRequestsActivity.this,list,layout,cols,views);
                        lv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                }
                if(!flag){
//                    finish();

                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Any Passengers", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }



    public void acceptRequest(View v){

        db = FirebaseFirestore.getInstance();

        LinearLayout view = (LinearLayout)v.getParent().getParent();
        TextView txtpassengerId = view.findViewById(R.id.passengerId);
        TextView txttempDriverId = view.findViewById(R.id.tempDriverId);
        String passengerId = txtpassengerId.getText().toString();
        String tempDriverId = txttempDriverId.getText().toString();

        db.document("users/user/passenger/"+passengerId).update("driverId",tempDriverId);
        db.document("users/user/passenger/"+passengerId).update("tempDriverId","");

        getPassengerRequestList();

        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Passenger Accepted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    public void declineRequest(View v){
        db = FirebaseFirestore.getInstance();

        LinearLayout view = (LinearLayout)v.getParent().getParent();
        TextView txtpassengerId = view.findViewById(R.id.passengerId);
        TextView txttempDriverId = view.findViewById(R.id.tempDriverId);
        String passengerId = txtpassengerId.getText().toString();
        String tempDriverId = txttempDriverId.getText().toString();

        db.document("users/user/passenger/"+passengerId).update("driverId","");
        db.document("users/user/passenger/"+passengerId).update("tempDriverId","");

        getPassengerRequestList();

        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Passenger Declined", Snackbar.LENGTH_LONG);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
