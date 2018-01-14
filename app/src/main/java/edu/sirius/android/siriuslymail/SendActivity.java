package edu.sirius.android.siriuslymail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class SendActivity extends AppCompatActivity {

    final String TAG = "lifecycle_send";

    @Override
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.d(TAG, "onCreate()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
    }

    @Override
    @CreatedBy(author = "Anthony Udovichenko", date = "13.01.2018")
    @LastChangeBy(author = "Anthony Udovichenko", date = "13.01.2018")
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

}
