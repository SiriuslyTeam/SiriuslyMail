package edu.sirius.android.siriuslymail;

import android.app.Activity;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        User user = UsersManager.getInstance().getActiveUser();
        //TODO elsi pusto to login else main
        if (user == null) {
            LoginActivity.start(this);
        } else {
            MainActivity.start(this);
        }
        finish();
    }
}
