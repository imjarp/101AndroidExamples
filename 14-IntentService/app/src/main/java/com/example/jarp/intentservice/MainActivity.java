package com.example.jarp.intentservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(myLocalReceiver, new IntentFilter(MyIntentService.ACTION_BROADCAST), null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(myLocalReceiver);
    }

    public void onClickStartService(View view){
        MyIntentService.startAction(this,"");
    }


    BroadcastReceiver myLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stop = "";

            Toast.makeText(MainActivity.this,"onReceived call by intent service",Toast.LENGTH_SHORT).show();





        }
    };


}
