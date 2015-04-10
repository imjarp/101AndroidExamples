package com.example.jarp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChargeConnectedListener extends BroadcastReceiver {
    public ChargeConnectedListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String getAction = intent.getAction();
        Intent mIntent ;

        if(Intent.ACTION_POWER_CONNECTED.equals(getAction)){

            mIntent  = new Intent(context, MyService.class);
            mIntent.setAction(getAction);
            context.startService(mIntent);


        }else if (Intent.ACTION_POWER_DISCONNECTED.equals(getAction)){

            mIntent  = new Intent(context, MyService.class);
            mIntent.setAction(getAction);
            context.startService(mIntent);

        }



    }
}
