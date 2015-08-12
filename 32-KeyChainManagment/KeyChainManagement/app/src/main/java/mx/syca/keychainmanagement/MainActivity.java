package mx.syca.keychainmanagement;

import android.content.Intent;
import android.security.KeyChain;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {



    private static final int INSTALL_CERT_CODE = 1001;

    private static final String CERT_FILENAME = "MyKeyStore.pfx";

    private static final String CERTIFICATE_NAME="MyCertificate";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==INSTALL_CERT_CODE)
        if(requestCode == RESULT_OK){

            //Certificated succesfully installed;
            Toast.makeText(this,"Congrats certificate install",Toast.LENGTH_SHORT).show();
        }
        else{

            //User cancelled certificate installation
            Toast.makeText(this,"Dismiss install",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void doInstallCertificate(View view){
        byte[] certData =  readFile(CERT_FILENAME);
        Intent installCert = KeyChain.createInstallIntent();
        installCert.putExtra(KeyChain.EXTRA_NAME,CERTIFICATE_NAME);
        installCert.putExtra(KeyChain.EXTRA_PKCS12,certData);
        startActivityForResult(installCert,INSTALL_CERT_CODE);

    }

    private byte[] readFile(String certFilename) {
        byte [] buffer = null;
        try {
            BufferedInputStream bufferedInputStream  =
                    new BufferedInputStream( getResources().getAssets().open(certFilename));

            buffer  = new byte[bufferedInputStream.available()];

            bufferedInputStream.read(buffer);

            bufferedInputStream.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return  buffer;

    }
}
