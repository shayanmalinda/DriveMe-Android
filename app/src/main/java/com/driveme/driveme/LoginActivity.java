package com.driveme.driveme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button signup;
    private Button login;
    private CheckBox remembermeCheckbox;

    private EditText etemail;
    private EditText etpass;
    private TextView txtRememberme;

    FirebaseFirestore db;

    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    SignInButton signinbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.btnLogin);
        remembermeCheckbox = findViewById(R.id.remembermeCheckbox);
        etemail = findViewById(R.id.loginemail);
        etpass = findViewById(R.id.loginpassword);
        txtRememberme = findViewById(R.id.txtRememberme);

        txtRememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remembermeCheckbox.performClick();
            }
        });


        RememberMe rm = new RememberMe();

        if(rm.isPassengerCheckbox()){
            remembermeCheckbox.setChecked(true);
            etemail.setText(rm.getPassengerEmail());
            etpass.setText(rm.getPassengerPassword());
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                final String email = etemail.getText().toString();
                final String password = etpass.getText().toString();

                md5 md5 = new md5();
//                byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
                final String hashedPassword = md5.md5Hash(password);
                Log.d("passworddd",hashedPassword);


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

                    CurrentUser usr = new CurrentUser();
                    usr.setParentId(null);
                    usr.setPassengerId(null);
                    usr.setDriverId(null);
                    usr.setOwnerID(null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.layout_loading_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();



                    db = FirebaseFirestore.getInstance();
                    db.collection("userCredentials").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()) {
                                Boolean validCredentials = false;
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot d: list){
                                    Map<String, Object> details = d.getData();
                                    String dbemail = details.get("email").toString();
                                    String dbpass = details.get("password").toString();


                                    if(email.equals(dbemail) && hashedPassword.equals(dbpass)){
                                        CurrentUser usr = new CurrentUser();
                                        usr.setUserCredentialId(d.getId());
                                        if(details.containsKey("driverId")) {
                                            usr.setDriverId(d.get("driverId").toString());
//                                            Log.d("userId driver= ",d.get("driverId").toString());
                                        }
                                        if(details.containsKey("passengerId")) {
                                            usr.setPassengerId(d.get("passengerId").toString());
//                                            Log.d("userId passenger= ",d.get("passengerId").toString());
                                        }
                                        if(details.containsKey("parentId")) {
                                            usr.setParentId(d.get("parentId").toString());
//                                            Log.d("userId parent= ",d.get("parentId").toString());
                                        }
                                        if(details.containsKey("ownerId")) {
                                            usr.setOwnerID(d.get("ownerId").toString());
//                                            Log.d("userId owner= ",d.get("ownerId").toString());
                                        }


//                                    Log.d("userID= ",d.getId());
                                        validCredentials = true;
                                        break;
                                    }
                                }
                                if(validCredentials){
                                    CurrentUser usr = new CurrentUser();
                                    String driverId = usr.getDriverId();
                                    String passengerId = usr.getPassengerId();
                                    String parentId = usr.getParentId();
                                    String ownerId = usr.getOwnerID();

                                    if(passengerId!=null){
//                                    Log.d("userId passenger= ",passengerId);
                                        Intent intent = new Intent(LoginActivity.this,PassengerHomePage.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",hashedPassword);
                                        startActivity(intent);

                                    }
                                    else if(parentId!=null){
//                                    Log.d("userId parent= ",parentId);
                                        Intent intent = new Intent(LoginActivity.this,ParentHomePage.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",hashedPassword);
                                        startActivity(intent);
                                    }
                                    else if(driverId!=null){
//                                    Log.d("userId driver= ",driverId);
                                        Intent intent = new Intent(LoginActivity.this,DriverHomePage.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",hashedPassword);
                                        startActivity(intent);

                                    }
                                    else if(ownerId!=null){
//                                    Log.d("userId owner= ",ownerId);
//                                    Intent intent = new Intent(LoginActivity.this,DriverHomeActivity.class);
//                                        intent.putExtra("email",email);
//                                        intent.putExtra("password",password);
//                                    startActivity(intent);

                                    }
                                    else{
                                        Intent intent = new Intent(LoginActivity.this,UserRegister.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",hashedPassword);
                                        startActivity(intent);
                                    }

//                                    finish();
                                    dialog.dismiss();
                                }
                                else{
                                    dialog.dismiss();
                                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Email or Password", Snackbar.LENGTH_LONG);
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

//                if(role!=null){
//                    if(role.equals("passenger")){
//                        if(etemail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty()){
//                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs cannot be Empty", Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Ok", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    snackbar.dismiss();
//                                }
//                            });
//                            snackbar.show();
//                        }
//                        else{
//                            RememberMe rm = new RememberMe();
//                            if(remembermeCheckbox.isChecked()){
//                                rm.setPassengerCheckbox(true);
//                                rm.setPassengerEmail(etemail.getText().toString());
//                                rm.setPassengerPassword(etpass.getText().toString());
//                            }
//                            else{
//                                rm.setPassengerCheckbox(false);
//                            }
//                            authPassenger();
//                        }
//                    }
//                    if(role.equals("parent")){
//                        if(etemail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty()){
//                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs cannot be Empty", Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Ok", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    snackbar.dismiss();
//                                }
//                            });
//                            snackbar.show();
//                        }
//                        else{
//                            RememberMe rm = new RememberMe();
//                            if(remembermeCheckbox.isChecked()){
//                                rm.setParentCheckbox(true);
//                                rm.setParentEmail(etemail.getText().toString());
//                                rm.setParentPassword(etpass.getText().toString());
//                            }
//                            else{
//                                rm.setParentCheckbox(false);
//                            }
//                            authParent();
//                        }
//                    }
//                }


            }
        });

//        auth = FirebaseAuth.getInstance();
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
//
//        signinbtn = findViewById(R.id.mygooglebtn);
//        signinbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signin();
//            }
//        });


    }



}
