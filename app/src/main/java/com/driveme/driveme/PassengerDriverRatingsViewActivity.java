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

public class PassengerDriverRatingsViewActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_driver_ratings_view);
        setTitle("Driver Ratings");

        Bundle bundle = getIntent().getExtras();
        String driverId = bundle.getString("driverId");

        db = FirebaseFirestore.getInstance();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.rating_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerDriverRatingsViewActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db.collection("users/user/driver/"+driverId+"/ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    HashMap<String,String> map  = new HashMap();
                    map.put("date","Date  :   "+querySnapshot.getString("date"));
                    map.put("stars","Ratings  :   "+String.valueOf(querySnapshot.getDouble("stars"))+" / 5.0");
                    map.put("ratings","Comment  :   "+querySnapshot.getString("rating"));
                    list.add(map);
                }
                int layout = R.layout.item_rating;
                String[] cols = {"date","stars","ratings"};
                int[] views = {R.id.date,R.id.stars,R.id.rating};
                SimpleAdapter adapter = new SimpleAdapter(PassengerDriverRatingsViewActivity.this,list,layout,cols,views);
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
