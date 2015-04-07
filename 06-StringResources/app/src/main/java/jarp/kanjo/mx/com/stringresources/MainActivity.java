package jarp.kanjo.mx.com.stringresources;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.text_welcome_message)).
                setText(getString(R.string.welcome_message, "Adrian"));


        setTextMessage( (TextView) findViewById(R.id.mtext_1), 0);

        setTextMessage((TextView) findViewById(R.id.mtext_2), 1);

        setTextMessage((TextView) findViewById(R.id.mtext_3), 2);

        setTextMessage((TextView) findViewById(R.id.mtext_4), 5);

        setTextMessage((TextView) findViewById(R.id.mtext_5), 10);

        Spinner spinPlant = (Spinner) findViewById(R.id.spin_planet);

        String [] planets = getResources().getStringArray(R.array.planets);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,android.R.id.text1,planets);

        spinPlant.setAdapter(adapter);

    }


    public void setTextMessage(TextView textView, int numCourse)
    {
        Resources res = getResources();

        String message = res.getQuantityString(R.plurals.courses,numCourse,numCourse);

        textView.setText(message);

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
}
