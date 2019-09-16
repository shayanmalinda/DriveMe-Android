package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.internal.et;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerRouteSearch2Activity extends AppCompatActivity {


    private static ArrayList<String> filteredRoutes = new ArrayList<String>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route_search2);
        setTitle("Route Search - Results");
        Bundle extras = getIntent().getExtras();

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerRouteSearch2Activity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db = FirebaseFirestore.getInstance();
        final ListView lv = findViewById(R.id.route_list);

        filteredRoutes = extras.getStringArrayList("filteredRoutes");

        final List<HashMap<String, String>> list = new ArrayList<>();

        if(filteredRoutes.isEmpty()){
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Results", Snackbar.LENGTH_LONG);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            dialog.dismiss();
        }

        for(final String driverId:filteredRoutes){

            db.document("users/user/driver/"+driverId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    HashMap<String,String> map  = new HashMap();
                    map.put("driverId",documentSnapshot.getId());
                    map.put("allowedPassengers: ",String.valueOf(documentSnapshot.getDouble("allowedPassengers")));
                    map.put("currentPassengers: ",String.valueOf(documentSnapshot.getDouble("currentPassengers")));
                    map.put("startPlace","Start Place  :  "+documentSnapshot.getString("startPlaceName"));
                    map.put("endPlace","End Place  :  "+documentSnapshot.getString("endPlaceName"));
                    map.put("startTime","Start Time  :  "+documentSnapshot.getString("startTime"));
                    map.put("endTime","End Time  :  "+documentSnapshot.getString("endTime"));
                    map.put("driverEmail","Driver Email  :  "+documentSnapshot.getString("email"));
                    map.put("driverTelephone","Driver Telephone  :  "+documentSnapshot.getString("driverTelephone"));
                    map.put("vehicleType","Vehicle Type  :  "+documentSnapshot.getString("vehicleType"));
                    if(documentSnapshot.getBoolean("isAC")){
                        map.put("isAC","Air Condition  :  "+"Yes");
                    }
                    else{
                        map.put("isAC","Air Condition  :  "+"No");
                    }
                    map.put("vehicleNumber","Vehicle Number  :  "+documentSnapshot.getString("vehicleNumber"));
                    if(documentSnapshot.getDouble("allowedPassengers")>documentSnapshot.getDouble("currentPassengers")){
                        list.add(map);
                    }


                    int layout = R.layout.item_route;
                    String[] cols = {"driverId","allowedPassengers","currentPassengers","startPlace","startTime","endPlace","endTime",
                            "driverEmail","driverTelephone","vehicleType","isAC","vehicleNumber"};
                    int[] views = {R.id.driverId,R.id.allowedPassengers,R.id.currentPassengers,R.id.startPlace,R.id.startTime,R.id.endPlace,
                            R.id.endTime,R.id.driverEmail,R.id.driverTelephone,R.id.vehicleType,R.id.isAC,R.id.vehicleNumber};
                    SimpleAdapter adapter = new SimpleAdapter(PassengerRouteSearch2Activity.this,list,layout,cols,views);
                    lv.setAdapter(adapter);
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    dialog.dismiss();
                }
            });
        }



    }

    public void viewdetails(View v){
        LinearLayout view = (LinearLayout)v.getParent();

        TextView etDriverId = view.findViewById(R.id.driverId);
        driverId = etDriverId.getText().toString();
        TextView etAllowedPassengers = view.findViewById(R.id.allowedPassengers);
        allowedPassengers = etAllowedPassengers.getText().toString();
        TextView etCurrentPassengers = view.findViewById(R.id.currentPassengers);
        currentPassengers = etCurrentPassengers.getText().toString();
        TextView etStartPlace = view.findViewById(R.id.startPlace);
        startPlace = etStartPlace.getText().toString();
        TextView etStartTime = view.findViewById(R.id.startTime);
        startTime = etStartTime.getText().toString();
        TextView etEndPlace = view.findViewById(R.id.endPlace);
        endPlace = etEndPlace.getText().toString();
        TextView etEndTime = view.findViewById(R.id.endTime);
        endTime = etEndTime.getText().toString();
        TextView etDriverEmail = view.findViewById(R.id.driverEmail);
        driverEmail = etDriverEmail.getText().toString();
        TextView etDriverTelephone = view.findViewById(R.id.driverTelephone);
        driverTelephone = etDriverTelephone.getText().toString();
        TextView etvehicleType = view.findViewById(R.id.vehicleType);
        vehicleType = etvehicleType.getText().toString();
        TextView etisAC = view.findViewById(R.id.isAC);
        isAC = etisAC.getText().toString();
        TextView etvehicleNumber = view.findViewById(R.id.vehicleNumber);
        vehicleNumber = etvehicleNumber.getText().toString();

        Intent intent = new Intent(PassengerRouteSearch2Activity.this,PassengerRouteSearch3Activity.class);
        intent.putExtra("driverId",driverId);
        intent.putExtra("allowedPassengers",allowedPassengers);
        intent.putExtra("currentPassengers",currentPassengers);
        intent.putExtra("startPlace",startPlace);
        intent.putExtra("startTime",startTime);
        intent.putExtra("endPlace",endPlace);
        intent.putExtra("endTime",endTime);
        intent.putExtra("driverEmail",driverEmail);
        intent.putExtra("driverTelephone",driverTelephone);
        intent.putExtra("vehicleType",vehicleType);
        intent.putExtra("isAC",isAC);
        intent.putExtra("vehicleNumber",vehicleNumber);
        startActivity(intent);
    }

}
