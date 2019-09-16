package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PassengerHomeActivity extends AppCompatActivity {

    private CardView myprofile;
    private CardView routesearch;
    private CardView driverlocation;
    private CardView myroute;
    private CardView ratedriver;
    private CardView payment;

    private ImageView imgroutesearch;
    private ImageView imgmyprofile;
    private ImageView imgdriverlocation;
    private ImageView imgmyroute;
    private ImageView imgratedriver;
    private ImageView imgpayment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);

        setTitle("Passenger - Home");

        myprofile = findViewById(R.id.myprofile);
        routesearch = findViewById(R.id.searchroute);
        driverlocation = findViewById(R.id.driverlocation);
        myroute = findViewById(R.id.myroute);
        ratedriver = findViewById(R.id.ratedriver);
        payment = findViewById(R.id.payments);

        imgmyprofile = findViewById(R.id.imgmyprofile);
        imgroutesearch = findViewById(R.id.imgsearch);
        imgdriverlocation = findViewById(R.id.imgdriverlocation);
        imgmyroute = findViewById(R.id.imgmyroute);
        imgratedriver = findViewById(R.id.imgratedriver);
        imgpayment = findViewById(R.id.imgpayment);

        final Animation animation = AnimationUtils.loadAnimation(PassengerHomeActivity.this,R.anim.rotate);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyprofile.startAnimation(animation);
                Intent intent = new Intent(PassengerHomeActivity.this,PassengerProfileActivity.class);
                startActivity(intent);
            }
        });

        routesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgroutesearch.startAnimation(animation);
                Intent intent = new Intent(PassengerHomeActivity.this,PassengerRouteSearchActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(PassengerHomeActivity.this,MapFragmentActivity.class);
//                startActivity(intent);

            }
        });

        driverlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgdriverlocation.startAnimation(animation);
                Intent intent = new Intent(PassengerHomeActivity.this,PassengerMapActivity.class);
                startActivity(intent);
            }
        });

        myroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyroute.startAnimation(animation);
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerHomeActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String passengerId = cu.getCurrentuserID();

                db.document("users/user/passenger/"+passengerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverId = documentSnapshot.getString("driverId");
                        if(driverId.isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Route have been Added", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                        else{
                            Intent intent = new Intent(PassengerHomeActivity.this,PassengerRouteActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });

        ratedriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgratedriver.startAnimation(animation);

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpayment.startAnimation(animation);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgmyprofile = findViewById(R.id.imgmyprofile);
        imgroutesearch = findViewById(R.id.imgsearch);
        imgdriverlocation = findViewById(R.id.imgdriverlocation);
        imgmyroute = findViewById(R.id.imgmyroute);
        imgratedriver = findViewById(R.id.imgratedriver);
        imgpayment = findViewById(R.id.imgpayment);

        imgmyroute.clearAnimation();
        imgmyprofile.clearAnimation();
        imgdriverlocation.clearAnimation();
        imgratedriver.clearAnimation();
        imgpayment.clearAnimation();
        imgroutesearch.clearAnimation();
    }
}
