package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseTimeActivity2 extends AppCompatActivity {

    private Button btnDone;
    private TimePicker timePicker;
    private Button btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time2);

        btncancel = findViewById(R.id.btncancel);

        SelectedTime st = new SelectedTime();
        st.setSelectedTime(null);

        setTitle("Choose Time");

//        final DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//
//        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));



        btnDone = findViewById(R.id.btndone);
        timePicker = findViewById(R.id.timepicker);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String selectedtime = hour+" : "+minute;
                if(minute==0){
                    selectedtime = selectedtime+"0";
                }
                if(hour==0){
                    selectedtime = "0"+selectedtime;
                }

                SelectedTime st = new SelectedTime();
                st.setSelectedTime(selectedtime);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChooseTimeActivity2.this,DriverRouteActivity2.class);
                finish();
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SelectedTime st = new SelectedTime();
        st.setSelectedTime(null);
    }
}
