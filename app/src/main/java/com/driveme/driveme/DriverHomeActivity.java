package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class DriverHomeActivity extends AppCompatActivity {

    private CardView driverroute;
    private CardView livelocation;
    private CardView passengerlist;
    private CardView ratepassengers;
    private CardView payments;
    private CardView myprofile;


    private ImageView imgroute;
    private ImageView imgsharelocation;
    private ImageView imgpassengerlist;
    private ImageView imgratepassengers;
    private ImageView imgpayments;
    private ImageView imgmyprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        setTitle("Driver - Home");


        driverroute = findViewById(R.id.driverroute);
        livelocation = findViewById(R.id.livelocation);
        passengerlist = findViewById(R.id.passengerlist);
        ratepassengers = findViewById(R.id.ratepassengers);
        payments = findViewById(R.id.payments);
        myprofile = findViewById(R.id.myprofile);

        imgroute = findViewById(R.id.imgroute);
        imgsharelocation = findViewById(R.id.imgsharelocation);
        imgpassengerlist = findViewById(R.id.imgpassengerlist);
        imgratepassengers = findViewById(R.id.imgratepassenger);
        imgpayments = findViewById(R.id.imgpayments);
        imgmyprofile = findViewById(R.id.imgmyprofile);

        final Animation animation = AnimationUtils.loadAnimation(DriverHomeActivity.this,R.anim.rotate);

        driverroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgroute.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(DriverHomeActivity.this,DriverRouteActivity.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });

        livelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgsharelocation.startAnimation(animation);
                Intent intent = new Intent(DriverHomeActivity.this,DriverMapActivity.class);
                startActivity(intent);
            }
        });

        passengerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpassengerlist.startAnimation(animation);
                Intent intent = new Intent(DriverHomeActivity.this,DriverPassengerListActivity.class);
                startActivity(intent);
            }
        });

        ratepassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgratepassengers.startAnimation(animation);

            }
        });

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpayments.startAnimation(animation);

            }
        });

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyprofile.startAnimation(animation);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        imgroute = findViewById(R.id.imgroute);
        imgsharelocation = findViewById(R.id.imgsharelocation);
        imgpassengerlist = findViewById(R.id.imgpassengerlist);
        imgratepassengers = findViewById(R.id.imgratepassenger);
        imgpayments = findViewById(R.id.imgpayments);
        imgmyprofile = findViewById(R.id.imgmyprofile);

        imgroute.clearAnimation();
        imgsharelocation.clearAnimation();
        imgpassengerlist.clearAnimation();
        imgratepassengers.clearAnimation();
        imgpayments.clearAnimation();
        imgmyprofile.clearAnimation();
    }
}
