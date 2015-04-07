package com.example.jarp.parallelexecution;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void  onClickAddFile(View view){

        //Simulate the result when a user pick a media file
        Uri uri = Uri.EMPTY;
        Intent intent = new Intent(this,MediaTranscoder.class);
        intent.setAction(MediaTranscoder.ACTION_TRANSCODE_MEDIA);
        intent.setData(uri);
        intent.putExtra(MediaTranscoder.EXTRA_OUTPUT_TYPE,"Some MP3");
        startService(intent);

    }



}
