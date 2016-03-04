package com.baelight.weatherartist.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.baelight.weatherartist.activity.WeatherActivity;


public class AboutFragement extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Weather Artist 1.0.0")
                .setMessage("A weather app for Android")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("Check Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((WeatherActivity)getActivity()).queryUpdate();
                    }
                })
                .setCancelable(false);

        // Create the AlertDialog object and return it
        return builder.create();
    }


}
