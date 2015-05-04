package jarp.example.com.enabledisablebroadcast;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    boolean isEnable = true;
    int isFirstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public  void sendMessage(View view){

        String message =( (EditText) findViewById(R.id.message)).getText().toString();


        Intent intentMessage = new Intent(this,MyReceiver.class);
        intentMessage.setAction(MyReceiver.ACTION);
        intentMessage.putExtra( MyReceiver.MESSAGE_PARAM, message );

        sendBroadcast(intentMessage);


    }


    public void onToggle(View view){

        isEnable =!isEnable;

        if(isEnable && isFirstTime!=0 ){
            enableBroadcastReceiver();
        }else{
            disableBroadcastReceiver();
        }
        isFirstTime++;





    }

    public void enableBroadcastReceiver(){
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting( new ComponentName(this,MyReceiver.class),
                                                                                      PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                                                                      PackageManager.DONT_KILL_APP );


    }


    public void disableBroadcastReceiver(){

        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting( new ComponentName(this,MyReceiver.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED ,
                PackageManager.DONT_KILL_APP );

    }


}
