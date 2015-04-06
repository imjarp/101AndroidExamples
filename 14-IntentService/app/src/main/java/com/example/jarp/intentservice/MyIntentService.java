package com.example.jarp.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private static final String ACTION_START_SERVICE = "com.example.jarp.intentservice.action.FOO";

    private static final String EXTRA_PARAM2 = "com.example.jarp.intentservice.extra.PARAM2";

    public static final String ACTION_BROADCAST = "com.example.jarp.intentservice.action.send"  ;

    private static final String TAG ="MyIntentService";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startAction(Context context, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_START_SERVICE);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_SERVICE.equals(action)) {
                sendMessageToActivity();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void sendMessageToActivity () {


        Log.d(TAG,"Sleeping 5 seconds ");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(ACTION_BROADCAST));
    }


}
