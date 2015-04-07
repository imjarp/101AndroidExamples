package mx.com.jarp.audioasset;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements SoundPool.OnLoadCompleteListener{

    HashMap<String,Integer> soundEffects = null;

    SoundPool mSoundPool;

    private final int mVolumeMax = 10;

    public void playSoundFX(View view)
    {

        String stop = "";
        int idx  = 0;


        switch (view.getId()) {
            case R.id.mButton1 : idx = 1; break;
            case R.id.mButton2 : idx = 2; break;
            case R.id.mButton3 : idx = 3; break;
            case R.id.mButton4 : idx = 4; break;
            case R.id.mButton5 : idx = 5; break;
            case R.id.mButton6 : idx = 6 ; break;
            default: break;


        }

        if( idx > 0 )
            mSoundPool.play(idx, mVolumeMax, mVolumeMax, 1, 0, 1.0f );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        try {
            soundEffects = loadSoundEffects(mSoundPool);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if( null == soundEffects )
        {
            return ;
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(null != mSoundPool)
        {

            for(Map.Entry<String,Integer> item : soundEffects.entrySet())
            {
                mSoundPool.unload(item.getValue());
            }

            mSoundPool.release();

            mSoundPool = null;
        }
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


    /*
        This sounds were taken from

            @link http://www.lunerouge.org/spip/rubrique.php3?id_rubrique=49

        Thanks to Lionel Allorge
     */
    public HashMap<String,Integer> loadSoundEffects(SoundPool soundPool) throws IOException
    {
        AssetManager assetManager = getAssets();

        String [] soundEffectFiles = assetManager.list("soundfx");

        HashMap<String,Integer> soundEffectMap = new HashMap<>(soundEffectFiles.length);

        for(String soundEffectFile : soundEffectFiles)
        {
            AssetFileDescriptor fileDescriptor = assetManager.openFd("soundfx/" + soundEffectFile);

            int id = soundPool.load(fileDescriptor,1);

            soundPool.setOnLoadCompleteListener(this);

            soundEffectMap.put(soundEffectFile,id);

            fileDescriptor.close();

        }

        return soundEffectMap;

    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

        String stop ="";

        if(status>0)
            stop = String.valueOf(sampleId);

    }
}
