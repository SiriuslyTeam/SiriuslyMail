package edu.sirius.android.siriuslymail;

import android.app.Activity;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

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
