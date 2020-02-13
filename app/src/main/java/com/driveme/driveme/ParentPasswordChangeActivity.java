package com.driveme.driveme;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ParentPasswordChangeActivity extends AppCompatActivity {
    private Button btndone;
    private EditText etold;
    private EditText etnew1;
    private EditText etnew2;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_password_change);

        setTitle("Change Password");
        btndone = findViewById(R.id.btndone);
        etold = findViewById(R.id.txtoldpass);
        etnew1 = findViewById(R.id.txtnewpass1);
        etnew2 = findViewById(R.id.txtnewpass2);
        db = FirebaseFirestore.getInstance();

        final DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;


        getWindow().setLayout((int)(width*0.8),(int)(height*0.5));

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String oldpassword = etold.getText().toString();
                final String newpass1 = etnew1.getText().toString();
                final String newpass2 = etnew2.getText().toString();

                md5 md5 = new md5();
                final String oldhashpassword = md5.md5Hash(oldpassword);
                final String newhashpassword = md5.md5Hash(newpass1);

                if(oldpassword.isEmpty() || newpass1.isEmpty() || newpass2.isEmpty()){
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
                    if(newpass1.equals(newpass2)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ParentPasswordChangeActivity.this);
                        builder.setCancelable(false); // if you want user to wait for some process to finish,
                        builder.setView(R.layout.layout_loading_dialog);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        CurrentUser c = new CurrentUser();
                        final String userId = CurrentUser.getuUserCredentialId();

//                        BigInteger passEncrypt = null;
//                        try{
//                            passEncrypt = new BigInteger(1, md5.encryptMD5(oldpassword.getBytes()));
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        final String oldEncryptpass = passEncrypt.toString();

                        db.document("userCredentials/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String dbpass = documentSnapshot.get("password").toString();
                                if(oldhashpassword.equals(dbpass)){

//                                    BigInteger passEncrypt2 = null;
//                                    try{
//                                        passEncrypt2 = new BigInteger(1, md5.encryptMD5(newpass1.getBytes()));
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                    }
//                                    final String newEncryptpass = passEncrypt2.toString();

                                    Map<String,Object> userMap = new HashMap<>();
//                                    userMap.put("password",newEncryptpass);
                                    userMap.put("password",newhashpassword);

                                    db.document("userCredentials/"+userId).update(userMap);
                                    finish();
                                    dialog.dismiss();
                                    Toast.makeText(ParentPasswordChangeActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
//                                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Password Changed", Snackbar.LENGTH_LONG);
//                                    snackbar.setAction("Ok", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            snackbar.dismiss();
//                                        }
//                                    });
//                                    snackbar.show();
                                }
                                else{
                                    dialog.dismiss();
                                    Toast.makeText(ParentPasswordChangeActivity.this, "Invalid Old Password", Toast.LENGTH_SHORT).show();
//                                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Old Password", Snackbar.LENGTH_LONG);
//                                    snackbar.setAction("Ok", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            snackbar.dismiss();
//                                        }
//                                    });
//                                    snackbar.show();
                                }
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
