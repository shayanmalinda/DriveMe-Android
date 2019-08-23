package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerRouteSearch2Activity extends AppCompatActivity {


    private static ArrayList<String> filteredRoutes = new ArrayList<String>();

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_route_search2);
        setTitle("Route Search - Results");
        Bundle extras = getIntent().getExtras();

        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerRouteSearch2Activity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        db = FirebaseFirestore.getInstance();

        filteredRoutes = extras.getStringArrayList("filteredRoutes");

        final List<HashMap<String, String>> list = new ArrayList<>();

        if(filteredRoutes.isEmpty()){
            Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        for(final String driverId:filteredRoutes){

            db.document("users/user/driver/"+driverId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    HashMap<String,String> map  = new HashMap();
                    map.put("driverId",documentSnapshot.getId());
                    map.put("startPlace",documentSnapshot.getString("startPlaceName"));
                    map.put("endPlace",documentSnapshot.getString("endPlaceName"));
                    map.put("startTime",documentSnapshot.getString("startTime"));
                    map.put("endTime",documentSnapshot.getString("endTime"));
                    list.add(map);

                    final ListView lv = findViewById(R.id.route_list);

                    int layout = R.layout.item_route;
                    String[] cols = {"driverId","startPlace","startTime","endPlace","endTime"};
                    int[] views = {R.id.driverId,R.id.startPlace,R.id.startTime,R.id.endPlace,R.id.endTime};
                    SimpleAdapter adapter = new SimpleAdapter(PassengerRouteSearch2Activity.this,list,layout,cols,views);
                    lv.setAdapter(adapter);
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    dialog.dismiss();
                }
            });
        }


    }

}
