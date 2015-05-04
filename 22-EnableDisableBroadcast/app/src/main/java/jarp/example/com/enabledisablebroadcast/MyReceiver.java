package jarp.example.com.enabledisablebroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public final static String ACTION ="jarp.example.com.enabledisablebroadcast.SEND_MESSAGE";

    public final static String MESSAGE_PARAM ="MESSAGE_PARAM ";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        if(ACTION.equals(intent.getAction())) {

            String message = intent.getStringExtra(MESSAGE_PARAM);

            Toast.makeText(context,"I received a message " + message, Toast.LENGTH_SHORT).show();

            Log.d("MyReceiver",message);

        }

    }
}
