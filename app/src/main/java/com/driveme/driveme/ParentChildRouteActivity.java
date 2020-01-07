package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentChildRouteActivity extends AppCompatActivity {

    private CardView viewRoute;
    private CardView dailyAvailability;
    private CardView viewPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_child_route);
        setTitle("DriveMe - MyRoute");

        viewRoute = findViewById(R.id.viewRoute);
        dailyAvailability = findViewById(R.id.dailyAvailability);
        viewPassengers = findViewById(R.id.viewPassengers);

        viewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentChildRouteActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String parentId = cu.getParentId();

                db.document("users/user/parent/"+parentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverId = documentSnapshot.getString("driverId");
                        if(driverId==null || driverId.isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Route have been Added", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                        else{
                            Intent intent = new Intent(ParentChildRouteActivity.this,ParentRouteActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });

        dailyAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ParentChildRouteActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String parentId = cu.getParentId();

                db.document("users/user/parent/"+parentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverId = documentSnapshot.getString("driverId");
                        if(driverId==null || driverId.isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Route have been Added", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                        else{
                            Intent intent = new Intent(ParentChildRouteActivity.this, ParentAvailabilityActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });

        viewPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ParentChildRouteActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CurrentUser cu = new CurrentUser();
                String parentId = cu.getParentId();

                db.document("users/user/parent/"+parentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String driverId = documentSnapshot.getString("driverId");
                        if(driverId==null || driverId.isEmpty()){
                            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Route have been Added", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                        else{
                            Intent intent = new Intent(ParentChildRouteActivity.this, ParentPassengerListActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });
    }
}
