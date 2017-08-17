package com.asergeev.imhere.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asergeev.imhere.R;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andrey on 8/7/2017.
 */


public class Menu1 extends Fragment {

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_menu_1, container, false);
        textView = (TextView) rootView.findViewById(R.id.textView14);
        String a;
        SharedPreferences pref1 = getContext().getSharedPreferences("Pref", MODE_PRIVATE);
        a= pref1.getString("Code", "");
        textView.setText(a);






            return rootView;










    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");


    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
