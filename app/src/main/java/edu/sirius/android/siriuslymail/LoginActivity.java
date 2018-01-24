package edu.sirius.android.siriuslymail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static edu.sirius.android.siriuslymail.PostService.SUCCESS_LOGIN;
import static edu.sirius.android.siriuslymail.PostServiceActions.LOGIN_ACTION;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    final String TAG = "lifecycle_login";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getExtras().getBoolean(SUCCESS_LOGIN);
            if (success) {
                finish();
                MainActivity.start(LoginActivity.this);
            } else {
                showProgress(false);
                Toast toast = Toast.makeText(getApplicationContext(), "Email, password or host is incorrect", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };



    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mImapHostView;
    private EditText mSmtpHostView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mImapHostView = findViewById(R.id.imap_host);
        mSmtpHostView = findViewById(R.id.smtp_host);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(LOGIN_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Log.d(TAG, "onDestroy()");
    }

    private void attemptLogin() {


        mEmailView.setError(null);
        mPasswordView.setError(null);
        mImapHostView.setError(null);
        mSmtpHostView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String imapHost = mImapHostView.getText().toString();
        String smtpHost = mSmtpHostView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid IMAP host
        if (TextUtils.isEmpty(imapHost)) {
            mImapHostView.setError(getString(R.string.error_field_required));
            focusView = mImapHostView;
            cancel = true;
        } else if (!isHostValid(imapHost)) {
            mImapHostView.setError(getString(R.string.error_invalid_host));
            focusView = mImapHostView;
            cancel = true;
        }

        // Check for a valid SMTP host
        if (TextUtils.isEmpty(smtpHost)) {
            mSmtpHostView.setError(getString(R.string.error_field_required));
            focusView = mSmtpHostView;
            cancel = true;
        } else if (!isHostValid(smtpHost)) {
            mSmtpHostView.setError(getString(R.string.error_invalid_host));
            focusView = mSmtpHostView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            PostServiceActions.attemptLogin(this, email, password, imapHost, smtpHost);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isHostValid(String host) {
        return host.contains(".");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}

