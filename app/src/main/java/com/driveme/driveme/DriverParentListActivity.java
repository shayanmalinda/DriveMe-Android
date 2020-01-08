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

public class DriverParentListActivity extends AppCompatActivity {

    FirebaseFirestore db;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_parent_list);

        setTitle("Parent List");

        getParentList();


    }

    public void getParentList(){

        db = FirebaseFirestore.getInstance();
        CurrentUser cu = new CurrentUser();
        final String userId = cu.getDriverId();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final List<HashMap<String, String>> list2 = new ArrayList<>();
        final ListView lv = findViewById(R.id.parent_list);
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverParentListActivity.this);
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
                            list2.add(map);
                        }
                        int layout = R.layout.item_parent2;
                        String[] cols = {"parentId","childName","childAge","childSchool","childSchoolPhone","parentEmail","parentPhone","parentAddress","pickupLocation"};
                        int[] views = {R.id.parentId,R.id.childName,R.id.childAge,R.id.childSchool,R.id.childSchoolPhone,R.id.parentEmail,R.id.parentPhone,R.id.parentAddress,R.id.pickupLocation};
                        SimpleAdapter adapter = new SimpleAdapter(DriverParentListActivity.this,list2,layout,cols,views);
                        lv.setAdapter(adapter);
                    }
                    dialog.dismiss();
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

    public void removeParent(View v){

        db = FirebaseFirestore.getInstance();

        LinearLayout view = (LinearLayout)v.getParent();
        TextView txtParentId = view.findViewById(R.id.parentId);
        String parentId = txtParentId.getText().toString();

        CurrentUser cu = new CurrentUser();
        String driverId = cu.getDriverId();

        db.collection("users/user/parent/"+parentId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    q.getReference().delete();
                }
            }
        });


        db.collection("users/user/driver/"+driverId+"/payments/"+parentId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    q.getReference().delete();
                }
            }
        });


        db.collection("users/user/driver/"+driverId+"/availability/"+parentId+"/availability").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    q.getReference().delete();
                }
            }
        });


        db.document("users/user/parent/"+parentId).update("driverId","");
        getParentList();
    }

    public void viewRatings(View v){

        db = FirebaseFirestore.getInstance();

        LinearLayout view = (LinearLayout)v.getParent();
        TextView txtparentId = view.findViewById(R.id.parentId);
        String parentId = txtparentId.getText().toString();

        Intent intent = new Intent(DriverParentListActivity.this,DriverParentRatingsActivity.class);
        intent.putExtra("parentId",parentId);
        startActivity(intent);

    }

    public void newRating(View v){

        db = FirebaseFirestore.getInstance();

        LinearLayout view = (LinearLayout)v.getParent();
        TextView txtparentId = view.findViewById(R.id.parentId);
        String parentId = txtparentId.getText().toString();

        Intent intent = new Intent(DriverParentListActivity.this,DriverParentNewRatingActivity.class);
        intent.putExtra("parentId",parentId);
        startActivity(intent);
    }
}
