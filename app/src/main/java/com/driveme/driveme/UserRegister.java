package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.data.model.User;

public class UserRegister extends AppCompatActivity {

    private Button btnPassenger;
    private Button btnParent;
    private Button btnDriver;
    private Button btnOwner;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        btnPassenger = findViewById(R.id.btnPassenger);
        btnParent = findViewById(R.id.btnParent);
        btnDriver = findViewById(R.id.btnDriver);
        btnOwner = findViewById(R.id.btnOwner);


        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");

        btnPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegister.this,PassengerSignupActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegister.this,ParentSignup1Activity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);

            }
        });

        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
