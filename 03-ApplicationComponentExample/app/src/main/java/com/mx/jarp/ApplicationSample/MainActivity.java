package com.mx.jarp.ApplicationSample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivityApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication app = (MyApplication) getApplication();

        Log.d(TAG, app.readMyGlobalVariable());

        launchThreads(app);


    }

    private void launchThreads(final MyApplication app) {

        for ( int i = 0 ; i <= 5; i++)
        {
            final int idx = i;
            new Thread() {
                public void run()
                {
                    if( idx%2 == 0)
                    {
                        Log.d(TAG, "Reading value " + app.readMyGlobalVariable());
                    }
                    else
                    {
                        String newValue = "New value" + String.valueOf(idx);
                        app.updateMyGlobalVariable(newValue);
                        Log.d(TAG, app.readMyGlobalVariable());
                    }
                }
            }.start();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
