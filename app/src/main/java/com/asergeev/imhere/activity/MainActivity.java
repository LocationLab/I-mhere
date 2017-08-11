package com.asergeev.imhere.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asergeev.imhere.R;
import com.asergeev.imhere.app.Config;
import com.asergeev.imhere.util.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
/**
 * Created by Andrey on 8/7/2017.
 */
public class MainActivity extends AppCompatActivity {
    Button child;
    Button par;
    private static final String MY_PREFERENCES = "my_preferences";
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    private FirebaseAuth mAuth;
    private boolean first = true;
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null ) {
            // No user is signed in
            Boolean value;
            SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
            value= pref1.getBoolean("Value", false);
            Log.d("DMT", String.valueOf(value));
            if(value){
                Intent intent = new Intent(MainActivity.this, Children_sec.class);
                startActivity(intent);
                finish();
            }

            setContentView(R.layout.activity_main);

            child = (Button) findViewById(R.id.button3);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, Child.class);
                    startActivity(intent);

                }
            });
            par = (Button) findViewById(R.id.button2);
            par.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Parent.class);
                    startActivity(intent);
                }
            });

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                    }
                }
            };
            displayFirebaseRegId();

            ActivityCompat.requestPermissions(MainActivity.this,

                    new String[]{

                            android.Manifest.permission.ACCESS_FINE_LOCATION

                    },

                    1);
            // Fetches reg id from shared preferences
            // and displays on the screen

        } else {
            // User logged in
            Intent intent = new Intent(MainActivity.this, Drawer.class);
            startActivity(intent);
            finish();

        }



    }
    @Override

    public void onRequestPermissionsResult(int requestCode,

                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1: {



                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0

                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                    // permission was granted, yay!

                } else {



                    // permission denied, boo!

                    Toast.makeText(MainActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();

                }

                return;

            }





        }

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))

            Log.e("ID",regId);



    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
