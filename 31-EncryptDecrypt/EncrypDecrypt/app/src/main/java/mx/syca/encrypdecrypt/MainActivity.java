package mx.syca.encrypdecrypt;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        displayEncriptedText(getData());

        findViewById(R.id.btn_encrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToEncrypt = ((TextView) findViewById(R.id.text_to_encrypt)).getText().toString();

                String password = ((TextView) findViewById(R.id.password)).getText().toString();

                try {
                    String encriptedText = encryptClearText(password.toCharArray(), textToEncrypt);

                    storeData(encriptedText);

                    displayEncriptedText(encriptedText);

                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }


            }
        });


        findViewById(R.id.btn_decrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encriptedText = ((TextView)findViewById(R.id.txt_encripted)).getText().toString();

                if(TextUtils.isEmpty(encriptedText)) {
                    Toast.makeText(MainActivity.this, "Please first encrypt some text", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = ((TextView)findViewById(R.id.password)).getText().toString();

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Password missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String decripted = decryptData(password.toCharArray(),encriptedText);
                    displayEncriptedText(decripted);
                } catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,"Cant decrypt data check your password",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void displayEncriptedText(String encripted){
        ((TextView)findViewById(R.id.txt_encripted)).setText(encripted);
    }


    private  static SecretKey generateKey(char [] password, byte[]salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        int iterations = 100;

        int outputKey = 256;
        SecretKeyFactory secretKeyFactory ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Use compatibility key factory -- only uses lower 8-bits of passphrase chars
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1And8bit");
        } else {
            // Traditional key factory. Will use lower 8-bits of passphrase chars on
            // older Android versions (API level 18 and lower) and all available bits
            // on KitKat and newer (API level 19 and higher).
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        }

        KeySpec keySpec = new PBEKeySpec(password,salt,iterations,outputKey);

        byte [] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();

        return  new SecretKeySpec(keyBytes,"AES");

    }


    public static String encryptClearText(char[] password, String plainText)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException {
        SecureRandom secureRandom = new SecureRandom();

        int saltLength = 8;

        byte salt [] = new byte[saltLength];

        secureRandom.nextBytes(salt);

        SecretKey secretKey = generateKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte [] initVector = new byte[cipher.getBlockSize()];

        secureRandom.nextBytes(initVector);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);

        cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);

        byte [] cipherData = cipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeToString(cipherData,Base64.NO_WRAP|Base64.NO_PADDING) + "]"
             + Base64.encodeToString(initVector,Base64.NO_WRAP|Base64.NO_PADDING) + "]"
             + Base64.encodeToString(salt,Base64.NO_WRAP|Base64.NO_PADDING) ;

    }

    public static String decryptData (char [] password, String encodedData)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        String [] parts = encodedData.split("]");

        byte[] cipherData =  Base64.decode(parts[0], Base64.DEFAULT);
        byte[] initVector =  Base64.decode(parts[1], Base64.DEFAULT);
        byte[] salt =  Base64.decode(parts[2], Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKey secretKey = generateKey(password, salt);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);

        cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);


        return new String(cipher.doFinal(cipherData),"UTF-8");

    }


    public String getData(){

        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);

        return preferenceManager.getString("KEY_DATA_ENCRYPTED","");

    }


    public void storeData(String encryptedData){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences.edit().putString("KEY_DATA_ENCRYPTED",encryptedData).apply();


    }
}
