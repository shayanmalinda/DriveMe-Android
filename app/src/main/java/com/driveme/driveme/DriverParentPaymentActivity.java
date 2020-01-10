package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverParentPaymentActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_parent_payment);

        setTitle("Parent Payments");

        getPassengerList();


    }

    public void getPassengerList(){

        db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        final String userId = cu.getDriverId();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.parent_payment_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverParentPaymentActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();


        db.collection("users/user/parent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean flag = false;
                if(!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                        if(querySnapshot.getString("driverId")!=null && querySnapshot.getString("driverId").equals(userId)){
                            HashMap<String,String> map  = new HashMap();
                            flag = true;
                            map.put("parentId",querySnapshot.getId());
                            map.put("childName","Child's Name  :   "+querySnapshot.getString("childName"));
                            map.put("childAge","Child's Age  :   "+querySnapshot.getString("childAge"));
                            map.put("childSchool","Child's School  :   "+querySnapshot.getString("childSchool"));
                            map.put("childSchoolPhone","School Phone  :   "+querySnapshot.getString("childSchoolPhone"));
                            map.put("parentEmail","Parent's Email  :   "+querySnapshot.getString("parentEmail"));
                            map.put("parentPhone","Parent's Phone  :   "+querySnapshot.getString("parentPhone"));
                            map.put("parentAddress","Parent's Address  :   "+querySnapshot.getString("parentAddress"));
                            map.put("pickupLocation","Pickup Location  :   "+querySnapshot.getString("pickupLocation"));
                            list.add(map);
                        }
                        int layout = R.layout.item_parent_payment;
                        String[] cols = {"parentId","childName","childAge","childSchool","childSchoolPhone","parentEmail","parentPhone","parentAddress","pickupLocation"};
                        int[] views = {R.id.parentId,R.id.childName,R.id.childAge,R.id.childSchool,R.id.childSchoolPhone,R.id.parentEmail,R.id.parentPhone,R.id.parentAddress,R.id.pickupLocation};
                        SimpleAdapter adapter = new SimpleAdapter(DriverParentPaymentActivity.this,list,layout,cols,views);
                        lv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                }
                if(!flag){
//                    finish();

                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Any Parents", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }

    public void addNewPayment(View v){

        LinearLayout view = (LinearLayout)v.getParent();
        TextView txtpassengerId = view.findViewById(R.id.parentId);
        String passengerId = txtpassengerId.getText().toString();

        Intent intent = new Intent(DriverParentPaymentActivity.this,DriverParentPaymentAddActivity.class);
        intent.putExtra("parentId",passengerId);
        startActivity(intent);


    }

    public void viewPayments(View v){

        LinearLayout view = (LinearLayout)v.getParent();
        TextView txtpassengerId = view.findViewById(R.id.parentId);
        String passengerId = txtpassengerId.getText().toString();

        Intent intent = new Intent(DriverParentPaymentActivity.this,DriverParentPaymentViewActivity.class);
        intent.putExtra("parentId",passengerId);
        startActivity(intent);

    }

}
