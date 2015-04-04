package com.mx.kanjo.jarp.toggleactivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class TermsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
    }


    public void onClickAccepted(View view){
        toggleActivities();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void toggleActivities() {

        PackageManager packageManager = getPackageManager();

        packageManager.setComponentEnabledSetting( new ComponentName( this,MainActivity.class) ,
                                                                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                                                            PackageManager.DONT_KILL_APP);

        packageManager.setComponentEnabledSetting( new ComponentName( this,TermsActivity.class) ,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}
