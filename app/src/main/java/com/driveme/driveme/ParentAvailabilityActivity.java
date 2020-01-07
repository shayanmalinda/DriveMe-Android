package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ParentAvailabilityActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private Button btnAddAvailability;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_availability);
        setTitle("Daily Availability");

        db = FirebaseFirestore.getInstance();

        final DatePicker datePicker = findViewById(R.id.datePicker);
        Button btnAddAvailability = findViewById(R.id.btnaddavailability);
        final Switch btnSwitch = findViewById(R.id.btnSwitch);
        btnAddAvailability = findViewById(R.id.btnaddavailability);

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(currentYear, currentMonth, currentDay, null);
        datePicker.setMinDate(System.currentTimeMillis());


        btnAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ParentAvailabilityActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final int year = datePicker.getYear();
                final int month = datePicker.getMonth()+1;
                final int day = datePicker.getDayOfMonth();


                boolean isAvailable = false;
                if(btnSwitch.isChecked()){
                    isAvailable = true;
                }
                else {
                    isAvailable = false;
                }
                CurrentUser cu = new CurrentUser();
                final String parentId = cu.getParentId();
                final Map<String, Object> objAvailability = new HashMap<>();
                objAvailability.put("availabilty",isAvailable);


                final Map<String, Object> objExist = new HashMap<>();
                objAvailability.put("exists",true);

                db.document("users/user/parent/"+parentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String dateString=null;
                        if(month<10 && day<10){
                            dateString = year+"0"+month+"0"+day;
                        }
                        else if(month<10){
                            dateString = year+"0"+month+day;
                        }
                        else if(day<10){
                            dateString = year+month+"0"+day;
                        }
                        else{
                            dateString = ""+year+month+day;
                        }

                        String driverId = documentSnapshot.getString("driverId");
                        db.collection("users/user/parent/"+parentId+"/availability").document(dateString).set(objAvailability);
                        db.collection("users/user/driver/"+driverId+"/availability/").document(parentId).set(objExist);
                        db.collection("users/user/driver/"+driverId+"/availability/"+parentId+"/availability").document(dateString).set(objAvailability);

                        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Availability Added", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();

                        dialog.dismiss();
                    }
                });



            }
        });

    }
}
