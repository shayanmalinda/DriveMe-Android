package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class ParentNotificationActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_notification);
        setTitle("Notifications");

        getPaymentList();

    }

    public void getPaymentList(){

        CurrentUser cu = new CurrentUser();
        final String parentId = cu.getParentId();

        db = FirebaseFirestore.getInstance();
        final List<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.payment_request_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentNotificationActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db.collection("users/user/parent/"+parentId+"/payments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    if(!querySnapshot.getBoolean("isAccepted")){
                        HashMap<String,String> map  = new HashMap();
                        map.put("date","Date  :   "+querySnapshot.getString("date"));
                        map.put("value","Value  :   "+querySnapshot.getString("value"));
                        map.put("paymentId",querySnapshot.getId());
                        map.put("driverPaymentId",querySnapshot.getString("driverPaymentId"));
                        map.put("parentId",querySnapshot.getReference().getParent().getParent().getId());
                        driverId = querySnapshot.getString("driverId");
                        list.add(map);
                    }
                }
                int layout = R.layout.item_payment_request;
                String[] cols = {"paymentId","parentId","driverPaymentId","date","value"};
                int[] views = {R.id.paymentId,R.id.parentId,R.id.driverPaymentId,R.id.date,R.id.value,};
                SimpleAdapter adapter = new SimpleAdapter(ParentNotificationActivity.this,list,layout,cols,views);
                lv.setAdapter(adapter);

                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void acceptPayment(View v){

        LinearLayout view = (LinearLayout)v.getParent().getParent();
        TextView txtparentId = view.findViewById(R.id.parentId);
        TextView txtpaymentId = view.findViewById(R.id.paymentId);
        TextView txtdriverPaymentId = view.findViewById(R.id.driverPaymentId);
        String parentId = txtparentId.getText().toString();
        String paymentId = txtpaymentId.getText().toString();
        String driverPaymentId = txtdriverPaymentId.getText().toString();

        db.document("users/user/parent/"+parentId+"/payments/"+paymentId).update("isAccepted",true);

        db.document("users/user/driver/"+driverId+"/payments/"+parentId+"/payments/"+driverPaymentId).update("isAccepted",true);
        getPaymentList();

        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Payment Accepted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void declinePayment(View v){

        LinearLayout view = (LinearLayout)v.getParent().getParent();
        TextView txtparentId = view.findViewById(R.id.parentId);
        TextView txtpaymentId = view.findViewById(R.id.paymentId);
        TextView txtdriverPaymentId = view.findViewById(R.id.driverPaymentId);
        String parentId = txtparentId.getText().toString();
        String paymentId = txtpaymentId.getText().toString();
        String driverPaymentId = txtdriverPaymentId.getText().toString();


        db.document("users/user/parent/"+parentId+"/payments/"+paymentId).delete();

        db.document("users/user/driver/"+driverId+"/payments/"+parentId+"/payments/"+driverPaymentId).delete();
        getPaymentList();

        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Payment Declined", Snackbar.LENGTH_LONG);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
