package com.example.jarp.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    private LocalBinder mLocalBinder = new LocalBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  mLocalBinder;
    }

    public class LocalBinder extends Binder {

        public MyService getService(){

            return  MyService.this;

        }

    }
}
