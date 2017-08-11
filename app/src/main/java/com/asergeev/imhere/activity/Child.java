package com.asergeev.imhere.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asergeev.imhere.R;
import com.asergeev.imhere.app.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

/**
 * Created by Andrey on 08.07.2017.
 */

public class Child extends AppCompatActivity {
    ImageButton imageButton;
    TextView textView;
    private FirebaseAuth mAuth;
    Button button;
    ImageButton imageButton1;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);
        final String a =  generateString();
        Log.e("IDa",a);
        textView = (TextView)findViewById(R.id.idm);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        button = (Button) findViewById(R.id.btn1);
        editText = (EditText) findViewById(R.id.editText3);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                imageButton1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton1.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                imageButton1.setVisibility(View.VISIBLE);

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Child.this, MainActivity.class);
                startActivity(intent);

            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref1.edit();
                editor.putBoolean("Value", true);
                editor.commit();
                String code;
                code = editText.getText().toString();
                Intent intent = new Intent(Child.this, Children_sec.class);
                startActivity(intent);
                editor.putString("Code", code);
                editor.commit();

            }
        });
       // final boolean first = reader.getBoolean("is_first", true);
        textView.setText(a);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref1 = getSharedPreferences("Pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref1.edit();
                editor.putBoolean("Value", true);
                editor.commit();

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("codes");
                myRef.child(a).child("tokenID").setValue(regId);
                myRef.child(a).child("state").setValue(1);
                editor.putString("Code", a);
                editor.commit();


                Intent intent = new Intent(Child.this, Children_sec.class);
                startActivity(intent);
            }
        });



    }
    public static String generateString() {
        String uuid = UUID.randomUUID().toString().replace("-","").substring(0,6);
        return uuid;
    }


}
