package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class PassengerProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView etemail;
    private TextView etphone;
    private TextView etaddress;
    private TextView etname;
    private TextView etpassword;

    private Button btnedit;
    private Button btnchangepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);
        setTitle("My Profile");

        db = FirebaseFirestore.getInstance();
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);

        btnedit = findViewById(R.id.btneditpassengerdetails);
        btnchangepass = findViewById(R.id.btnchangepassword);

        getpassengerdetails();

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentUser usr = new CurrentUser();
                String userID = usr.getPassengerId();
                Intent intent = new Intent(PassengerProfileActivity.this, PassengerProfileEditActivity.class);
                intent.putExtra("userId",userID);
                intent.putExtra("email",etemail.getText().toString());
                intent.putExtra("phone",etphone.getText().toString());
                intent.putExtra("address",etaddress.getText().toString());
                intent.putExtra("name",etname.getText().toString());
                startActivity(intent);

            }
        });

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerProfileActivity.this,PassengerPasswordChangeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();setTitle("My Profile");

        db = FirebaseFirestore.getInstance();
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);

        btnedit = findViewById(R.id.btneditpassengerdetails);
        btnchangepass = findViewById(R.id.btnchangepassword);

        getpassengerdetails();

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentUser usr = new CurrentUser();
                String userID = usr.getPassengerId();
                Intent intent = new Intent(PassengerProfileActivity.this, PassengerProfileEditActivity.class);
                intent.putExtra("userId",userID);
                intent.putExtra("email",etemail.getText().toString());
                intent.putExtra("phone",etphone.getText().toString());
                intent.putExtra("address",etaddress.getText().toString());
                intent.putExtra("name",etname.getText().toString());
                startActivity(intent);

            }
        });

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerProfileActivity.this,PassengerPasswordChangeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getpassengerdetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerProfileActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser usr = new CurrentUser();
        String userID = usr.getPassengerId();

        db.document("users/user/passenger/"+userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    etname.setText(documentSnapshot.get("name").toString());
                    etemail.setText(documentSnapshot.get("email").toString());
                    etphone.setText(documentSnapshot.get("phone").toString());
                    etaddress.setText(documentSnapshot.get("address").toString());
                }
                dialog.dismiss();
            }
        });

    }


}
