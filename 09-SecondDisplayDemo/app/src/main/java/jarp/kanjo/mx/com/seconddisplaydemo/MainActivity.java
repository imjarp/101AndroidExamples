package jarp.kanjo.mx.com.seconddisplaydemo;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;

/*
    To simulate multiple screen :
        Settings -> Developer Options -> Advanced ->Simulate Secondary Display commands

        image example in :
        /SecondDisplayDemo/app/Example2ndDisplay.png

 */
public class MainActivity extends Activity {

    SecondDisplayPresentation mSecondDisplay ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSecondDisplay();
    }

    private void setupSecondDisplay() {

        DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);

        Display defaultDisplay = displayManager.getDisplay(Display.DEFAULT_DISPLAY);

        Display [] presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);

        if( presentationDisplays.length > 0 ){
            for(Display presentationDisplay : presentationDisplays){
                if(presentationDisplay.getDisplayId() != defaultDisplay.getDisplayId()){
                    SecondDisplayPresentation presentation = new SecondDisplayPresentation(this,presentationDisplay);
                    presentation.show();
                    mSecondDisplay = presentation;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mSecondDisplay != null){
            mSecondDisplay.cancel();
        }
    }


    class SecondDisplayPresentation extends Presentation{

        public SecondDisplayPresentation(Context outerContext, Display display) {
            super(outerContext, display);
            setContentView(R.layout.second_screen_layout);
        }

    }


}
