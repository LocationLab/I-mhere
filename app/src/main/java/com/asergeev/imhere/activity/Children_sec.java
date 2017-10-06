package com.asergeev.imhere.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asergeev.imhere.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Andrey on 8/7/2017.
 */

public class Children_sec extends AppCompatActivity implements LocationListener {

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 100;
    double currentLatitude = 55.457559, currentLongitude = 38.159159;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public static final String TAG = "Activity";
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private String a;
    private Button button;
    private double Latitude = 0;
    private double Longtitude = 0;
    private int PLACE_PICKER_REQUEST = 1;
    private static Context context;
    private TextView textView;
    PendingIntent mGeofencePendingIntent;
    LocationRequest mLocationRequest;
    Boolean locationFound;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure);

        textView =(TextView)findViewById(R.id.textView5);
        button = (Button)findViewById(R.id.btn223);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Children_sec.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.e("EE","Error");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();

                }
            }
        });

        SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
        a= pref1.getString("Code", "");
        textView.setText(a);


        Log.d(a,a);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
        if (resultCode == RESULT_OK) {
            SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
            Place place = PlacePicker.getPlace(data, this);
            String toastMsg = String.format("Place: %s", place.getAddress());
            Latitude = place.getLatLng().latitude;
            Longtitude =place.getLatLng().longitude;
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = pref1.edit();
            editor.putString("Place", toastMsg);
            editor.commit();


                if(Latitude != 0 && Longtitude != 0) {
                    mGeofenceList = new ArrayList<Geofence>();
                    int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                    if (resp == ConnectionResult.SUCCESS) {
                        initGoogleAPIClient();
                        createGeofences(Latitude, Longtitude);
                    } else {
                        Log.e(TAG, "Your Device doesn't support Google Play Services.");
                    }
                    // Create the LocationRequest object
                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                            .setFastestInterval(1 * 1000); // 1 second, in milliseconds
                    }
                }
         }
    }

    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "onConnected");

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (location == null) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) Children_sec.this);
                    } else {
                        //If everything went fine lets get latitude and longitude
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        Log.i(TAG, currentLatitude + " WORKS " + currentLongitude);
                        //createGeofences(currentLatitude, currentLongitude);
                        //registerGeofences(mGeofenceList);
                    }

                    try{
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                getGeofencingRequest(),
                                getGeofencePendingIntent()
                        ).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Log.i(TAG, "Saving Geofence");
                                } else {
                                    Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                            " : " + status.getStatusCode());
                                }
                            }
                        });
                    } catch (SecurityException securityException) {
                        // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                        Log.e(TAG, "Error");
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.e(TAG, "onConnectionSuspended");
                }
            };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e(TAG, "onConnectionFailed");
                }
            };
    /**
     * Create a Geofence list
     */
    public void createGeofences(double latitude, double longitude) {
        SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);

        String id = UUID.randomUUID().toString();
        a= pref1.getString("Place", id);
        Geofence fence = new Geofence.Builder()
                .setRequestId(a)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 100)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(TAG, "onLocationChanged");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
