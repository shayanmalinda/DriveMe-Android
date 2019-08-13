package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PassengerPasswordChangeActivity extends AppCompatActivity {

    private Button btndone;
    private EditText etold;
    private EditText etnew1;
    private EditText etnew2;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_password_change);

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
                if(oldpassword.isEmpty() || newpass1.isEmpty() || newpass2.isEmpty()){
                    Toast.makeText(PassengerPasswordChangeActivity.this, "Inputs are Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(newpass1.equals(newpass2)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerPasswordChangeActivity.this);
                        builder.setCancelable(false); // if you want user to wait for some process to finish,
                        builder.setView(R.layout.layout_loading_dialog);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        CurrentUser c = new CurrentUser();
                        final String userId = CurrentUser.getCurrentuserID();

                        BigInteger passEncrypt = null;
                        try{
                            passEncrypt = new BigInteger(1, md5.encryptMD5(oldpassword.getBytes()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        final String oldEncryptpass = passEncrypt.toString();

                        db.document("users/user/passenger/"+userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String dbpass = documentSnapshot.get("password").toString();
                                if(oldEncryptpass.equals(dbpass)){

                                    BigInteger passEncrypt2 = null;
                                    try{
                                        passEncrypt2 = new BigInteger(1, md5.encryptMD5(newpass1.getBytes()));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    final String newEncryptpass = passEncrypt2.toString();

                                    Map<String,Object> userMap = new HashMap<>();
                                    userMap.put("password",newEncryptpass);
                                    db.document("users/user/passenger/"+userId).update(userMap);
                                    finish();
                                    dialog.dismiss();
                                    Toast.makeText(PassengerPasswordChangeActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    dialog.dismiss();
                                    Toast.makeText(PassengerPasswordChangeActivity.this, "Invalid Old Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }
                    else{
                        Toast.makeText(PassengerPasswordChangeActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
