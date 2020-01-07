package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ParentProfileEditActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText txtParentEmail;
    private EditText txtParentPhone;
    private EditText txtParentAddress;
    private EditText txtChildName;
    private EditText txtChildAge;
    private EditText txtSchoolName;
    private EditText txtSchoolPhone;

    private Button saveDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile_edit);
        final String userId = getIntent().getStringExtra("userId");
        String parentEmail = getIntent().getStringExtra("parentEmail");
        String parentPhone = getIntent().getStringExtra("parentPhone");
        String parentAddress = getIntent().getStringExtra("parentAddress");
        String childAge = getIntent().getStringExtra("childAge");
        String childName = getIntent().getStringExtra("childName");
        String schoolName = getIntent().getStringExtra("schoolName");
        String schoolPhone = getIntent().getStringExtra("schoolPhone");
        setTitle("Edit Profile");

        db = FirebaseFirestore.getInstance();
        txtParentEmail = findViewById(R.id.txtparentemail);
        txtParentAddress = findViewById(R.id.txtparentaddress);
        txtParentPhone = findViewById(R.id.txtparentphone);
        txtChildAge = findViewById(R.id.txtchilage);
        txtChildName = findViewById(R.id.txtchildname);
        txtSchoolName = findViewById(R.id.txtschoolname);
        txtSchoolPhone = findViewById(R.id.txtschoolphone);
        saveDetails = findViewById(R.id.btnsavedetails);

        txtParentEmail.setText(parentEmail);
        txtParentPhone.setText(parentPhone);
        txtParentAddress.setText(parentAddress);
        txtChildAge.setText(childAge);
        txtChildName.setText(childName);
        txtSchoolName.setText(schoolName);
        txtSchoolPhone.setText(schoolPhone);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentProfileEditActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("parentEmail",txtParentEmail.getText().toString());
                userMap.put("parentAddress",txtParentAddress.getText().toString());
                userMap.put("parentPhone",txtParentPhone.getText().toString());
                userMap.put("childName",txtChildName.getText().toString());
                userMap.put("childAge",txtChildAge.getText().toString());
                userMap.put("childSchool",txtSchoolName.getText().toString());
                userMap.put("childSchoolPhone",txtSchoolPhone.getText().toString());
                db.document("users/user/parent/"+userId).update(userMap);
                Intent intent2 = new Intent(ParentProfileEditActivity.this,ParentProfileActivity.class);
                finish();
                startActivity(intent2);
                dialog.dismiss();
                Toast.makeText(ParentProfileEditActivity.this, "Change Success", Toast.LENGTH_SHORT).show();
//                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes Success", Snackbar.LENGTH_LONG);
//                snackbar.setAction("Ok", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snackbar.dismiss();
//                    }
//                });
//                snackbar.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final String userId = getIntent().getStringExtra("userId");
        String parentEmail = getIntent().getStringExtra("parentEmail");
        String parentPhone = getIntent().getStringExtra("parentPhone");
        String parentAddress = getIntent().getStringExtra("parentAddress");
        String childAge = getIntent().getStringExtra("childAge");
        String childName = getIntent().getStringExtra("childName");
        String schoolName = getIntent().getStringExtra("schoolName");
        String schoolPhone = getIntent().getStringExtra("schoolPhone");
        setTitle("Edit Profile");

        db = FirebaseFirestore.getInstance();
        txtParentEmail = findViewById(R.id.txtparentemail);
        txtParentAddress = findViewById(R.id.txtparentaddress);
        txtParentPhone = findViewById(R.id.txtparentphone);
        txtChildAge = findViewById(R.id.txtchilage);
        txtChildName = findViewById(R.id.txtchildname);
        txtSchoolName = findViewById(R.id.txtschoolname);
        txtSchoolPhone = findViewById(R.id.txtschoolphone);
        saveDetails = findViewById(R.id.btnsavedetails);

        txtParentEmail.setText(parentEmail);
        txtParentPhone.setText(parentPhone);
        txtParentAddress.setText(parentAddress);
        txtChildAge.setText(childAge);
        txtChildName.setText(childName);
        txtSchoolName.setText(schoolName);
        txtSchoolPhone.setText(schoolPhone);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentProfileEditActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("parentEmail",txtParentEmail.getText().toString());
                userMap.put("parentAddress",txtParentAddress.getText().toString());
                userMap.put("parentPhone",txtParentPhone.getText().toString());
                userMap.put("childName",txtChildName.getText().toString());
                userMap.put("childAge",txtChildAge.getText().toString());
                userMap.put("childSchool",txtSchoolName.getText().toString());
                userMap.put("childSchoolPhone",txtSchoolPhone.getText().toString());
                db.document("users/user/parent/"+userId).update(userMap);
                Intent intent2 = new Intent(ParentProfileEditActivity.this,ParentProfileActivity.class);
                finish();
                startActivity(intent2);
                dialog.dismiss();
                Toast.makeText(ParentProfileEditActivity.this, "Change Success", Toast.LENGTH_SHORT).show();

//                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes Success", Snackbar.LENGTH_LONG);
//                snackbar.setAction("Ok", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snackbar.dismiss();
//                    }
//                });
//                snackbar.show();
            }
        });
    }
}
