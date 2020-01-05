package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
//    private TextView txtParentName;
    private TextView txtParentEmail;
    private TextView txtParentPhone;
    private TextView txtParentAddress;
    private TextView txtChildName;
    private TextView txtChildAge;
    private TextView txtSchoolName;
    private TextView txtSchoolPhone;

    private Button btnedit;
    private Button btnchangepass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        setTitle("My Profile");
        db = FirebaseFirestore.getInstance();
//        txtParentName = findViewById(R.id.txtparentname);
        txtParentEmail = findViewById(R.id.txtparentemail);
        txtParentAddress = findViewById(R.id.txtparentaddress);
        txtParentPhone = findViewById(R.id.txtparentphone);
        txtChildAge = findViewById(R.id.txtchilage);
        txtChildName = findViewById(R.id.txtchildname);
        txtSchoolName = findViewById(R.id.txtschoolname);
        txtSchoolPhone = findViewById(R.id.txtschoolphone);

            btnedit = findViewById(R.id.btneditparentdetails);
        btnchangepass = findViewById(R.id.btnchangepassword);

        getParentDetails();

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                CurrentUser usr = new CurrentUser();
//                String userID = usr.getParentId();
//                Intent intent = new Intent(ParentProfileActivity.this, PassengerProfileEditActivity.class);
//                intent.putExtra("userId",userID);
//                intent.putExtra("email",etemail.getText().toString());
//                intent.putExtra("phone",etphone.getText().toString());
//                intent.putExtra("address",etaddress.getText().toString());
//                intent.putExtra("name",etname.getText().toString());
//                startActivity(intent);

            }
        });

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PassengerProfileActivity.this,PassengerPasswordChangeActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();setTitle("My Profile");

        db = FirebaseFirestore.getInstance();
//        txtParentName = findViewById(R.id.txtparentname);
        txtParentEmail = findViewById(R.id.txtparentemail);
        txtParentAddress = findViewById(R.id.txtparentaddress);
        txtParentPhone = findViewById(R.id.txtparentphone);
        txtChildAge = findViewById(R.id.txtchilage);
        txtChildName = findViewById(R.id.txtchildname);
        txtSchoolName = findViewById(R.id.txtschoolname);
        txtSchoolPhone = findViewById(R.id.txtschoolphone);

        btnedit = findViewById(R.id.btneditparentdetails);
        btnchangepass = findViewById(R.id.btnchangepassword);

        getParentDetails();

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                CurrentUser usr = new CurrentUser();
//                String userID = usr.getPassengerId();
//                Intent intent = new Intent(PassengerProfileActivity.this, PassengerProfileEditActivity.class);
//                intent.putExtra("userId",userID);
//                intent.putExtra("email",etemail.getText().toString());
//                intent.putExtra("phone",etphone.getText().toString());
//                intent.putExtra("address",etaddress.getText().toString());
//                intent.putExtra("name",etname.getText().toString());
//                startActivity(intent);

            }
        });

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PassengerProfileActivity.this,PassengerPasswordChangeActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void getParentDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ParentProfileActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        CurrentUser usr = new CurrentUser();
        String userID = usr.getParentId();

        db.document("users/user/parent/"+userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
//                    txtParentName.setText(documentSnapshot.get("name").toString());
                    txtParentEmail.setText(documentSnapshot.get("parentEmail").toString());
                    txtParentAddress.setText(documentSnapshot.get("parentAddress").toString());
                    txtParentPhone.setText(documentSnapshot.get("parentPhone").toString());
                    txtChildAge.setText(documentSnapshot.get("childAge").toString());
                    txtChildName.setText(documentSnapshot.get("childName").toString());
                    txtSchoolName.setText(documentSnapshot.get("childSchool").toString());
                    txtSchoolPhone.setText(documentSnapshot.get("childSchoolPhone").toString());

                }
                dialog.dismiss();
            }
        });

    }


}
