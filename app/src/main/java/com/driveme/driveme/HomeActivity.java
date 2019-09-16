package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btnDriver;
    private Button btnPassenger;
    private Button btnParent;
    private Button btnOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDriver = findViewById(R.id.btnDriver);
        btnPassenger = findViewById(R.id.btnPassenger);
        btnParent = findViewById(R.id.btnParent);
        btnOwner = findViewById(R.id.btnOwner);




        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterRole c = new CharacterRole();
                c.setRole("driver");
                Intent intent = new Intent(HomeActivity.this,Login2Activity.class);
                startActivity(intent);
            }
        });

        btnPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterRole c = new CharacterRole();
                c.setRole("passenger");
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterRole c = new CharacterRole();
                c.setRole("parent");
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterRole c = new CharacterRole();
                c.setRole("owner");
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }





}
