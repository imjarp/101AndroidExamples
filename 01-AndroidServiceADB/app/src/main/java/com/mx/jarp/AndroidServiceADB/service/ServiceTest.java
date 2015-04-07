package com.mx.jarp.AndroidServiceADB.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceTest extends Service {

    String TAG = ServiceTest.class.getSimpleName();

    public ServiceTest() {
    }


    @Override
    public void onCreate() {
        // The service is being created
        Log.d(TAG,"Hello from service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


        //throw new UnsupportedOperationException("Not yet implemented");

        return null;
    }
}
