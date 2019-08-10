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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

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
                Toast.makeText(ParentSignup1Activity.this, "Btn next clicked", Toast.LENGTH_SHORT).show();

                String email = etemail.getText().toString();
                String phone = etphone.getText().toString();
                String address = etaddress.getText().toString();
                String pass1 = etpass1.getText().toString();
                String pass2 = etpass2.getText().toString();

                if(email.isEmpty() || phone.isEmpty() || address.isEmpty() || pass1.isEmpty() || pass2.isEmpty()){
                    Toast.makeText(ParentSignup1Activity.this, "Inputs are Empty", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(pass1.equals(pass2)){
                        Intent intent = new Intent(ParentSignup1Activity.this,ParentSignup2Activity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("phone",phone);
                        intent.putExtra("address",address);
                        intent.putExtra("pass",pass1);

                        startActivity(intent);


                    }
                    else{
                        Toast.makeText(ParentSignup1Activity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

}
