package com.example.jarp.aidlcallback;

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


public class MainActivity extends Activity  implements ServiceConnection{

    ApiInterfaceV1 mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent n = new Intent("com.example.jarp.aidlcallback.action");
        bindService(n, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }


    public void onClickCall(View view){

        try {
            mService.storeData(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

            mService = ApiInterfaceV1.Stub.asInterface(service);

        try {
            mService.addCallback(mAidlCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;

    }

    private AidlCallback.Stub mAidlCallback = new AidlCallback.Stub() {
        @Override
        public void onDataUpdated(CustomData[] data) throws RemoteException {
            Toast.makeText(MainActivity.this, "Data was updated", Toast.LENGTH_SHORT).show();
        }
    };
}
