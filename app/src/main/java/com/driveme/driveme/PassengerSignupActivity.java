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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PassengerSignupActivity extends AppCompatActivity {

    private EditText etemail;
    private EditText etphone;
    private EditText etaddress;
    private EditText etpass1;
    private EditText etpass2;

    private Button signup;

    private FirebaseFirestore db;

    private String email;
    private String phone;
    private String address;
    private String pass1;
    private String pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_signup);

        db = FirebaseFirestore.getInstance();

        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etpass1 = findViewById(R.id.txtpass1);
        etpass2 = findViewById(R.id.txtpass2);
        signup = findViewById(R.id.btnpassengersignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerSignupActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();


                email = etemail.getText().toString();
                phone = etphone.getText().toString();
                address = etaddress.getText().toString();
                pass1 = etpass1.getText().toString();
                pass2 = etpass2.getText().toString();

                if(email.isEmpty() || phone.isEmpty() || address.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){
                    Toast.makeText(PassengerSignupActivity.this, "Inputs are Empty", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(pass1.equals(pass2)){
                        dialog.show();
                        CollectionReference dbPassenger = db.collection("users/user/passenger");
                        Passengers passenger = new Passengers(email,address,phone,pass1);


                        dbPassenger.add(passenger).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                dialog.dismiss();
                                Toast.makeText(PassengerSignupActivity.this, "Passenger Registered", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PassengerSignupActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }
}
