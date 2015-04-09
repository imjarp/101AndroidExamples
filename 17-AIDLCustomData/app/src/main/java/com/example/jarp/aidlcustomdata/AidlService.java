package com.example.jarp.aidlcustomdata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Date;

public class AidlService extends Service {
    public AidlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean isPrimeImp(long value){
        return true;
    }

    public CustomData[] getData(Date date){

        return new CustomData[]{
                new CustomData(),
                new CustomData(),
        };

    }

    private void storeDataImpl(CustomData data) {

        //Data saved

    }



    private final ApiInterfaceV1.Stub mBinder = new ApiInterfaceV1.Stub() {


        @Override
        public boolean isPrime(long value) throws RemoteException {
            return isPrimeImp(value);
        }

        @Override
        public void getAllDataSince(long timeStamp, CustomData[] result) throws RemoteException {

            result = getData(new Date(timeStamp));

        }

        @Override
        public void storeData(CustomData data) throws RemoteException {
            storeDataImpl(data);
        }


    };




}
