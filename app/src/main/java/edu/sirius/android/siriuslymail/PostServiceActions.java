package edu.sirius.android.siriuslymail;

import android.content.Context;
import android.content.Intent;

import static edu.sirius.android.siriuslymail.IntentConstants.EMAIL;
import static edu.sirius.android.siriuslymail.IntentConstants.FOLDER;
import static edu.sirius.android.siriuslymail.IntentConstants.HOST;
import static edu.sirius.android.siriuslymail.IntentConstants.PASSWORD;

public class PostServiceActions {
    public static final String GET_MESSAGES_ACTION = "GET_MESSAGES_ACTION";
    public static final String LOGIN_ACTION = "LOGIN_ACTION";

    private PostServiceActions() {
        // no instance
    }

    public static void attemptLogin(Context context, String email, String password, String host) {
        Intent intent = new Intent(context, PostService.class)
                .setAction(LOGIN_ACTION)
                .putExtra(HOST, host)
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
}
