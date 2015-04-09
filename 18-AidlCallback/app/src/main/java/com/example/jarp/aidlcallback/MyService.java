package com.example.jarp.aidlcallback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service {


    ArrayList<AidlCallback> mCallBacks;

    public MyService() {

        mCallBacks = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
      return mBinder;
    }


    private final ApiInterfaceV1.Stub mBinder = new ApiInterfaceV1.Stub() {
        @Override
        public boolean isPrime(long value) throws RemoteException {
            return false;
        }

        @Override
        public void getAllDataSince(long timeStamp, CustomData[] result) throws RemoteException {

        }

        @Override
        public void storeData(CustomData data) throws RemoteException {

            mCallBacks.get(0).onDataUpdated(null);

        }

        @Override
        public void addCallback(final AidlCallback callback) throws RemoteException {

                mCallBacks.add(callback);

            callback.asBinder().linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    mCallBacks.remove(callback);
                }
            }, 0 );
        }
    };

}
