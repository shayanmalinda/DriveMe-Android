package com.driveme.driveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DriverAddNewPaymentActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_new_payment);
        db = FirebaseFirestore.getInstance();

        setTitle("New Payment");

        Bundle bundle = getIntent().getExtras();
        final String passengerId = bundle.getString("passengerId");

        final DatePicker datePicker = findViewById(R.id.datePicker);
        Button btnAddPayment = findViewById(R.id.btnaddpayment);
        final EditText etPayment = findViewById(R.id.etPayment);

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(currentYear, currentMonth, currentDay, null);
        datePicker.setMaxDate(System.currentTimeMillis());


        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DriverAddNewPaymentActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();

                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                String dateString = year+"-"+month+"-"+day;

                final String paymentValue = etPayment.getText().toString();
                CurrentUser cu = new CurrentUser();
                final String driverId = cu.getDriverId();


                final Map<String, Object> objPayment = new HashMap<>();
                objPayment.put("date",dateString);
                objPayment.put("value",paymentValue);
                objPayment.put("isAccepted",false);

                db.collection("users/user/driver/"+driverId+"/payments/"+passengerId+"/payments/").add(objPayment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        objPayment.put("driverId",driverId);
                        db.collection("users/user/passenger/"+passengerId+"/payments/").add(objPayment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                dialog.dismiss();

                                Toast.makeText(DriverAddNewPaymentActivity.this, "Payment Added", Toast.LENGTH_SHORT).show();
                                DriverAddNewPaymentActivity.this.finish();
                            }
                        });



                    }
                });

            }
        });

    }
}
