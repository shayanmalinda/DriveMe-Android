package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
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


                                    if(email.equals(dbemail) && password.equals(dbpass)){
                                        CurrentUser usr = new CurrentUser();
                                        usr.setUserCredentialId(d.getId());
                                        if(details.containsKey("driverId")) {
                                            usr.setDriverId(d.get("driverId").toString());
                                            Log.d("userId driver= ",d.get("driverId").toString());
                                        }
                                        if(details.containsKey("passengerId")) {
                                            usr.setPassengerId(d.get("passengerId").toString());
                                            Log.d("userId passenger= ",d.get("passengerId").toString());
                                        }
                                        if(details.containsKey("parentId")) {
                                            usr.setParentId(d.get("parentId").toString());
                                            Log.d("userId parent= ",d.get("parentId").toString());
                                        }
                                        if(details.containsKey("ownerId")) {
                                            usr.setOwnerID(d.get("ownerId").toString());
                                            Log.d("userId owner= ",d.get("ownerId").toString());
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

                                    if(driverId!=null){
//                                    Log.d("userId driver= ",driverId);
                                        Intent intent = new Intent(LoginActivity.this,DriverHomeActivity.class);
                                        startActivity(intent);

                                    }
                                    else if(passengerId!=null){
//                                    Log.d("userId passenger= ",passengerId);
                                        Intent intent = new Intent(LoginActivity.this,PassengerHomeActivity.class);
                                        startActivity(intent);

                                    }
                                    else if(parentId!=null){
//                                    Log.d("userId parent= ",parentId);
                                        Intent intent = new Intent(LoginActivity.this,ParentHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else if(ownerId!=null){
//                                    Log.d("userId owner= ",ownerId);
//                                    Intent intent = new Intent(LoginActivity.this,DriverHomeActivity.class);
//                                    startActivity(intent);

                                    }
                                    else{
                                        Intent intent = new Intent(LoginActivity.this,UserRegister.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",password);
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


    private void authPassenger(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        final String email = etemail.getText().toString();
        final String password = etpass.getText().toString();
        BigInteger passEncrypt = null;

        try{
            passEncrypt = new BigInteger(1, md5.encryptMD5(password.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }

        final String passEncryptString = passEncrypt.toString();
        db.collection("users/user/passenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Boolean validCredentials = false;
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        Map<String, Object> details = d.getData();
                        String dbemail = details.get("email").toString();
                        String dbpass = details.get("password").toString();
//                        if(email.equals(dbemail) && passEncryptString.equals(dbpass)){
                        if(email.equals(dbemail) && password.equals(dbpass)){
                            CurrentUser usr = new CurrentUser();
                            usr.setUserCredentialId(d.getId());
                            validCredentials = true;
                            break;
                        }
                    }
                    if(validCredentials){
                        Intent intent = new Intent(LoginActivity.this,PassengerHomeActivity.class);
                        finish();
                        dialog.dismiss();
                        startActivity(intent);
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
                else{
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Inputs", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authParent(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        final String email = etemail.getText().toString();
        final String password = etpass.getText().toString();
        BigInteger passEncrypt = null;

        try{
            passEncrypt = new BigInteger(1, md5.encryptMD5(password.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }

        final String passEncryptString = passEncrypt.toString();
        db.collection("users/user/parent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Boolean validCredentials = false;
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        Map<String, Object> details = d.getData();
                        String dbemail = details.get("parentemail").toString();
                        String dbpass = details.get("parentpass").toString();
//                        if(email.equals(dbemail) && passEncryptString.equals(dbpass)){
                        if(email.equals(dbemail) && password.equals(dbpass)){
                            CurrentUser usr = new CurrentUser();
                            usr.setUserCredentialId(d.getId());
                            validCredentials = true;
                            break;
                        }
                    }
                    if(validCredentials){

                        Intent intent = new Intent(LoginActivity.this,ParentHomeActivity.class);
                        finish();
                        dialog.dismiss();
                        startActivity(intent);
                    }
                    else{
                        dialog.dismiss();
                        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Username or Password", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }

                }
                else{
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Inputs", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Inputs", Snackbar.LENGTH_LONG);
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
//    private void signin(){
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent,101);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == 101) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("", "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("", "signInWithCredential:success");
//                            FirebaseUser user = auth.getCurrentUser();
//                            Toast.makeText(LoginActivity.this, "Logged in Success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(LoginActivity.this,DriverHomeActivity.class);
//                            startActivity(intent);
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "Logged in Error", Toast.LENGTH_SHORT).show();
//                            Log.w("", "signInWithCredential:failure", task.getException());
//                        }
//
//                        // ...
//                    }
//                });
//    }
}
