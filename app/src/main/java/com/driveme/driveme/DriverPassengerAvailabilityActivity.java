package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class DriverPassengerAvailabilityActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_passenger_availability);

        setTitle("Daily Availability");Bundle bundle = getIntent().getExtras();

        db = FirebaseFirestore.getInstance();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.availability_list);

        CurrentUser cu = new CurrentUser();
        final String driverId = cu.getDriverId();

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverPassengerAvailabilityActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db.collection("users/user/driver/"+driverId+"/availability").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){

                    Calendar c = Calendar.getInstance();
                    final int currentYear = c.get(Calendar.YEAR);
                    final int currentMonth = c.get(Calendar.MONTH)+1;
                    final int currentDay = c.get(Calendar.DAY_OF_MONTH);

                    String currentDateString =null;
                    if(currentMonth<10 && currentDay<10){
                        currentDateString = currentYear+"0"+currentMonth+"0"+currentDay;
                    }
                    else if(currentMonth<10){
                        currentDateString = currentYear+"0"+currentMonth+currentDay;
                    }
                    else if(currentDay<10){
                        currentDateString = currentYear+currentMonth+"0"+currentDay;
                    }
                    else{
                        currentDateString = ""+currentYear+currentMonth+currentDay;

                    }


                    final int currentDateInt = Integer.parseInt(currentDateString);

                    String passengerId = querySnapshot.getId();

                    db.collection("users/user/driver/"+driverId+"/availability/"+passengerId+"/availability").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                            for(QueryDocumentSnapshot querySnapshot2: queryDocumentSnapshots2){
                                String dbDateString = querySnapshot2.getId();
                                int dbDateInt = Integer.parseInt(dbDateString);
                                if(dbDateInt>=currentDateInt){
                                    Log.d("dbb",""+dbDateInt);
                                    int dbYear = dbDateInt/10000;
                                    int dbMonth = (dbDateInt/100)%100;
                                    int dbDay = dbDateInt%100;


                                    String dateString=null;
                                    if(dbMonth<10 && dbDay<10){
                                        dateString = dbYear+"-"+"0"+dbMonth+"-"+"0"+dbDay;
                                    }
                                    else if(dbMonth<10){
                                        dateString = dbYear+"-"+"0"+dbMonth+"-"+dbDay;
                                    }
                                    else if(dbDay<10){
                                        dateString = dbYear+"-"+dbMonth+"0"+"-"+dbDay;
                                    }
                                    else{
                                        dateString = dbYear+"-"+dbMonth+"-"+dbDay;
                                    }


                                    HashMap<String,String> map  = new HashMap();
                                    String date =
                                    map.put("date","Date  :   "+dateString);
                                    if(querySnapshot2.getBoolean("availabilty")){
                                        map.put("availability","Status  :   Available");
                                    }
                                    else{
                                        map.put("availability","Status  :   Unavailable");
                                    }
                                    list.add(map);
                                }

                                dialog.dismiss();
                            }

                            int layout = R.layout.item_availability;
                            String[] cols = {"date","availability"};
                            int[] views = {R.id.date,R.id.availability};
                            SimpleAdapter adapter = new SimpleAdapter(DriverPassengerAvailabilityActivity.this,list,layout,cols,views);
                            lv.setAdapter(adapter);
                        }
                    });

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
