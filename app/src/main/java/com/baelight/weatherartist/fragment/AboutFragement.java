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
//                .setNeutralButton("Buy me a zongzi", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onclick(dialoginterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("message/rfc822");
//                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"findnick@163.com"});
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        try{
//                            getActivity().startActivity(Intent.createChooser(intent,"Send..."));
//                        } catch (android.content.ActivityNotFoundException e){
//
//                        }
//                    }
//                })
                .setNeutralButton("Check Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((WeatherActivity)getActivity()).checkUpdate();
                    }
                })
                .setCancelable(false);

        // Create the AlertDialog object and return it
        return builder.create();
    }


}
