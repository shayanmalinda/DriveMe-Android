package com.driveme.driveme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Driver;

public class DriverRouteActivity extends AppCompatActivity {


    private TextView startLocation;
    private TextView endLocation;
    private TextView startTime;
    private TextView endTime;

    private Place startPlace=null;
    private Place endPlace=null;
    private String startTimeString = null;
    private String endTimeString = null;

    private Button btnDone;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_route);

        setTitle("Driver Route");

        startLocation = findViewById(R.id.btnstartlocation);
        endLocation = findViewById(R.id.btnendlocation);
        startTime = findViewById(R.id.btnstarttime);
        endTime = findViewById(R.id.btnendtime);
        btnDone = findViewById(R.id.btnDone);

        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        SelectedTime st = new SelectedTime();
        st.setSelectedTime(null);

        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRouteActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,1);
            }
        });

        endLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRouteActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,2);
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRouteActivity.this, ChooseTimeActivity.class);
                startActivityForResult(intent,100);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRouteActivity.this,ChooseTimeActivity.class);
                startActivityForResult(intent,101);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startPlace==null || endPlace==null || startTimeString==null || endTimeString==null){
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs Can not be Empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else{
                    Intent intent = new Intent(DriverRouteActivity.this,DriverRouteActivity2.class);
                    DriverRouteDetails dr = new DriverRouteDetails();
                    dr.setStartPlace(startPlace);
                    dr.setEndPlace(endPlace);
                    dr.setStartTime(startTimeString);
                    dr.setEndTime(endTimeString);
                    startActivity(intent);
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            SelectedPlace s = new SelectedPlace();
            startPlace = s.getPlace();
            if(startPlace!=null){
                startLocation.setText(startPlace.getName().toString());
            }
        }
        if(requestCode==2){
            SelectedPlace s = new SelectedPlace();
            endPlace = s.getPlace();
            if(endLocation!=null){
                endLocation.setText(endPlace.getName().toString());
            }
        }
        if(requestCode==100){
            SelectedTime s = new SelectedTime();
            startTimeString = s.getSelectedTime();
            if(startTime!=null){
                startTime.setText(startTimeString);
            }
        }
        if(requestCode==101){
            SelectedTime s = new SelectedTime();
            endTimeString = s.getSelectedTime();
            if(endTime!=null){
                endTime.setText(endTimeString);
            }
        }
    }
}
