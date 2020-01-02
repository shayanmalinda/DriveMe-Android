package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView etemail;
    private TextView etphone;
    private TextView etaddress;
    private TextView etname;
    private TextView etlicense;
    private TextView etnic;
    private ImageView driverImage;

    private Button btnchangepass;
    private Button btnviewvehicledetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        setTitle("My Profile");

        db = FirebaseFirestore.getInstance();
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);
        etlicense = findViewById(R.id.txtLicense);
        etnic = findViewById(R.id.txtNIC);
        driverImage = findViewById(R.id.driverImage);

        btnchangepass = findViewById(R.id.btnchangepassword);
        btnviewvehicledetails = findViewById(R.id.btnviewvehicledetails);

        getDriverDetails();

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DriverProfileActivity.this,DriverPasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        btnviewvehicledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverProfileActivity.this,DriverViewVehicleDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


//        driverImage.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/driveme-9bd2a.appspot.com/o/driverImages%2Fdriveravatar2.png?alt=media&token=1392605b-0c26-461c-9434-319f4ca00780"));

    }

    private void getDriverDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverProfileActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser usr = new CurrentUser();
        String userID = usr.getDriverId();

        db.document("users/user/driver/"+userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    etname.setText(documentSnapshot.get("name").toString());
                    etemail.setText(documentSnapshot.get("email").toString());
                    etphone.setText(documentSnapshot.get("driverTelephone").toString());
                    etaddress.setText(documentSnapshot.get("driverAddress").toString());
                    etlicense.setText("License Number : "+documentSnapshot.get("driverLicense").toString());
                    etnic.setText("NIC : "+documentSnapshot.get("driverNIC").toString());
                    String imgUrl =  documentSnapshot.get("imgURL").toString();
                    if(imgUrl!=null){
                        Glide.with(getApplicationContext()).load(imgUrl).into(driverImage);
                    }
                }
                dialog.dismiss();
            }
        });

    }
}
