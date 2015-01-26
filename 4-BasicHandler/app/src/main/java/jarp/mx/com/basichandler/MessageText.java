package jarp.mx.com.basichandler;

import android.os.Handler;

/**
 * Created by JARP on 1/26/15.
 */
public class MessageText {

    Handler handler;

    String message;

    private MessageText(Handler handler , String message)
    {
        this.handler = handler;
        this.message = message;
    }

    public static MessageText createInstance(Handler handler , String message)
    {
        return  new MessageText(handler , message);
    }
}
