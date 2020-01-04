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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassengerPassengerListActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_passenger_list);
        setTitle("Passenger List");

        getPassengerList();


    }

    public void getPassengerList(){

        db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        final String passengerId = cu.getPassengerId();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.passenger_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerPassengerListActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db.collection("users/user/passenger").document(passengerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final String driverId = documentSnapshot.getString("driverId");

                db.collection("users/user/passenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean flag = false;
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                                if(querySnapshot.getString("driverId")!=null && querySnapshot.getString("driverId").equals(driverId)){
                                    HashMap<String,String> map  = new HashMap();
                                    flag = true;
                                    map.put("passengerId",querySnapshot.getId());
                                    map.put("name","Name  :   "+querySnapshot.getString("name"));
                                    map.put("email","Email  :   "+querySnapshot.getString("email"));
                                    map.put("address","Address  :   "+querySnapshot.getString("address"));
                                    map.put("phone","Phone  :   "+querySnapshot.getString("phone"));
                                    map.put("pickupLocation","Pickup Location  :   "+querySnapshot.getString("pickupLocation"));
                                    list.add(map);
                                }
                                int layout = R.layout.item_passenger2;
                                String[] cols = {"passengerId","name","email","address","phone","pickupLocation"};
                                int[] views = {R.id.passengerId,R.id.passengerName,R.id.passengerEmail,R.id.passengerAddress,R.id.passengerPhone,R.id.pickupLocation};
                                SimpleAdapter adapter = new SimpleAdapter(PassengerPassengerListActivity.this,list,layout,cols,views);
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
        });




    }


}
