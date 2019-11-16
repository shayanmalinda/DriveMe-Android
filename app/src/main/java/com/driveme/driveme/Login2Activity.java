package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Login2Activity extends AppCompatActivity {

    private Button login;

    private EditText etemail;
    private EditText etpass;
    private CheckBox remembermeCheckbox;
    private TextView txtRememberme;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        setTitle("DriveMe - Driver");

        login = findViewById(R.id.btnLogin);
        db = FirebaseFirestore.getInstance();
        remembermeCheckbox = findViewById(R.id.remembermeCheckbox);
        txtRememberme = findViewById(R.id.txtRememberme);
        etemail = findViewById(R.id.loginemail);
        etpass = findViewById(R.id.loginpassword);

        txtRememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remembermeCheckbox.performClick();
            }
        });

        RememberMe rm = new RememberMe();
        if(rm.isDriverCheckbox()){
            remembermeCheckbox.setChecked(true);
            etemail.setText(rm.getDriverEmail());
            etpass.setText(rm.getDriverPassword());
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RememberMe rm = new RememberMe();
                if(remembermeCheckbox.isChecked()){
                    rm.setDriverCheckbox(true);
                    rm.setDriverEmail(etemail.getText().toString());
                    rm.setDriverPassword(etpass.getText().toString());
                }
                else{
                    rm.setDriverCheckbox(false);
                }
                authDriver();
            }
        });
    }

    private void authDriver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login2Activity.this);
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
        db.collection("users/user/driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Boolean validCredentials = false;
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        Map<String, Object> details = d.getData();
                        try{

                            String dbemail = details.get("email").toString();
                            String dbpass = details.get("password").toString();

                            if(email.equals(dbemail) && password.equals(dbpass)){
                                CurrentUser usr = new CurrentUser();
                                usr.setUserCredentialId(d.getId());
                                validCredentials = true;
                                break;
                            }
                        }
                        catch(Exception e){

                        }
                    }
                    if(validCredentials){
                        Intent intent = new Intent(Login2Activity.this,DriverHomeActivity.class);
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
}
