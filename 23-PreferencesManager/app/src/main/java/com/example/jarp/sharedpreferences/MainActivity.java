package com.example.jarp.sharedpreferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    SharedPreferences preferences;

    private final String KEY_BOOLEAN = "bool";
    private final String KEY_INT = "int";
    private final String KEY_STRING = "string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setPreferences();

    }

    public void onClickSavePreferences(View view){

        boolean isCheck = ( (CheckBox) findViewById(R.id.chk) ).isChecked();

        int integerVal = ((EditText) findViewById(R.id.edt_integer)).getText().equals("")? 0 : Integer.valueOf(((EditText) findViewById(R.id.edt_integer)).getText().toString());
        String stringVal =  ((EditText)findViewById(R.id.edt_text)).getText().toString();

        SharedPreferences.Editor editor =  preferences.edit();
        editor.putBoolean(KEY_BOOLEAN, isCheck );
        editor.putInt(KEY_INT, integerVal);
        editor.putString(KEY_STRING, stringVal);
        editor.apply();

        Toast.makeText(this,"Values updated",Toast.LENGTH_SHORT).show();

    }

    public void setPreferences(){
        boolean isCheck = preferences.getBoolean(KEY_BOOLEAN, false);
        int integerVal =  preferences.getInt(KEY_INT, Integer.MIN_VALUE);
        String strIntegerVal = integerVal==Integer.MIN_VALUE ? "":String.valueOf(integerVal);
        String stringVal = preferences.getString(KEY_STRING, "");

        ( (CheckBox) findViewById(R.id.chk) ).setChecked(isCheck);
        ((EditText) findViewById(R.id.edt_integer)).setText(strIntegerVal);
        ((EditText)findViewById(R.id.edt_text)).setText(stringVal);
    }

}
