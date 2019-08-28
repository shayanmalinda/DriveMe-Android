package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class DriverHomeActivity extends AppCompatActivity {

    private CardView driverroute;
    private CardView livelocation;
    private CardView passengerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        setTitle("Driver - Home");

        driverroute = findViewById(R.id.driverroute);
        livelocation = findViewById(R.id.livelocation);
        passengerlist = findViewById(R.id.passengerlist);

        driverroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this,DriverRouteActivity.class);
                startActivity(intent);
            }
        });

        livelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this,DriverMapActivity.class);
                startActivity(intent);
            }
        });

        passengerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this,DriverPassengerListActivity.class);
                startActivity(intent);
            }
        });

    }

}
