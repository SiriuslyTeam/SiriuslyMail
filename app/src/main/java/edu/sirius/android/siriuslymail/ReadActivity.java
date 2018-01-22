package edu.sirius.android.siriuslymail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class ReadActivity extends AppCompatActivity {

    final String TAG = "lifecycle_read";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Toolbar toolbar = findViewById(R.id.toolbar_read);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        WebView readFull = findViewById(R.id.read_full);
        readFull.loadData("<b>helloworld</b>", "text/html", "UTF-8");

        Log.d(TAG, "onCreate()");
    }
}
