package com.asergeev.imhere.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.asergeev.imhere.R;

/**
 * Created by Andrey on 08.07.2017.
 */

public class Child extends AppCompatActivity {
    ImageButton imageButton;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Child.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
