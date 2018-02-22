package com.afroz.watermanagementsystem;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main3Activity extends AppCompatActivity {

    Button btnSendSMS;
    IntentFilter intentFilter;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static String phoneNo = "";
    private static final String message = "es";
    private ProgressBar spinner;
    double moisture;
    double x;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference soilm = database.getReference("SoilMoisture");

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView l = (TextView) findViewById(R.id.textView5);
            l.setText(intent.getExtras().getString("moisture") + "%" );
            TextView p = (TextView) findViewById(R.id.textView8);

            x = Double.parseDouble(intent.getExtras().getString("moisture"));
            if(x < 30)
                p.setText("Dry Soil");
            else if(x > 30 && x<70)
                p.setText("Moist Soil");
            else
                p.setText("Over Moist");

            soilm.setValue(x);
            moisture = x;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        // Read from the database
        soilm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Double value = dataSnapshot.getValue(Double.class);
                 x = value;
                TextView l = (TextView) findViewById(R.id.textView5);
                String s = value.toString();
                l.setText(s);
               // Log.d(TAG, "Value is: " + value);
                TextView p = (TextView) findViewById(R.id.textView8);
                if(x < 30)
                    p.setText("Dry Soil");
                else if(x > 30 && x<70)
                    p.setText("Moist Soil");
                else
                    p.setText("Over Moist");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        //---register the receiver--
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //---unregister the receiver--
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
    

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
