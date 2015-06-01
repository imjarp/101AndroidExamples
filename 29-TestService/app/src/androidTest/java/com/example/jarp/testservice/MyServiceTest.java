package com.example.jarp.testservice;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

/**
 * Created by JARP on 01/06/15.
 */
public class MyServiceTest  extends ServiceTestCase<MyService>{


    public MyServiceTest() {
        super(MyService.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setupService();
    }


    public void testBinder () throws Exception{

        Intent serviceIntent = new Intent(getContext(),MyService.class);

        IBinder binder = bindService(serviceIntent);

        assertTrue( binder instanceof MyService.LocalBinder );

        MyService myService = ((MyService.LocalBinder)binder).getService();

        assertSame(myService,getService());

    }

    @Override
    protected void tearDown() throws Exception {
        shutdownService();
        super.tearDown();
    }


}
