package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DriverNotificationsActivity extends AppCompatActivity {

    private CardView dailyAvailability;
    private CardView passengerRequests;
    private CardView parentRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_notifications);

        setTitle("Notifications");

        dailyAvailability = findViewById(R.id.dailyAvailability);
        passengerRequests = findViewById(R.id.passengerRequests);
        parentRequests = findViewById(R.id.parentRequests);

        dailyAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverNotificationsActivity.this,DriverPassengerAvailabilityActivity.class);
                startActivity(intent);
            }
        });

        passengerRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverNotificationsActivity.this,DriverPassengerRequestsActivity.class);
                startActivity(intent);
            }
        });

        parentRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverNotificationsActivity.this,DriverParentRequestActivity.class);
                startActivity(intent);
            }
        });
    }
}
