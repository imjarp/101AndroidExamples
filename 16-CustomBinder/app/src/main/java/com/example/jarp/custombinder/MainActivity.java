package com.example.jarp.custombinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements ServiceConnection {

    IBinder mBinder;

    private static final String TAG = "MainActivity_Service";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent bindIntent = new Intent(this,MyService.class);

        bindService(bindIntent,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinder = null;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mBinder  = service;

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mBinder = null;
    }

    public void callBinder(View view){
        String r="";
        try {
            r = performCustomBinderTransaction(mBinder);
        } catch (RemoteException e) {
            Log.d(TAG, "ERROR " + e);
        }

        if(r != ""){
            Toast.makeText(this,r,Toast.LENGTH_SHORT).show();
        }

    }

    public String performCustomBinderTransaction(IBinder binder) throws RemoteException {
        String arg0  = "arg0";
        int arg1 = 1;
        float arg2 =2;

        Parcel request = Parcel.obtain();
        Parcel reponse= Parcel.obtain();

        request.writeString(arg0);
        request.writeInt(arg1);
        request.writeFloat(arg2);

        binder.transact(IBinder.FIRST_CALL_TRANSACTION, request, reponse, 0);

        String result = reponse.readString();

        request.recycle();
        reponse.recycle();

        return result;

    }
}
