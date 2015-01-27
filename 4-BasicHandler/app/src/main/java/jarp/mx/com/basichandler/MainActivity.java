package jarp.mx.com.basichandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextSwitcher;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements android.os.Handler.Callback {


    private Handler handlerMainUi;

    private Handler handlerBackground;

    public static final int OperationInBackGround = 10;

    public static final int OperationInMainThread = 20;

    private TextSwitcher mTextSwitcher;

    private final int ONE_SECOND = 1000;

    private TextView textView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        textView.setText("Hello");

        handlerMainUi = new Handler(getMainLooper(),this);

        HandlerThread backgroundThread = new HandlerThread("BackgroundThread");

        backgroundThread.start();

        handlerBackground = new Handler(backgroundThread.getLooper(),this);

        sendMessages();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shut down the Looper thread
        handlerBackground.getLooper().quit();
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

    @Override
    public boolean handleMessage(final Message msg) {

        switch (msg.what)
        {
            case OperationInBackGround :

                //Hello i'm doing something in the background

                MessageText messageText = (MessageText) msg.obj;

                Handler mainUi = messageText.handler;

                mainUi.obtainMessage(OperationInMainThread,messageText.message).sendToTarget();

                break ;

            case OperationInMainThread :
                //Now we are on the UI update!

                final String message = (String) msg.obj;

                textView.setText(message );

                break ;

            default: ;
        }

        return true;
    }


    void sendMessages()
    {

        MessageText messageText = MessageText.createInstance(handlerMainUi,"5");

        Message message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND );


        messageText = MessageText.createInstance(handlerMainUi,"4");

        message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND * 2);

        messageText = MessageText.createInstance(handlerMainUi,"3");

        message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND * 3);

        messageText = MessageText.createInstance(handlerMainUi,"2");

        message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND * 4 );

        messageText = MessageText.createInstance(handlerMainUi,"1");

        message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND * 5);


        messageText = MessageText.createInstance(handlerMainUi,"THANKS!");

        message = handlerBackground.obtainMessage(OperationInBackGround, messageText);

        handlerBackground.sendMessageDelayed(message, ONE_SECOND * 6);



    }


}
