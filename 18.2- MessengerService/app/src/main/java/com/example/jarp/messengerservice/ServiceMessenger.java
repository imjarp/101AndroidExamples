package com.example.jarp.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ServiceMessenger extends Service {


    private Messenger mMessenger;

    private Handler mMessageHandler ;

    public ServiceMessenger() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread handlerThread = new HandlerThread("com.example.jarp.messengerservice.handlerThread");

        handlerThread.start();

        mMessageHandler = new Handler(handlerThread.getLooper(),new MyHandlerCallback());

        mMessenger = new Messenger(mMessageHandler);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessageHandler.getLooper().quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class MyHandlerCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message msg) {


            String message = "Hello i received this message " +  (String) msg.obj;



            Message reply = Message.obtain(null,0,message);


            try {
                msg.replyTo.send(reply);
            } catch (RemoteException e) {
                Log.e("ServiceMessenger","Error sending reply");
            }


            return true;


        }
    }

}
