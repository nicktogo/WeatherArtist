package com.baelight.weatherartist.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.baelight.weatherartist.BuildConfig;
import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;
import com.baelight.weatherartist.fragment.UpdateFragment;
import com.baelight.weatherartist.model.Update;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by nick on 2016/3/4.
 */
public class ApkUpdateAgent {

    public static final String URL_PREFIX = "http://file.bmob.cn/";

    public static void update(final Activity activity) {
        BmobQuery<Update> queryUpdate = new BmobQuery<>();
        queryUpdate.addWhereGreaterThan(Update.VERSION_CODE, BuildConfig.VERSION_CODE);
        queryUpdate.setLimit(1);
        queryUpdate.findObjects(MyApplication.getContext(), new FindListener<Update>() {
            @Override
            public void onSuccess(List<Update> list) {
                for (Update update : list) {
                    DialogFragment updateFragment = new UpdateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(activity.getString(R.string.new_update_url), URL_PREFIX + update.getApk().getUrl());
                    bundle.putString(activity.getString(R.string.new_update_msg), update.getUpdateMsg());
                    updateFragment.setArguments(bundle);
                    updateFragment.show(activity.getFragmentManager(), "");
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i("ApkUpdateAgent", s);
            }
        });

    }
}
