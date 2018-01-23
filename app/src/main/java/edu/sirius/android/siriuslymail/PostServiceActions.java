package edu.sirius.android.siriuslymail;

import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static edu.sirius.android.siriuslymail.IntentConstants.EMAIL;
import static edu.sirius.android.siriuslymail.IntentConstants.FOLDER;
import static edu.sirius.android.siriuslymail.IntentConstants.IMAP_HOST;
import static edu.sirius.android.siriuslymail.IntentConstants.PASSWORD;
import static edu.sirius.android.siriuslymail.IntentConstants.SMTP_HOST;

public class PostServiceActions {
    public static final String GET_MESSAGES_ACTION = "GET_MESSAGES_ACTION";
    public static final String LOGIN_ACTION = "LOGIN_ACTION";
    public static final String POST_MESSAGE = "POST_MESSAGE";
    public static final String RETURN_RESULT = "RETURN_RESULT";

    private PostServiceActions() {
        // no instance
    }

    public static void attemptLogin(Context context, String email, String password, String imapHost, String smtpHost) {
        Intent intent = new Intent(context, PostService.class)
                .setAction(LOGIN_ACTION)
                .putExtra(IMAP_HOST, imapHost)
                .putExtra(SMTP_HOST, smtpHost)
                .putExtra(PASSWORD, password)
                .putExtra(EMAIL, email);
        context.startService(intent);
    }

    public static void getMessages(Context context, String folder) {
        Intent intent = new Intent(context, PostService.class)
                .setAction(GET_MESSAGES_ACTION)
                .putExtra(FOLDER, folder);
        context.startService(intent);
    }

    public static void postMessage(Context context, Message message) {
        Intent intent = new Intent(context, PostService.class)
                .setAction(POST_MESSAGE)
                .putExtra("MESSAGE", message);
        context.startService(intent);
    }
}
