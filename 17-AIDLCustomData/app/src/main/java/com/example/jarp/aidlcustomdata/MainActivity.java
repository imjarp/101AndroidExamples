package com.example.jarp.aidlcustomdata;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements ServiceConnection{

    private ApiInterfaceV1 mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent n = new Intent("com.example.jarp.aidlcustomdata.action");
        bindService(n, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);

    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        String stop = "";
        mService = ApiInterfaceV1.Stub.asInterface(service);


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;

    }

    public void onClickCallPrime(View view){

        try {
            boolean isPrime= mService.isPrime(2L);
            Toast.makeText(this,"Result " + isPrime , Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
