package edu.sirius.android.siriuslymail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static edu.sirius.android.siriuslymail.PostService.SUCCESS_LOGIN;
import static edu.sirius.android.siriuslymail.PostService.SUCCESS_SEND;
import static edu.sirius.android.siriuslymail.PostServiceActions.LOGIN_ACTION;

public class SendActivity extends AppCompatActivity {

    final String TAG = "lifecycle_send";
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getExtras().getBoolean(SUCCESS_SEND);
            if (success) {
                Toast toast = Toast.makeText(getApplicationContext(),"Message was successfuly sent",Toast.LENGTH_LONG);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

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
                EditText to = findViewById(R.id.toEmail);
                EditText copy = findViewById(R.id.copyEmail);
                EditText subject = findViewById(R.id.subjectEmail);
                EditText body = findViewById(R.id.send_full);
                Message message = new Message();
                message.body = body.toString();
                message.subject = subject.toString();
                message.to = to.toString();
                message.from = UsersManager.getInstance().getActiveUser().getEmail();
                message.copy = copy.toString();
                PostServiceActions.postMessage(SendActivity.this, message);
                finish();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(LOGIN_ACTION));



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
