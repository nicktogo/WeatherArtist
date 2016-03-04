package com.baelight.weatherartist.service;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.baelight.weatherartist.R;

import java.io.File;

public class AppUpdateService extends Service {

    private long apkDownloadId;
    private BroadcastReceiver receiverDownloadCompleted;

    public AppUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("Update", "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(intent.getStringExtra(getString(R.string.new_update_url)));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(getString(R.string.app_name))
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getString(R.string.apk_name));
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                apkDownloadId = downloadManager.enqueue(request);
            }
        }).start();

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiverDownloadCompleted = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (apkDownloadId == downloadId) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(apkDownloadId);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int fileStatusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int fileStatus = cursor.getInt(fileStatusIdx);
                        if (fileStatus == DownloadManager.STATUS_SUCCESSFUL) {
                            int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                            String fileName = cursor.getString(fileNameIdx);
                            Intent installApk = new Intent(Intent.ACTION_VIEW);
                            installApk.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                            installApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(installApk);
                            unregisterReceiver(receiverDownloadCompleted);
                            stopSelf();
                        }
                    }
                    cursor.close();
                }
            }
        };

        registerReceiver(receiverDownloadCompleted, filter);

        return super.onStartCommand(intent, flags, startId);
    }


}
