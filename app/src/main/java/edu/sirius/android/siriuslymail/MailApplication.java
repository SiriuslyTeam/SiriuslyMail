package edu.sirius.android.siriuslymail;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Work on 21.01.2018.
 */

public class MailApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, PostService.class);
        startService(intent);
    }
}
