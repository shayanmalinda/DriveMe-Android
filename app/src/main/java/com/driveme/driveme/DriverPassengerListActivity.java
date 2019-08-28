package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverPassengerListActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_passenger_list);

        setTitle("Passenger List");

        db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        final String userId = cu.getCurrentuserID();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.passenger_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverPassengerListActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        db.collection("users/user/passenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    if(querySnapshot.getString("driverId").equals(userId)){
                        HashMap<String,String> map  = new HashMap();
                        map.put("name","Name  :   "+querySnapshot.getString("name"));
                        map.put("email","Email  :   "+querySnapshot.getString("email"));
                        map.put("address","Address  :   "+querySnapshot.getString("address"));
                        map.put("phone","Phone  :   "+querySnapshot.getString("phone"));
                        map.put("pickupLocation","Pickup Location  :   "+querySnapshot.getString("pickupLocation"));
                        list.add(map);
                    }
                    int layout = R.layout.item_passenger;
                    String[] cols = {"name","email","address","phone","pickupLocation"};
                    int[] views = {R.id.passengerName,R.id.passengerEmail,R.id.passengerAddress,R.id.passengerPhone,R.id.pickupLocation};
                    SimpleAdapter adapter = new SimpleAdapter(DriverPassengerListActivity.this,list,layout,cols,views);
                    lv.setAdapter(adapter);
                    dialog.dismiss();
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
