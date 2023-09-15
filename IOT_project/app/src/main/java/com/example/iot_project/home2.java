package com.example.iot_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home2 extends AppCompatActivity {

    private GaugeView sensorAGauge, sensorBGauge;
    private Switch switch1, switch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        sensorAGauge = findViewById(R.id.sensorAGauge);
        sensorBGauge = findViewById(R.id.sensorBGauge);
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        // Read values from Firebase on app launch
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get sensor values from Firebase and set them in the GaugeView
                float value1 = dataSnapshot.child("sensorA").getValue(Float.class);
                int value2 = dataSnapshot.child("sensorB").getValue(Integer.class);

                // Update the GaugeView with the new values
                sensorAGauge.setValue(value1);
                sensorBGauge.setValue(value2);

                // Update the switches based on Firebase data
                switch1.setChecked(dataSnapshot.child("switches").child("sensorAKey").getValue(Boolean.class));
                switch2.setChecked(dataSnapshot.child("switches").child("sensorBKey").getValue(Boolean.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        // Set switch change listeners to update values in Firebase
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseDatabase.getInstance().getReference("switches").child("sensorAKey").setValue(isChecked);
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseDatabase.getInstance().getReference("switches").child("sensorBKey").setValue(isChecked);
            }
        });
    }
}
