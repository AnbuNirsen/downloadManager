package com.example.downloadmanagerandroidsample;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
DownloadManager downloadManager;
Button downloadfile;
Uri DownloadUri;
long refid;
List<Long> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadfile = findViewById(R.id.downloadfile);
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadUri = Uri.parse("https://www.adobe.com/support/products/enterprise/knowledgecenter/media/c4611_sample_explain.pdf");
                DownloadManager.Request request = new DownloadManager.Request(DownloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle("Ashok sennan");
                request.setDescription("hi hello welcome...");
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/DOWNLMANAGERTEST/"  + "/" + "Sample" + ".pdf");
                refid = downloadManager.enqueue(request);
                list.add(refid);
            }
        });
        registerReceiver(onCompleted,new IntentFilter(new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)));
    }
    BroadcastReceiver onCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long refid = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            list.remove(refid);
            if(list.size()==0){
                Log.e("INSIDE", "" + refid);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Downloaded")
                                .setContentText("Download completed");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onCompleted);
    }
}
