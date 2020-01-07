package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParentRouteSearch3Activity extends AppCompatActivity {

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

    TextView txtPlace;
    TextView txtTime;

    Button btnAddRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_route_search3);
        setTitle("Route Search - Results");

        db = FirebaseFirestore.getInstance();
        txtPlace = findViewById(R.id.txtPlace);
        txtTime = findViewById(R.id.txtTime);
        btnAddRoute = findViewById(R.id.addRoute);

        Bundle bundle = getIntent().getExtras();
        driverId = bundle.getString("driverId");
        allowedPassengers = bundle.getString("allowedPassengers");
        currentPassengers = bundle.getString("currentPassengers");
        startPlace = bundle.getString("startPlace");
        endPlace = bundle.getString("endPlace");
        startTime = bundle.getString("startTime");
        endTime = bundle.getString("endTime");
        driverEmail = bundle.getString("driverEmail");
        driverTelephone = bundle.getString("driverTelephone");
        vehicleType = bundle.getString("vehicleType");
        isAC = bundle.getString("isAC");
        vehicleNumber = bundle.getString("vehicleNumber");

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

        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.checkpoint_list);


        db.collection("users/user/driver/"+driverId+"/checkpoints/").orderBy("order").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentsnapshots: queryDocumentSnapshots){
                    if(documentsnapshots.getBoolean("isActive")){
                        HashMap<String,String> map  = new HashMap();
                        map.put("placeName",documentsnapshots.getString("placeName"));
                        map.put("time",documentsnapshots.getString("time"));
                        list.add(map);
                    }
                }
                int layout = R.layout.item_checkpoint;
                String[] cols = {"placeName","time"};
                int[] views = {R.id.txtPlace,R.id.txtTime};
                SimpleAdapter adapter = new SimpleAdapter(ParentRouteSearch3Activity.this,list,layout,cols,views);
                lv.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        CurrentUser cu = new CurrentUser();
        final String userId = cu.getParentId();

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ParentRouteSearch3Activity.this,MapFragmentActivity4.class);
                intent.putExtra("userId",userId);
                intent.putExtra("driverId",driverId);
                startActivity(intent);
                ParentRouteSearch3Activity.this.finish();
            }
        });

    }
}
