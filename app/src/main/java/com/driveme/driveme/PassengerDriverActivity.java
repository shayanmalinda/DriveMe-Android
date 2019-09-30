package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PassengerDriverActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private TextView txtDriverName;
    private TextView txtDriverEmail;
    private TextView txtDriverTelephone;

    private Button btndriverLocation;
    private Button btnviewrRatings;
    private Button btnnewRating;
    String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_driver);
        setTitle("Driver Details");

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerDriverActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser cu = new CurrentUser();
        String userId = cu.getCurrentuserID();

        db = FirebaseFirestore.getInstance();
        txtDriverEmail = findViewById(R.id.driverEmail);
        txtDriverName = findViewById(R.id.driverName);
        txtDriverTelephone = findViewById(R.id.driverTelephone);

        btndriverLocation = findViewById(R.id.viewDriverLocation);
        btnviewrRatings = findViewById(R.id.viewDriverRatings);
        btnnewRating = findViewById(R.id.rateNow);

        db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                driverId = documentSnapshot.getString("driverId");
                db.document("users/user/driver/"+driverId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverName = documentSnapshot.getString("name");
                        String driverEmail = documentSnapshot.getString("email");
                        String driverTelephone = documentSnapshot.getString("driverTelephone");
                        txtDriverEmail.setText("Email  :   "+driverEmail);
                        txtDriverName.setText("Name  :   "+driverName);
                        txtDriverTelephone.setText("Telephone  :   "+driverTelephone);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

        btndriverLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerDriverActivity.this,PassengerMapActivity.class);
                startActivity(intent);
            }
        });

        btnviewrRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerDriverActivity.this,PassengerDriverRatingsViewActivity.class);
                intent.putExtra("driverId",driverId);
                startActivity(intent);
            }
        });

        btnnewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerDriverActivity.this,PassengerDriverNewRatingActivity.class);
                intent.putExtra("driverId",driverId);
                startActivity(intent);
            }
        });

    }
}
