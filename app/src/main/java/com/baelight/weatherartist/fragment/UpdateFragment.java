package com.baelight.weatherartist.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.app.AlertDialog;
import android.text.TextUtils;

import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;
import com.baelight.weatherartist.service.AppUpdateService;

/**
 * update
 */
public class UpdateFragment extends DialogFragment {

    private String apkUrl;
    private String updateMsg;

    private DialogInterface.OnClickListener buttonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // download apk
                    if (!TextUtils.isEmpty(apkUrl)) {
                        Intent intent = new Intent(MyApplication.getContext(), AppUpdateService.class);
                        intent.putExtra(getString(R.string.new_update_url), apkUrl);
                        MyApplication.getContext().startService(intent);
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        apkUrl = bundle.getString(getString(R.string.new_update_url), "");
        updateMsg = bundle.getString(getString(R.string.new_update_msg), "");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
        builder.setTitle(R.string.new_update)
                .setMessage(updateMsg)
                .setPositiveButton(R.string.update_now, buttonClickListener)
                .setNegativeButton(R.string.update_later, buttonClickListener);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
