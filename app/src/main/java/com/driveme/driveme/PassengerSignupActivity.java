package com.driveme.driveme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class PassengerSignupActivity extends AppCompatActivity {

    private EditText etemail;
    private EditText etname;
    private EditText etphone;
    private EditText etaddress;
    private EditText etpass1;
    private EditText etpass2;
    private TextView telephoneError;

    private Button signup;

    private FirebaseFirestore db;

    private String email;
    private String name;
    private String phone;
    private String address;
    private String password;
    private String hashedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_signup);

        setTitle("DriverMe - Passenger");

        db = FirebaseFirestore.getInstance();

        etphone = findViewById(R.id.txtphone);
        etaddress = findViewById(R.id.txtaddress);
        etname = findViewById(R.id.txtname);
        signup = findViewById(R.id.btnpassengersignup);
        telephoneError = findViewById(R.id.telephoneError);

        etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String tp = etphone.getText().toString().trim();
                if(etphone.getText().toString().length()==9 && tp.charAt(0)=='0'){
                    telephoneError.setText("");

                    signup.setClickable(true);
                    signup.setEnabled(true);

                }
                else{
                    telephoneError.setText("Please Enter Valid Phone Number");
                    telephoneError.setTextColor(getResources().getColor(R.color.colorAccent));

                    signup.setClickable(false);
                    signup.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerSignupActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();



                Bundle extras = getIntent().getExtras();
                email = extras.getString("email");
                password = extras.getString("password");
                name = etname.getText().toString();
                phone = etphone.getText().toString();
                address = etaddress.getText().toString();

                if(name.isEmpty() || phone.isEmpty() || address.isEmpty()){
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

                    dialog.show();

                    CollectionReference dbPassenger = db.collection("users/user/passenger");
                    BigInteger passEncrypt = null;


                    md5 md5 = new md5();
                    hashedPassword = md5.md5Hash(password);

//                        Passengers passenger = new Passengers(name,email,address,phone,passEncrypt.toString());
                    Passengers passenger = new Passengers(name,email,address,phone);

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

                    dbPassenger.add(passenger).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dialog.dismiss();

                            String passengerId = documentReference.getId();

                            Map<String, Object> userCredential = new HashMap<>();
                            userCredential.put("passengerId",passengerId);
                            userCredential.put("email",email);
                            userCredential.put("password",hashedPassword);

                            CurrentUser cu = new CurrentUser();
                            String userCredentialId = cu.getuUserCredentialId();
                            final DocumentReference dbUserCredential = db.document("userCredentials/"+userCredentialId);


                            dbUserCredential.update(userCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                    Toast.makeText(PassengerSignupActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PassengerSignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(PassengerSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });


    }
}
