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

public class PassengerPaymentViewActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_payment_view);

        db = FirebaseFirestore.getInstance();

        setTitle("View Payments");

        CurrentUser cu = new CurrentUser();
        String passengerId = cu.getPassengerId();


        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.payment_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerPaymentViewActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        db.collection("users/user/passenger/"+passengerId+"/payments/").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    if(querySnapshot.getBoolean("isAccepted")){
                        HashMap<String,String> map  = new HashMap();
                        map.put("date","Date  :   "+querySnapshot.getString("date"));
                        map.put("value","Payment  :   Rs. "+querySnapshot.getString("value")+ " /=");
                        list.add(map);
                    }
                }
                int layout = R.layout.item_payment;
                String[] cols = {"date","value"};
                int[] views = {R.id.date,R.id.value};
                SimpleAdapter adapter = new SimpleAdapter(PassengerPaymentViewActivity.this,list,layout,cols,views);
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
