package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;

public class PassengerSignupActivity extends AppCompatActivity {

    private EditText etemail;
    private EditText etname;
    private EditText etphone;
    private EditText etaddress;
    private EditText etpass1;
    private EditText etpass2;

    private Button signup;

    private FirebaseFirestore db;

    private String email;
    private String name;
    private String phone;
    private String address;
    private String pass1;
    private String pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_signup);

        setTitle("DriverMe - Passenger");

        db = FirebaseFirestore.getInstance();

        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etpass1 = findViewById(R.id.txtpass1);
        etpass2 = findViewById(R.id.txtpass2);
        etname = findViewById(R.id.txtname);
        signup = findViewById(R.id.btnpassengersignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerSignupActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();


                email = etemail.getText().toString();
                name = etname.getText().toString();
                phone = etphone.getText().toString();
                address = etaddress.getText().toString();
                pass1 = etpass1.getText().toString();
                pass2 = etpass2.getText().toString();

                if(email.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs Are Empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else{

                    if(pass1.equals(pass2)){
                        dialog.show();
                        CollectionReference dbPassenger = db.collection("users/user/passenger");
                        BigInteger passEncrypt = null;

                        try{
                            passEncrypt = new BigInteger(1, md5.encryptMD5(pass1.getBytes()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        Passengers passenger = new Passengers(name,email,address,phone,passEncrypt.toString());
                        Passengers passenger = new Passengers(name,email,address,phone,pass1);


                        dbPassenger.add(passenger).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                dialog.dismiss();
                                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Registration Success", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(PassengerSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Password Mismatched", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }

                }

            }
        });


    }
}
