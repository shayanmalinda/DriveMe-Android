package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PassengerRouteSearchActivity extends AppCompatActivity {

    private TextView startLocation;
    private TextView endLocation;

    private Place startPlace;
    private Place endPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route_search);

        setTitle("Route Search");

        SelectedPlace sp = new SelectedPlace();
        sp.setPlace(null);

        startLocation = findViewById(R.id.btnstartlocation);
        endLocation = findViewById(R.id.btnendlocation);

        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,1);
            }
        });

        endLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerRouteSearchActivity.this,MapFragmentActivity.class);
                startActivityForResult(intent,2);
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
            if(startPlace!=null) {
                endLocation.setText(endPlace.getName().toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
