package edu.sirius.android.siriuslymail;

import android.app.Application;

public class MailApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataSource.init(this);
        UsersManager.initLogin(this);
    }
}
