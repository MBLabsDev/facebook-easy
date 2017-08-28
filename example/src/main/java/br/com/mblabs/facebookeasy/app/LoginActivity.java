package br.com.mblabs.facebookeasy.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import br.com.mblabs.facebookeasy.FacebookManager;
import br.com.mblabs.facebookeasy.business.FacebookBO;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final List<String> DUMMY_PERMISSIONS = Arrays.asList("public_profile", "user_likes", "user_birthday", "email");
    private FacebookManager mFacebookManager;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{"foo@example.com:hello", "bar@example.com:world"};

    // UI references.
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFacebookManager = new FacebookManager(this, DUMMY_PERMISSIONS);

        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showButton(false);
        mFacebookManager.connect(new FacebookLoginListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookManager.onActivityResult(requestCode, resultCode, data);
    }

    private class FacebookLoginListener implements FacebookBO.AppFacebookLoginListener {

        @Override
        public void onFacebookSuccess(@NonNull String facebookId, @NonNull String token) {

        }

        @Override
        public void onFacebookError() {
            showButton(true);
        }

        @Override
        public void onFacebookCancel() {
            showButton(true);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showButton(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

