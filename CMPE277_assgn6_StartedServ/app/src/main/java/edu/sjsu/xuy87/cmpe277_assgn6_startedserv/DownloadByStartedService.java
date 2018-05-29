package edu.sjsu.xuy87.cmpe277_assgn6_startedserv;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.IBinder;

import android.widget.Toast;



public class DownloadByStartedService extends Service{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String[] url = {
                "http://www.cisco.com/c/dam/en_us/about/annual-report/2016-annual-report-full.pdf",
                "http://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf",
                "http://www.cisco.com/web/strategy/docs/gov/everything-for-cities.pdf",
                "http://www.verizonenterprise.com/resources/reports/rp_DBIR_2017_Report_execsummary_en_xg.pdf",
                "http://www.verizonenterprise.com/resources/reports/rp_DBIR_2017_Report_en_xg.pdf"
        };
        String[] destFiles = new String[url.length];

        for(int i = 0; i< url.length; i++){
            destFiles[i] = url[i].substring(url[i].lastIndexOf("/")+1);
            downloadHelper(url[i], destFiles[i]);
        }
        Toast.makeText(this, "Download complete!", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);

    }

    public void downloadHelper(String url, String destFile) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String tmp =getExternalFilesDir("MyFolder").toString();
        String dir = tmp.substring(tmp.indexOf("0")+1);
        request.setDestinationInExternalPublicDir(dir, destFile);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(this, "Start downloading "+destFile + " ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
