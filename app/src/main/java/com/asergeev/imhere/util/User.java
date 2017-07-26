package com.asergeev.imhere.util;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Andrey on 24.07.2017.
 */
@IgnoreExtraProperties
public class User extends AppCompatActivity {

    public String regID;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String regID, String email) {
        this.regID = regID;
        this.email = email;
    }
}
