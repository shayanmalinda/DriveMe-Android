package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverViewVehicleDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView vehicleNumber;
    private TextView vehicleChassis;
    private TextView vehicleType;
    private TextView availableSeats;;

    private Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_vehicle_details);
        setTitle("Vehicle Details");

        db = FirebaseFirestore.getInstance();
        vehicleNumber = findViewById(R.id.txtVehicleNumber);
        vehicleChassis = findViewById(R.id.txtChassisNumber);
        vehicleType = findViewById(R.id.txtVehicleType);
        availableSeats = findViewById(R.id.txtAvailableSeats);

        btnGoBack = findViewById(R.id.btngoback);

        getVehicleDetails();

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getVehicleDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverViewVehicleDetailsActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser usr = new CurrentUser();
        String userID = usr.getDriverId();

        db.document("users/user/driver/"+userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    vehicleNumber.setText("Vehicle Number :  "+documentSnapshot.get("vehicleNumber").toString());
                    vehicleChassis.setText("Chassis Number :  "+documentSnapshot.get("vehicleChassis").toString());
                    vehicleType.setText("Vehicle Type :  "+documentSnapshot.get("vehicleType").toString());
                    availableSeats.setText("Number of Available Seets :  "+documentSnapshot.get("availableSeets").toString());
                }
                dialog.dismiss();
            }
        });

    }
}
