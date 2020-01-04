package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DriverNotificationsActivity extends AppCompatActivity {

    private CardView dailyAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_notifications);

        setTitle("Notifications");

        dailyAvailability = findViewById(R.id.dailyAvailability);

        dailyAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverNotificationsActivity.this,DriverPassengerAvailabilityActivity.class);
                startActivity(intent);
            }
        });
    }
}
