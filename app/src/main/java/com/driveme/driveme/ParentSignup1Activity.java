package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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

import java.math.BigInteger;

public class ParentSignup1Activity extends AppCompatActivity {

    private EditText etemail;
    private EditText etphone;
    private EditText etpass1;
    private EditText etpass2;
    private EditText etaddress;

    private Button btnnext;
    public static Activity fa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup1);

        fa = this;

        setTitle("DriveMe - Parent");


        etemail = findViewById(R.id.parentemail);
        etphone = findViewById(R.id.parentphone);
        etpass1 = findViewById(R.id.parentpass1);
        etpass2 = findViewById(R.id.parentpass2);
        etaddress = findViewById(R.id.parentaddress);
        btnnext = findViewById(R.id.parentnextbtn);




        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etemail.getText().toString();
                String phone = etphone.getText().toString();
                String address = etaddress.getText().toString();
                String pass1 = etpass1.getText().toString();
                String pass2 = etpass2.getText().toString();

                if(email.isEmpty() || phone.isEmpty() || address.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs are Empty", Snackbar.LENGTH_LONG);
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

                        BigInteger passEncrypt = null;

                        try{
                            passEncrypt = new BigInteger(1, md5.encryptMD5(pass1.getBytes()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(ParentSignup1Activity.this,ParentSignup2Activity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("phone",phone);
                        intent.putExtra("address",address);
                        intent.putExtra("pass",passEncrypt.toString());

                        startActivity(intent);


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
