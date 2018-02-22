package com.afroz.watermanagementsystem;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    Button btnSendSMS;

    private static final String TAG = " main 2";
    IntentFilter intentFilter;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static String phoneNo="";
    String lo, u;
    private DatabaseReference mDatabase;
    private static final String message="es";
    private ProgressBar spinner;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference low = database.getReference("lowerVal");
    DatabaseReference up = database.getReference("upperVal");

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView l = (TextView) findViewById(R.id.textView4);
            l.setText(intent.getExtras().getString("sms"));
            TextView ltval = (TextView) findViewById(R.id.textView);
            lo = intent.getExtras().getString("lowerTank");
            ltval.setText(intent.getExtras().getString("lowerTank") + "%");

            TextView utval = (TextView) findViewById(R.id.textView2);
            u = intent.getExtras().getString("upperTank");
            utval.setText(intent.getExtras().getString("upperTank") + "%");
            TextView ms = (TextView) findViewById(R.id.textView3);
            ms.setText(intent.getExtras().getString("motorStatus"));

            low.setValue(lo);

            up.setValue(u);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        low.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView l = (TextView) findViewById(R.id.textView);
                l.setText(value);
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        up.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView l = (TextView) findViewById(R.id.textView2);
                l.setText(value);
                // Log.d(TAG, "Value is: " + value);
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

    public void submitPost(View view) {
        final double title = Double.parseDouble(lo);
        //mTitleField.getText().toString();
        final double body = Double.parseDouble(u);
        //mBodyField.getText().toString();


        // Disable button so there are no multi-posts

        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lowerVal");
        myRef.setValue("Hello, World!");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
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
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
