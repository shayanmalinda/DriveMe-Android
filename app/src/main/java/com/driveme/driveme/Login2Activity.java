package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        setTitle("DriveMe - Driver");

        login = findViewById(R.id.btnLogin);
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etemail = findViewById(R.id.loginemail);
                etpass = findViewById(R.id.loginpassword);
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
                        String dbemail = details.get("email").toString();
                        String dbpass = details.get("password").toString();
                        if(email.equals(dbemail) && password.equals(dbpass)){
                            CurrentUser usr = new CurrentUser();
                            usr.setCurrentuserID(d.getId());
                            validCredentials = true;
                            break;
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
                        Toast.makeText(Login2Activity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(Login2Activity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login2Activity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
