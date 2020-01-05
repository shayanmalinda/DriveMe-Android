package com.driveme.driveme;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PassengerHomePage extends AppCompatActivity {


    private CardView myprofile;
    private CardView routesearch;
    private CardView notifications;
    private CardView myroute;
    private CardView mydriver;
    private CardView payment;

    private ImageView imgroutesearch;
    private ImageView imgmyprofile;
    private ImageView imgnotifications;
    private ImageView imgmyroute;
    private ImageView imgmydriver;
    private ImageView imgpayment;

    MenuItem switchToDriver;
    MenuItem switchToParent;
    MenuItem switchToOwner;

    private String driverId = null;
    private String parentId = null;
    private String ownerId = null;

    MenuItem registerAsDriver;
    MenuItem registerAsParent;
    MenuItem registerAsOwner;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Passenger - Home");

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");

        myprofile = findViewById(R.id.myprofile);
        routesearch = findViewById(R.id.searchroute);
        notifications = findViewById(R.id.notifications);
        myroute = findViewById(R.id.myroute);
        mydriver = findViewById(R.id.mydriver);
        payment = findViewById(R.id.payments);

        imgmyprofile = findViewById(R.id.imgmyprofile);
        imgroutesearch = findViewById(R.id.imgsearch);
        imgnotifications = findViewById(R.id.imgnotifications);
        imgmyroute = findViewById(R.id.imgmyroute);
        imgmydriver = findViewById(R.id.imgmydriver);
        imgpayment = findViewById(R.id.imgpayment);


        CurrentUser usr = new CurrentUser();
        driverId = usr.getDriverId();
        parentId = usr.getParentId();
        ownerId = usr.getOwnerID();

        final Animation animation = AnimationUtils.loadAnimation(PassengerHomePage.this,R.anim.rotate);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyprofile.startAnimation(animation);
                Intent intent = new Intent(PassengerHomePage.this,PassengerProfileActivity.class);
                startActivity(intent);
            }
        });

        routesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgroutesearch.startAnimation(animation);
                Intent intent = new Intent(PassengerHomePage.this,PassengerRouteSearchActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(PassengerHomeActivity.this,MapFragmentActivity.class);
//                startActivity(intent);

            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgnotifications.startAnimation(animation);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String userId = cu.getPassengerId();
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerHomePage.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();


                db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.contains("driverId") || documentSnapshot.getString("driverId").isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Any Notifications", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                            imgmydriver = findViewById(R.id.imgmydriver);
                            imgmydriver.clearAnimation();
                        }
                        else{

                            Intent intent = new Intent(PassengerHomePage.this,PassengerNotificationsActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });

            }
        });

        myroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyroute.startAnimation(animation);
                Intent intent = new Intent(PassengerHomePage.this,PassengerMyRouteActivity.class);
                startActivity(intent);
            }
        });

        mydriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmydriver.startAnimation(animation);
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String userId = cu.getPassengerId();
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerHomePage.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();


                db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.contains("driverId") || documentSnapshot.getString("driverId").isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Not Registered with a Route", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                            imgmydriver = findViewById(R.id.imgmydriver);
                            imgmydriver.clearAnimation();
                        }
                        else{
                            Intent intent = new Intent(PassengerHomePage.this,PassengerDriverActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpayment.startAnimation(animation);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String userId = cu.getPassengerId();
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerHomePage.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();


                db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.contains("driverId") || documentSnapshot.getString("driverId").isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Not Registered with a Route", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                            imgmydriver = findViewById(R.id.imgmydriver);
                            imgmydriver.clearAnimation();
                        }
                        else{
                            Intent intent = new Intent(PassengerHomePage.this,PassengerPaymentViewActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        imgmyprofile = findViewById(R.id.imgmyprofile);
        imgroutesearch = findViewById(R.id.imgsearch);
        imgnotifications = findViewById(R.id.imgnotifications);
        imgmyroute = findViewById(R.id.imgmyroute);
        imgmydriver = findViewById(R.id.imgmydriver);
        imgpayment = findViewById(R.id.imgpayment);

        imgmyroute.clearAnimation();
        imgmyprofile.clearAnimation();
        imgnotifications.clearAnimation();
        imgmydriver.clearAnimation();
        imgpayment.clearAnimation();
        imgroutesearch.clearAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(driverId!=null){
            switchToDriver = menu.findItem(R.id.switchToDriver);
            switchToDriver.setVisible(true);
        }
        else{
            registerAsDriver = menu.findItem(R.id.registerAsDriver);
            registerAsDriver.setVisible(true);
        }
        if(parentId!=null){
            switchToParent = menu.findItem(R.id.switchToParent);
            switchToParent.setVisible(true);
        }
        else{
            registerAsParent = menu.findItem(R.id.registerAsParent);
            registerAsParent.setVisible(true);
        }
        if(ownerId!=null){
            switchToOwner = menu.findItem(R.id.switchToOwner);
            switchToOwner.setVisible(true);
        }
        else{
            registerAsOwner = menu.findItem(R.id.registerAsOwner);
            registerAsOwner.setVisible(true);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switchToDriver) {
            finish();
            Intent intent = new Intent(PassengerHomePage.this,DriverHomePage.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
            return true;
        }
        if (id == R.id.switchToOwner) {
//            Intent intent = new Intent(PassengerHomePage.this,Own.class);
//            intent.putExtra("email",email);
//            intent.putExtra("password",password);
//            startActivity(intent);
//            return true;
        }
        if (id == R.id.switchToParent) {
            finish();
            Intent intent = new Intent(PassengerHomePage.this,ParentHomePage.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
            return true;
        }
        if (id == R.id.registerAsDriver) {
            Intent intent = new Intent(PassengerHomePage.this,VisitDrivMe.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsParent) {
            Intent intent = new Intent(PassengerHomePage.this,PassengerSignupActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsOwner) {
            Intent intent = new Intent(PassengerHomePage.this,VisitDrivMe.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.logout) {
            finishAffinity();
            Intent intent = new Intent(PassengerHomePage.this,LoginActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
