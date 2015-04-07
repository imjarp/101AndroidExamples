package com.mx.jarp.ApplicationSample;

import android.app.Application;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by JARP on 1/24/15.
 */
public class MyApplication extends Application {


    ReentrantReadWriteLock mLock;

    private String myGlobalVariable ;


    @Override
    public void onCreate() {
        super.onCreate();
        mLock = new ReentrantReadWriteLock();
        myGlobalVariable = "Empty";
    }

    public String readMyGlobalVariable()
    {
        ReentrantReadWriteLock.ReadLock readLock = mLock.readLock();
        try
        {
            readLock.lock();
            return myGlobalVariable;
        }
        finally {
            readLock.unlock();

        }
    }

    public void updateMyGlobalVariable(String newValue)
    {
        ReentrantReadWriteLock.WriteLock writeLock = mLock.writeLock();

        try
        {
            writeLock.lock();
            myGlobalVariable = newValue;
        }
        finally {
            writeLock.unlock();
        }
    }


}
