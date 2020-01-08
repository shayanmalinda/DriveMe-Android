package com.driveme.driveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DriverParentNewRatingActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private Button cancel;
    private Button ratenow;
    private EditText etrating;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private Boolean flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_parent_new_rating);
        setTitle("Rate NOW");
        flag = false;
        final int[] stars = new int[1];

        star1 = findViewById(R.id.btnstar1);
        star2 = findViewById(R.id.btnstar2);
        star3 = findViewById(R.id.btnstar3);
        star4 = findViewById(R.id.btnstar4);
        star5 = findViewById(R.id.btnstar5);
        etrating = findViewById(R.id.etRating);


        Bundle extras = getIntent().getExtras();

        final String parentId = extras.getString("parentId");

        cancel = findViewById(R.id.btncancel);
        ratenow = findViewById(R.id.btnrateNow);
        db = FirebaseFirestore.getInstance();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                stars[0] = 1;
                star1.setImageResource(R.drawable.ic_star_yellow_24dp);
                star2.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star3.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star4.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star5.setImageResource(R.drawable.ic_star_border_grey_24dp);
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                stars[0] = 2;
                star1.setImageResource(R.drawable.ic_star_yellow_24dp);
                star2.setImageResource(R.drawable.ic_star_yellow_24dp);
                star3.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star4.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star5.setImageResource(R.drawable.ic_star_border_grey_24dp);

            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                stars[0] = 3;
                star1.setImageResource(R.drawable.ic_star_yellow_24dp);
                star2.setImageResource(R.drawable.ic_star_yellow_24dp);
                star3.setImageResource(R.drawable.ic_star_yellow_24dp);
                star4.setImageResource(R.drawable.ic_star_border_grey_24dp);
                star5.setImageResource(R.drawable.ic_star_border_grey_24dp);

            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                stars[0] = 4;
                star1.setImageResource(R.drawable.ic_star_yellow_24dp);
                star2.setImageResource(R.drawable.ic_star_yellow_24dp);
                star3.setImageResource(R.drawable.ic_star_yellow_24dp);
                star4.setImageResource(R.drawable.ic_star_yellow_24dp);
                star5.setImageResource(R.drawable.ic_star_border_grey_24dp);

            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                stars[0] = 5;
                star1.setImageResource(R.drawable.ic_star_yellow_24dp);
                star2.setImageResource(R.drawable.ic_star_yellow_24dp);
                star3.setImageResource(R.drawable.ic_star_yellow_24dp);
                star4.setImageResource(R.drawable.ic_star_yellow_24dp);
                star5.setImageResource(R.drawable.ic_star_yellow_24dp);

            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverParentNewRatingActivity.this.finish();
            }
        });

        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etrating = findViewById(R.id.etRating);
                String rating = etrating.getText().toString();
                if(!flag){

                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Select at least 1 Star", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverParentNewRatingActivity.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.layout_loading_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                    String strDate = mdformat.format(calendar.getTime());

                    Map<String, Object> objrate = new HashMap<>();
                    objrate.put("rating",rating);
                    objrate.put("date",strDate);
                    objrate.put("stars",stars[0]);

                    Log.d("parentID",""+parentId);

                    db.collection("users/user/parent/"+parentId+"/ratings").add(objrate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dialog.dismiss();

                            Toast.makeText(DriverParentNewRatingActivity.this, "Rating Success", Toast.LENGTH_SHORT).show();
                            DriverParentNewRatingActivity.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        etrating.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });


    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
