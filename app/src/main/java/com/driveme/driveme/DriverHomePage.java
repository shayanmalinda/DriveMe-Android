package com.driveme.driveme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class DriverHomePage extends AppCompatActivity {

    MenuItem switchToParent;
    MenuItem switchToPassenger;
    MenuItem switchToOwner;

    MenuItem registerAsParent;
    MenuItem registerAsPassenger;
    MenuItem registerAsOwner;

    private String parentId=null;
    private String passengerId=null ;
    private String ownerId=null;

    private CardView driverroute;
    private CardView livelocation;
    private CardView passengerlist;
    private CardView notifications;
    private CardView payments;
    private CardView myprofile;


    private ImageView imgroute;
    private ImageView imgsharelocation;
    private ImageView imgpassengerlist;
    private ImageView imgnotifications;
    private ImageView imgpayments;
    private ImageView imgmyprofile;


    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Driver - Home");
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");

        CurrentUser usr = new CurrentUser();
        parentId = usr.getParentId();
        passengerId = usr.getPassengerId();
        ownerId = usr.getOwnerID();


        driverroute = findViewById(R.id.driverroute);
        livelocation = findViewById(R.id.livelocation);
        passengerlist = findViewById(R.id.passengerlist);
        notifications = findViewById(R.id.notifications);
        payments = findViewById(R.id.payments);
        myprofile = findViewById(R.id.myprofile);

        imgroute = findViewById(R.id.imgroute);
        imgsharelocation = findViewById(R.id.imgsharelocation);
        imgpassengerlist = findViewById(R.id.imgpassengerlist);
        imgnotifications = findViewById(R.id.imgnotifications);
        imgpayments = findViewById(R.id.imgpayments);
        imgmyprofile = findViewById(R.id.imgmyprofile);

        final Animation animation = AnimationUtils.loadAnimation(DriverHomePage.this,R.anim.rotate);

        driverroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgroute.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(DriverHomePage.this,DriverRouteActivity.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });

        livelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgsharelocation.startAnimation(animation);
                Intent intent = new Intent(DriverHomePage.this,DriverMapActivity.class);
                startActivity(intent);
            }
        });

        passengerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpassengerlist.startAnimation(animation);
                Intent intent = new Intent(DriverHomePage.this,DriverUserListActivity.class);
                startActivity(intent);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgnotifications.startAnimation(animation);
                Intent intent = new Intent(DriverHomePage.this,DriverNotificationsActivity.class);
                startActivity(intent);
            }
        });

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgpayments.startAnimation(animation);
                Intent intent = new Intent(DriverHomePage.this,DriverPaymentsSelectUserActivity.class);
                startActivity(intent);

            }
        });

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgmyprofile.startAnimation(animation);
                Intent intent = new Intent(DriverHomePage.this,DriverProfileActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        imgroute = findViewById(R.id.imgroute);
        imgsharelocation = findViewById(R.id.imgsharelocation);
        imgpassengerlist = findViewById(R.id.imgpassengerlist);
        imgnotifications = findViewById(R.id.imgnotifications);
        imgpayments = findViewById(R.id.imgpayments);
        imgmyprofile = findViewById(R.id.imgmyprofile);

        imgroute.clearAnimation();
        imgsharelocation.clearAnimation();
        imgpassengerlist.clearAnimation();
        imgnotifications.clearAnimation();
        imgpayments.clearAnimation();
        imgmyprofile.clearAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(parentId!=null){
            switchToParent = menu.findItem(R.id.switchToParent);
            switchToParent.setVisible(true);
        }
        else{
            registerAsParent = menu.findItem(R.id.registerAsParent);
            registerAsParent.setVisible(true);
        }

        if(passengerId!=null){
            switchToPassenger = menu.findItem(R.id.switchToPassenger);
            switchToPassenger.setVisible(true);
        }
        else{
            registerAsPassenger = menu.findItem(R.id.registerAsPassenger);
            registerAsPassenger.setVisible(true);
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
        if (id == R.id.switchToParent) {
            finish();
            Intent intent = new Intent(DriverHomePage.this,ParentHomePage.class);
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
        if (id == R.id.switchToPassenger) {
            finish();
            Intent intent = new Intent(DriverHomePage.this,PassengerHomePage.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
            return true;
        }
        if (id == R.id.registerAsParent) {
            Intent intent = new Intent(DriverHomePage.this,ParentSignup1Activity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsPassenger) {
            Intent intent = new Intent(DriverHomePage.this,PassengerSignupActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsOwner) {
            Intent intent = new Intent(DriverHomePage.this,VisitDrivMe.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.logout) {
            finishAffinity();
            Intent intent = new Intent(DriverHomePage.this,LoginActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


