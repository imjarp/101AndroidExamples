package com.example.jarp.messengerservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements ServiceConnection {

    Handler mReplyHandler ;
    Messenger mRemoteMesseger;
    Messenger mReplyMesenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        HandlerThread mHandlerThread = new HandlerThread("handlerClientThread");

        mHandlerThread.start();

        mReplyHandler = new Handler(mHandlerThread.getLooper(), new MyReplyCallback());

        mReplyMesenger = new Messenger(mReplyHandler);


    }

    @Override
    protected void onResume() {
        super.onResume()  ;

        Intent intent = new Intent("com.example.jarp.messengerservice.action");

        bindService(intent,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    public  void sendMessage(View view){

        String messageText =( (EditText) findViewById(R.id.et_message)).getText().toString();

        Message message = Message.obtain();

        message.what = 0 ;

        message.obj =messageText ;

        message.replyTo = mReplyMesenger;

        try {
            mRemoteMesseger.send(message);
        } catch (RemoteException e) {
            Log.e("MainActivity","Error sending message");
        }





    }

    private class MyReplyCallback implements Handler.Callback{


        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, (CharSequence) msg.obj,Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mRemoteMesseger = new Messenger(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRemoteMesseger = null;

    }
}
