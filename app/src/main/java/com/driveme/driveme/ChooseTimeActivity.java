package com.driveme.driveme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.sql.Time;

public class ChooseTimeActivity extends AppCompatActivity {

    private Button btnDone;
    private TimePicker timePicker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

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
    }
}
