package com.driveme.driveme;

import android.app.Activity;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ParentSignup1Activity extends AppCompatActivity {


    private EditText etParentPhone;
    private EditText etParentAddress;
    private EditText etChildName;
    private EditText etChildAge;
    private EditText etChildSchool;
    private EditText etChildSchoolPhone;
    private FirebaseFirestore db;

    private Button signup;

    private String email;
    private String password;
    private String hashedPassword;
    private String parentPhone;
    private String parentAddress;
    private String childName;
    private String childAge;
    private String childSchool;
    private String childSchoolPhone;

    private Button btnnext;
    public static Activity fa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup1);


        AlertDialog.Builder builder = new AlertDialog.Builder(ParentSignup1Activity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");

        fa = this;

        setTitle("DriveMe - Parent");


        etParentPhone = findViewById(R.id.parentphone);
        etParentAddress = findViewById(R.id.parentaddress);
        etChildName = findViewById(R.id.childname);
        etChildAge = findViewById(R.id.childage);
        etChildSchool = findViewById(R.id.childschool);
        etChildSchoolPhone = findViewById(R.id.childschoolphone);

        signup = findViewById(R.id.parentSignup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parentAddress = etParentAddress.getText().toString();
                parentPhone = etParentPhone.getText().toString();
                childName = etChildName.getText().toString();
                childAge = etChildAge.getText().toString();
                childSchool = etChildSchool.getText().toString();
                childSchoolPhone = etChildSchoolPhone.getText().toString();

                if(parentAddress.isEmpty() || parentPhone.isEmpty() || childName.isEmpty() || childSchoolPhone.isEmpty() || childAge.isEmpty() || childSchool.isEmpty()){
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Inputs are Empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else{
                    dialog.show();

                    CollectionReference dbParent = db.collection("users/user/parent");
                    BigInteger passEncrypt = null;




                    md5 md5 = new md5();
                    hashedPassword = md5.md5Hash(password);

//                        Passengers passenger = new Passengers(name,email,address,phone,passEncrypt.toString());

//                        db.collection("userCredentials").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                if(!queryDocumentSnapshots.isEmpty()) {
//                                    Boolean validCredentials = false;
//                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                                    boolean flag = false;
//                                    for(DocumentSnapshot d: list){
//                                        Map<String, Object> details = d.getData();
//                                        String dbemail = details.get("email").toString();
//                                        if(email.equals(dbemail)){
//                                            flag = true;
//                                            String userCredentialId = d.getId();
//                                            break;
//                                        }
//                                   }
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
                    Map<String, Object> parent = new HashMap<>();
                    parent.put("parentEmail",email);
                    parent.put("parentPhone",parentPhone);
                    parent.put("parentAddress",parentAddress);
                    parent.put("childName",childName);
                    parent.put("childAge",childAge);
                    parent.put("childSchool",childSchool);
                    parent.put("childSchoolPhone",childSchoolPhone);

                    dbParent.add(parent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dialog.dismiss();

                            String parentId = documentReference.getId();

                            Map<String, Object> userCredential = new HashMap<>();
                            userCredential.put("parentId",parentId);
                            userCredential.put("email",email);
                            userCredential.put("password",hashedPassword);

                            CurrentUser cu = new CurrentUser();
                            String userCredentialId = cu.getuUserCredentialId();
                            final DocumentReference dbUserCredential = db.document("userCredentials/"+userCredentialId);


                            dbUserCredential.update(userCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                    Toast.makeText(ParentSignup1Activity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ParentSignup1Activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ParentSignup1Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

}
