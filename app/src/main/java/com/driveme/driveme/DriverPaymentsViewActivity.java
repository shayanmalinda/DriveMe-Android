package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class DriverPaymentsViewActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_payments_view);
        db = FirebaseFirestore.getInstance();

        setTitle("View Payments");

        Bundle bundle = getIntent().getExtras();
        final String passengerId = bundle.getString("passengerId");


        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.payment_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverPaymentsViewActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser cu = new CurrentUser();
        String driverId = cu.getDriverId();

        db.collection("users/user/driver/"+driverId+"/payments/"+passengerId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    HashMap<String,String> map  = new HashMap();
                    map.put("date","Date  :   "+querySnapshot.getString("date"));
                    map.put("value","Payment  :   Rs. "+querySnapshot.getString("value")+ " /=");
                    if(querySnapshot.getBoolean("isAccepted")){
                        map.put("isAccepted","Passenger Accepted");
                    }
                    else{
                        map.put("isAccepted","Passenger Not Accepted");
                    }
                    list.add(map);
                }
                int layout = R.layout.item_payment2;
                String[] cols = {"date","value","isAccepted"};
                int[] views = {R.id.date,R.id.value,R.id.isAccepted};
                SimpleAdapter adapter = new SimpleAdapter(DriverPaymentsViewActivity.this,list,layout,cols,views);
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
