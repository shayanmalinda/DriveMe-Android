package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button signup;
    private Button login;

    private EditText etemail;
    private EditText etpass;

    FirebaseFirestore db;

    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    SignInButton signinbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CharacterRole c = new CharacterRole();
        final String role = c.getRole();

        signup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.btnLogin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(role!=null){
                    if(role.equals("passenger")){
                        intent = new Intent(LoginActivity.this, PassengerSignupActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();

                etemail = findViewById(R.id.loginemail);
                etpass = findViewById(R.id.loginpassword);

                if(role!=null){
                    if(role.equals("passenger")){
                        if(etemail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty()){
                            Toast.makeText(LoginActivity.this, "Username or Password is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            authPassenger();
                        }
                    }

                }
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
                        if(email.equals(dbemail) && password.equals(dbpass)){
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
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
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
