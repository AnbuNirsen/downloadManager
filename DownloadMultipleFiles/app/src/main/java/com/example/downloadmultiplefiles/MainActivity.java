package com.example.downloadmultiplefiles;

import android.app.DownloadManager;
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
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
DownloadManager downloadManager;
Uri downloadUri;
long refid;
List<Long> list = new ArrayList<>();
Button single,multiple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        single = findViewById(R.id.single);
        multiple = findViewById(R.id.multiple);
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadUri = Uri.parse("http://solutionsproj.net/software/Programming%20Kotlin.pdf");
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Hio");
                request.setDescription("Download");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"SignleFile.pdf");
                refid = downloadManager.enqueue(request);
                list.add(refid);
            }
        });

        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<3;i++){
                    downloadUri = Uri.parse("http://solutionsproj.net/software/Programming%20Kotlin.pdf");
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
                    request.setVisibleInDownloadsUi(true);
                    request.setTitle("Download file");
                    request.setDescription("hello  and welcome");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/DownloadMultiplefilesTest/"+"abc.pdf");

                    refid = downloadManager.enqueue(request);
                    list.add(refid);
                }
            }
        });




        registerReceiver(oncompleted,new IntentFilter(new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)));
    }

    BroadcastReceiver oncompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            list.remove(referenceId);
            if(list.size() == 0){
                NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Ashok")
                        .setContentText("All Downloads completed")
                        ;
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(12,myBuilder.build());


            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(oncompleted);
    }
}
