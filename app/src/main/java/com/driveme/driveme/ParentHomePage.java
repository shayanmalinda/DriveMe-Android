package com.driveme.driveme;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ParentHomePage extends AppCompatActivity {

    MenuItem switchToDriver;
    MenuItem switchToPassenger;
    MenuItem switchToOwner;

    private String driverId = null;
    private String passengerId = null ;
    private String ownerId = null;

    MenuItem registerAsDriver;
    MenuItem registerAsPassenger;
    MenuItem registerAsOwner;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Parent - Home");

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");

        CurrentUser usr = new CurrentUser();
        driverId = usr.getDriverId();
        passengerId = usr.getPassengerId();
        ownerId = usr.getOwnerID();

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
        if (id == R.id.switchToDriver) {
            finish();
            Intent intent = new Intent(ParentHomePage.this,DriverHomePage.class);
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
            Intent intent = new Intent(ParentHomePage.this,PassengerHomePage.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
            return true;
        }
        if (id == R.id.registerAsDriver) {
            Intent intent = new Intent(ParentHomePage.this,VisitDrivMe.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsPassenger) {
            Intent intent = new Intent(ParentHomePage.this,PassengerSignupActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);

            return true;
        }
        if (id == R.id.registerAsOwner) {
            Intent intent = new Intent(ParentHomePage.this,VisitDrivMe.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.logout) {
            finishAffinity();
            Intent intent = new Intent(ParentHomePage.this,LoginActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
