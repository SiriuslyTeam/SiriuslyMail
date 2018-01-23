package edu.sirius.android.siriuslymail;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        //TODO elsi pusto to login else main

        User.getInstance().update();
    }
}
