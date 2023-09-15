package com.example.iot_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.iot_project.GaugeView;
import com.example.iot_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home extends AppCompatActivity {

    private TextView value1TextView, value2TextView, value3TextView;
    private Switch switch1, switch2;
    private GaugeView sensorAGauge;

    private Handler handler = new Handler();
    private final int delay = 1000; // Refresh every seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sensorAGauge = findViewById(R.id.sensorAGauge);
        value1TextView = findViewById(R.id.value1TextView);
        value2TextView = findViewById(R.id.value2TextView);
        value3TextView = findViewById(R.id.value3TextView);
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        // Create a Runnable to periodically update data
        final Runnable refreshDataRunnable = new Runnable() {
            @Override
            public void run() {
                refreshData();
                handler.postDelayed(this, delay); // Schedule the Runnable to run again
            }
        };

        // Start refreshing data immediately
        handler.post(refreshDataRunnable);

        // Set switch change listeners to update values in Firebase
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseDatabase.getInstance().getReference("switches").child("tempkey").setValue(isChecked);
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseDatabase.getInstance().getReference("switches").child("rainkey").setValue(isChecked);
            }
        });
    }

    private void refreshData() {
        FirebaseDatabase.getInstance().getReference("values").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float value1 = dataSnapshot.child("Temp").getValue(Float.class);
                String value11 = String.valueOf(value1); // Avoid duplicate getValue() calls
                String value2 = String.valueOf(dataSnapshot.child("rainAnalogVal").getValue(Integer.class));
                String value3 = String.valueOf(dataSnapshot.child("rainDigitalVal").getValue(Integer.class));
                sensorAGauge.setValue(value1);
                value1TextView.setText("Temp Value: " + value11);
                value2TextView.setText("Rain Analog Value: " + value2);
                value3TextView.setText("Rain Digital Value: " + value3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
