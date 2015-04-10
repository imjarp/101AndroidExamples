package com.example.jarp.broadcastreceiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements ServiceConnection,MyService.MyCallback {

    MyService mService ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this,MyService.class);
        bindService(i,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mService =  ( ( MyService.MyLocalBinder ) service ).getService();

        mService.setCallBack(this);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;

    }

    @Override
    public void onPowerConnected() {
        Toast.makeText(MainActivity.this,"OnPowerConnected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPoweDisconnected() {
        Toast.makeText(MainActivity.this,"OnPowerDisconnected",Toast.LENGTH_SHORT).show();

    }
}
