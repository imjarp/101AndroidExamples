package mx.com.kanjo.jarp.simplebinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements ServiceConnection , MyLocalService.Callback {


    MyLocalService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentBindService = new Intent(this,MyLocalService.class);

        bindService(intentBindService, this, BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mService !=  null){
            mService.setCallBack(null);
            mService = null;
        }
    }


    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted(MyComplexResult complexResult) {

        Toast.makeText(this,"The service has finished the operation in the background :)",Toast.LENGTH_SHORT).show();
        findViewById(R.id.btn_service).setEnabled(true);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService =   ( (MyLocalService.LocalBinder) service).getService();
        mService.setCallBack(this);
        findViewById(R.id.btn_service).setEnabled(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;

    }


    public void onClickService(View view){

        MyComplexDataObject request = new MyComplexDataObject();
        mService.doLongRunningOperation( request);

    }
}
