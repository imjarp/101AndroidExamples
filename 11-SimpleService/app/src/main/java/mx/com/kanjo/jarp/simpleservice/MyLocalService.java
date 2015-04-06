package mx.com.kanjo.jarp.simpleservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyLocalService extends Service {

    private LocalBinder mLocalBinder = new LocalBinder();

    String TAG = "MyLocalService";

    public MyLocalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public void doLongRunningOperation(){

        Log.d(TAG, "Hello I'm running in the Local Service");

    }

    public class LocalBinder extends Binder{
        public MyLocalService getService(){
            return MyLocalService.this;

        }

    }
}
