package com.asergeev.imhere.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asergeev.imhere.R;
import com.asergeev.imhere.app.Config;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by Andrey on 08.07.2017.
 */

public class Child extends AppCompatActivity {
    ImageButton imageButton;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);
        String a =  generateString();
        Log.e("IDa",a);
        textView = (TextView)findViewById(R.id.id);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Child.this, MainActivity.class);
                startActivity(intent);

            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("codes");
        myRef.child(a).child("tokenID").setValue(regId);
        textView.setText(a);
    }
    public static String generateString() {
        String uuid = UUID.randomUUID().toString().replace("-","").substring(0,6);
        return uuid;
    }


}
