package edu.sirius.android.siriuslymail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SendActivity extends AppCompatActivity {

    final String TAG = "lifecycle_send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = findViewById(R.id.toolbar_send);
        toolbar.setTitle("Send email");
        setSupportActionBar(toolbar);

        Button send = findViewById(R.id.send_mail_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO place send action here
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.d(TAG, "onCreate()");
    }
}
