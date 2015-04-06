package mx.com.kanjo.jarp.simpleservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;


public class MainActivity extends Activity implements ServiceConnection {


    private  MyLocalService mService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent bindIntent = new Intent(this,MyLocalService.class);
        bindService(bindIntent,this,BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if( mService!= null ){
            unbindService(this);
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mService =( ( MyLocalService.LocalBinder ) service ).getService();

        mService.doLongRunningOperation();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;

    }
}
