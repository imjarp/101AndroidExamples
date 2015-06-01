package com.example.jarp.testactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.view.View;

/**
 * Created by JARP on 29/05/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {


    Intent mService;
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testIfButtonHasClickListener(){
        //startActivity(new Intent(Intent.ACTION_MAIN),null,null);

        //View testShowButton = getActivity().findViewById(R.id.btnShowSomething);

        //assertTrue(testShowButton.hasOnClickListeners());

    }

    public void testIfButtonStartService(){


        MyMockContext myMockContext = new MyMockContext( getInstrumentation().getTargetContext() );
       //setActivityContext( myMockContext);

        //startActivity(new Intent(Intent.ACTION_MAIN), null, null);

        Activity x = getActivity();
        View testShowButton = getActivity().findViewById(R.id.btnShowSomething);


        assertEquals("Wrong Intent action for starting service!",
                "startBackgroundJob", mService.getAction());


    }

    public class MyMockContext extends ContextWrapper{

        public MyMockContext(Context base) {
            super(base);
        }

        @Override
        public ComponentName startService(Intent service) {
            mService = service ;
            return  new ComponentName("com.example.jarp.testactivity","MyService");
        }
    }



}
