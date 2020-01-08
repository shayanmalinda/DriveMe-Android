package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DriverUserListActivity extends AppCompatActivity {

    private CardView viewPassengers;
    private CardView viewParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_user_list);

        setTitle("View Passengers");

        viewPassengers = findViewById(R.id.viewPassengers);
        viewParents = findViewById(R.id.viewParents);

        viewPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverUserListActivity.this,DriverPassengerListActivity.class);
                startActivity(intent);
            }
        });

        viewParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverUserListActivity.this,DriverParentListActivity.class);
                startActivity(intent);
            }
        });
    }
}
