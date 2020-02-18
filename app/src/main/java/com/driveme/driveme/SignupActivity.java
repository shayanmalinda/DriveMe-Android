package com.driveme.driveme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText etEmail;
    private EditText etPassword;
    private TextView emailError;
    private TextView passwordError;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.loginemail);
        etPassword = findViewById(R.id.loginpassword);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        boolean isEmailValid = false;

        btnSignup = findViewById(R.id.btnSignup);


        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                final String inputEmail = etEmail.getText().toString().trim();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (inputEmail.matches(emailPattern) && s.length() > 0)
                {
                    emailError.setText("Valid Email Address");
                    emailError.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    emailError.setTextColor(getResources().getColor(R.color.colorAccent));
                    emailError.setText("Please Enter a Valid Email");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String inputPassword = etPassword.getText().toString().trim();
                if(inputPassword.length()>=6){
                    passwordError.setText("Valid Password");
                    passwordError.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else{
                    passwordError.setText("Password should have at least 6 characters");
                    passwordError.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(email.isEmpty()){

                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Email is Empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }

                else if(password.isEmpty()){

                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Password is Empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }

                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.layout_loading_dialog);
                    final AlertDialog dialog = builder.create();


                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (email.matches(emailPattern) && password.length()>=6){
                        dialog.show();

                        md5 md5 = new md5();
                        String hashedPassword = md5.md5Hash(password);

                        db = FirebaseFirestore.getInstance();
                        CollectionReference dbUserCredential = db.collection("userCredentials");
                        Map<String, String> userCredential = new HashMap<>();
                        userCredential.put("email",email);
                        userCredential.put("password",hashedPassword);

                        dbUserCredential.add(userCredential).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                dialog.dismiss();
                                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Signup Success", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    }
                }

            }
        });





    }
}
