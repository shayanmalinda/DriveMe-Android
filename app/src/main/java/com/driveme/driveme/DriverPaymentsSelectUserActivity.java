package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DriverPaymentsSelectUserActivity extends AppCompatActivity {

    private CardView passengerPayments;
    private CardView parentPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_payments_select_user);


        setTitle("Payments");

        passengerPayments = findViewById(R.id.passengerPayments);
        parentPayments = findViewById(R.id.parentPayments);

        passengerPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverPaymentsSelectUserActivity.this,DriverPaymentActivity.class);
                startActivity(intent);
            }
        });

        parentPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverPaymentsSelectUserActivity.this,DriverParentPaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
