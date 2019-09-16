package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PassengerProfileEditActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView etemail;
    private TextView etphone;
    private TextView etaddress;
    private TextView etname;
    private Button savedetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile_edit);
        final String userId = getIntent().getStringExtra("userId");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        String name = getIntent().getStringExtra("name");
        setTitle("Edit Profile");

        db = FirebaseFirestore.getInstance();
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);
        savedetails = findViewById(R.id.btnsavedetails);

        etemail.setText(email);
        etphone.setText(phone);
        etaddress.setText(address);
        etname.setText(name);

        savedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerProfileEditActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("name",etname.getText().toString());
                userMap.put("phone",etphone.getText().toString());
                userMap.put("address",etaddress.getText().toString());
                userMap.put("email",etemail.getText().toString());
                db.document("users/user/passenger/"+userId).update(userMap);
                Intent intent2 = new Intent(PassengerProfileEditActivity.this,PassengerProfileActivity.class);
                finish();
                startActivity(intent2);
                dialog.dismiss();
                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes Success", Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final String userId = getIntent().getStringExtra("userId");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        String name = getIntent().getStringExtra("name");
        setTitle("Edit Profile");

        db = FirebaseFirestore.getInstance();
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);
        savedetails = findViewById(R.id.btnsavedetails);

        etemail.setText(email);
        etphone.setText(phone);
        etaddress.setText(address);
        etname.setText(name);

        savedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerProfileEditActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("name",etname.getText().toString());
                userMap.put("phone",etphone.getText().toString());
                userMap.put("address",etaddress.getText().toString());
                userMap.put("email",etemail.getText().toString());
                db.document("users/user/passenger/"+userId).update(userMap);
                Intent intent2 = new Intent(PassengerProfileEditActivity.this,PassengerProfileActivity.class);
                finish();
//                startActivity(intent2);
                dialog.dismiss();
                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes Success", Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }
}
