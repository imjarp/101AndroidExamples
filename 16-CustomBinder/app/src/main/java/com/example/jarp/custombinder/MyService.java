package com.example.jarp.custombinder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MyService extends Service {

    CustomBinder binder = new CustomBinder();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class CustomBinder extends Binder{


        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            String arg0  = data.readString();

            int arg1 = data.readInt();

            float arg2 = data.readFloat();

            String result = "Result from BINDER";

            reply.writeString(result);

            return  true;
        }
    }
}
