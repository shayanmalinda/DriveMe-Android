package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PassengerHomeActivity extends AppCompatActivity {

    private CardView myprofile;
    private CardView routesearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);

        setTitle("Passenger - Home");

        myprofile = findViewById(R.id.myprofile);
        routesearch = findViewById(R.id.searchroute);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerHomeActivity.this,PassengerProfileActivity.class);
                startActivity(intent);
            }
        });

        routesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PassengerHomeActivity.this,PassengerRouteSearchActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(PassengerHomeActivity.this,MapFragmentActivity.class);
//                startActivity(intent);

            }
        });
    }
}
