package jarp.example.com.localbroadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {


    public static final String LOCAL_BROADCAST_ACTION = "localBrodcast";
    private static final String KEY_MESSAGE= "KEY_MESSAGE";

    private BroadcastReceiver mLocalReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter intentFilter = new IntentFilter(LOCAL_BROADCAST_ACTION);

        mLocalReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String message = intent.getStringExtra(KEY_MESSAGE);


                message = "Hey thanks i received this message " + message;


                Toast.makeText( context,message,Toast.LENGTH_SHORT ).show();

            }
        };

        broadcastManager.registerReceiver(mLocalReceiver, intentFilter);
    }

    public void sendLocalBroadcast(Intent broadcastIntent){
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);

        localBroadcastManager.sendBroadcast(broadcastIntent);

    }


    public void sendMessage( View view ){

        String messsage = ( (EditText) findViewById(R.id.txt_message)).getText().toString();

        sendLocalBroadcast(createIntenteMessage(messsage));

    }

    public static Intent createIntenteMessage(String message){

        Intent intent = new Intent(LOCAL_BROADCAST_ACTION);
        intent.putExtra(KEY_MESSAGE,message);
        return  intent;


    }

}
