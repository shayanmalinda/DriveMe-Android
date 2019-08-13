package com.driveme.driveme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentSignup2Activity extends AppCompatActivity {

    private EditText etname;
    private EditText etage;
    private EditText etschool;
    private EditText etschoolphone;
    private FirebaseFirestore db;

    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("DriveMe - Child");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup2);


        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent(); // gets the previously created intent
        final String parentemail = intent.getStringExtra("email");
        final String parentphone= intent.getStringExtra("phone");
        final String parentaddress= intent.getStringExtra("address");
        final String parentpass= intent.getStringExtra("pass");


        etname = findViewById(R.id.childname);
        etage = findViewById(R.id.childage);
        etschool = findViewById(R.id.childschool);
        etschoolphone = findViewById(R.id.childschoolphone);
        signup = findViewById(R.id.parentsignup);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ParentSignup2Activity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();

                String childname = etname.getText().toString();
                String childage = etage.getText().toString();
                String childschool = etschool.getText().toString();
                String childschoolphone = etschoolphone.getText().toString();

                if(childname.isEmpty() || childage.isEmpty() || childschool.isEmpty() || childschoolphone.isEmpty() ){
                    Toast.makeText(ParentSignup2Activity.this, "Inputs are Empty", Toast.LENGTH_SHORT).show();
                }
                else{

                    dialog.show();
                    CollectionReference dbPassenger = db.collection("users/user/parent");
                    Parents passenger = new Parents(parentemail,parentphone,parentaddress,parentpass,childname,childage,childschool,childschoolphone);


                    dbPassenger.add(passenger).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dialog.dismiss();
                            Toast.makeText(ParentSignup2Activity.this, "Parent Registered", Toast.LENGTH_SHORT).show();
                            finish();
                            ParentSignup1Activity.fa.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ParentSignup2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
