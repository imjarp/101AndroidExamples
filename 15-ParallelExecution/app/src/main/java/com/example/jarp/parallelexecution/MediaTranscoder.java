package com.example.jarp.parallelexecution;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaTranscoder extends Service {


    private static final int NOTIFICATION_ID = 1001;

    public static final String ACTION_TRANSCODE_MEDIA = " com.example.jarp.parallelexecution.TRANSCODE_MEDIA";

    public static final String EXTRA_OUTPUT_TYPE = "outputType";

    private ExecutorService mExecutorService ;

    private int mRunningJobs= 0 ;

    private final Object mLock = new Object();

    private boolean mIsForeground = false;

    public MediaTranscoder() {
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mExecutorService = Executors.newCachedThreadPool();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if(ACTION_TRANSCODE_MEDIA.equals(action)){

            String outputType = intent.getStringExtra(EXTRA_OUTPUT_TYPE);
            synchronized (mLock){

                TranscodeRuunable transcodeRuunable = new TranscodeRuunable(intent.getData(),outputType);
                mExecutorService.execute(transcodeRuunable);
                mRunningJobs++;
                startForegroundIfNeeded();
            }
        }
        return  START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void startForegroundIfNeeded(){

        if(!mIsForeground){

            Notification notificationCompat = createBuiderNotification().build();
            startForeground(NOTIFICATION_ID,notificationCompat);
            mIsForeground = true;

        }
    }

    private void stopForegroundIfAllDone(){

        if(mRunningJobs == 0 &&  mIsForeground){
            stopForeground(true);
            mIsForeground = false;

        }

    }

    private NotificationCompat.Builder createBuiderNotification(){


        return  new NotificationCompat.Builder(this)
                .setContentText("Notification from Service")
                .setContentText("Content Text Service")
                .setSmallIcon(R.mipmap.ic_launcher);


    }

    //Simulate a class that will transform MP3 to WAV
    private class TranscodeRuunable implements Runnable{


        private Uri mInData ;
        private String mOutputType ;

        public TranscodeRuunable(Uri mInData, String mOutputType) {
            this.mInData = mInData;
            this.mOutputType = mOutputType;
        }

        @Override
        public void run() {


            //Simulate work
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //after the work is done decrease mRunningJobs

            synchronized (mLock){

                mRunningJobs--;
                stopForegroundIfAllDone();

            }



        }
    }


}
