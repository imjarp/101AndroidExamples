package com.example.jarp.broadcastreceiver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    MyCallback mCallback;
    MyLocalBinder mBinder ;

    interface MyCallback{

        void onPowerConnected();
        void onPoweDisconnected();
    }

    public MyService() {
        mBinder = new MyLocalBinder();
    }


    public void setCallBack(MyCallback callBack)
    {
        mCallback = callBack;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(intent.getAction() == Intent.ACTION_POWER_CONNECTED){
            if(mCallback!=null){
                mCallback.onPowerConnected();

            }
        }

        if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED){
            if(mCallback!=null){
                mCallback.onPoweDisconnected();

            }
        }

        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return true;
    }



    public  class MyLocalBinder extends Binder{

        public MyService getService(){
            return MyService.this;
        }


    }

}
