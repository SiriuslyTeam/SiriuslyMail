package edu.sirius.android.siriuslymail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import javax.mail.Multipart;

public class ReadActivity extends AppCompatActivity {

    final String TAG = "lifecycle_read";
    public static final String READ_MESSAGE_ACTION = "READ_MESSAGE_ACTION";
    public static final String MESSAGE = "MESSAGE";

    Message message;


    public static void start(Activity activity, Message message) {
        Intent intent = new Intent(activity, ReadActivity.class)
                .setAction(READ_MESSAGE_ACTION)
                .putExtra(MESSAGE, message);
        activity.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
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

        Intent intent = getIntent();
        Message message = (Message) intent.getSerializableExtra(MESSAGE);

        TextView from = findViewById(R.id.from);
        String fromEncoded = "";
        try {
            String needToDecode = message.from;
            if (needToDecode.startsWith("=?utf-8?B?")) {
                String[] parts = needToDecode.split("\\?=");
                parts[0] = parts[0].replace("=?utf-8?B?", "");
                byte[] data;
                if (parts.length > 1) {
                    parts[1] = parts[1].replace("?=", "");
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8") + parts[1];
                } else {
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8");
                }
            } else if (needToDecode.startsWith("=?UTF-8?B?")) {
                String[] parts = needToDecode.split("\\?=");
                parts[0] = parts[0].replace("=?UTF-8?B?", "");
                byte[] data;
                if (parts.length > 1) {
                    parts[1] = parts[1].replace("?=", "");
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8") + parts[1];
                } else {
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8");
                }
            } else if (needToDecode.startsWith("=?utf-8?b?")) {
                String[] parts = needToDecode.split("\\?=");
                parts[0] = parts[0].replace("=?utf-8?b?", "");
                byte[] data;
                if (parts.length > 1) {
                    parts[1] = parts[1].replace("?=", "");
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8") + parts[1];
                } else {
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8");
                }
            } else if (needToDecode.startsWith("=?utf-8?Q?")) {
                String[] parts = needToDecode.split("\\?=");
                parts[0] = parts[0].replace("=?utf-8?Q?", "");
                byte[] data;
                if (parts.length > 1) {
                    parts[1] = parts[1].replace("?=", "");
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8") + parts[1];
                } else {
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8");
                }
            } else if (needToDecode.startsWith("=?UTF-8?q?")) {
                String[] parts = needToDecode.split("\\?=");
                parts[0] = parts[0].replace("=?UTF-8?q?", "");
                byte[] data;
                if (parts.length > 1) {
                    parts[1] = parts[1].replace("?=", "");
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8") + parts[1];
                } else {
                    data = Base64.decode(parts[0], Base64.DEFAULT);
                    fromEncoded = new String(data, "UTF-8");
                }
            } else {
                fromEncoded = message.from;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        from.setText(Html.fromHtml("<b>From: </b>" + fromEncoded));

        TextView to = findViewById(R.id.to);
        to.setText(Html.fromHtml("<b>To: </b>" + message.to));

        TextView copy = findViewById(R.id.copy);
        copy.setText(Html.fromHtml("<b>Copy: </b>" + message.copy));

        TextView subject = findViewById(R.id.subject);
        subject.setText(Html.fromHtml("<b>Subject: </b>" + message.subject));

        WebView readFull = findViewById(R.id.read_full);
        readFull.loadDataWithBaseURL(null, message.body, "text/html", "UTF-8", null);
//        readFull.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

        Log.d(TAG, "onCreate()");
    }
}
