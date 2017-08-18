package com.asergeev.imhere.activity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.asergeev.imhere.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 8/7/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = "GeofenceTransitions";
    private String a;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
        a= pref1.getString("Code", "");
        Log.i(TAG, "onHandleIntent");

        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        Log.i(TAG, "geofenceTransition = " + geofenceTransition + " Enter : " + Geofence.GEOFENCE_TRANSITION_ENTER + "Exit : " + Geofence.GEOFENCE_TRANSITION_EXIT);
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
            showNotification("Уведомление", "Вы пришли");
            mMessagesReference = mDatabase.getReference().child("messages");
            Message message = new Message("Уведомление", "Я здесь", a);
            mMessagesReference.push().setValue(message);
        }
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.i(TAG, "Showing Notification...");
            showNotification("Уведомление", "Вы ушли");
            mMessagesReference = mDatabase.getReference().child("messages");
            Message message = new Message("Уведомление", "Я вышел", a);
            mMessagesReference.push().setValue(message);
        } else {
            // Log the error.
            showNotification("Error", "Error");
            Log.e(TAG, "Error ");
        }
    }

    public void showNotification(String text, String bigText) {
        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.gps)
                .setContentTitle("Уведомление")
                .setContentText(text)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}
