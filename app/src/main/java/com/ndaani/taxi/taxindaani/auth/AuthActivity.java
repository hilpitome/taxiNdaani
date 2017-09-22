package com.ndaani.taxi.taxindaani.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.Transaction;
import com.ndaani.taxi.taxindaani.MainApplication;
import com.ndaani.taxi.taxindaani.MapsActivity;
import com.ndaani.taxi.taxindaani.R;
import com.ndaani.taxi.taxindaani.model.AuthData;
import com.ndaani.taxi.taxindaani.model.AuthResponse;
import com.ndaani.taxi.taxindaani.networking.NetworkService;
import com.ndaani.taxi.taxindaani.utils.PrefUtils;


import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lawrence on 11/04/2017.
 */

public class AuthActivity extends AppCompatActivity implements AuthView {

    @BindView(R.id.verify_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.phoneNum)
    TextInputEditText phoneNum;
    @BindString(R.string.error_field_required)
    String error_field_required;
    @BindString(R.string.error_invalid_phone)
    String error_invalid_phone;
    @BindString(R.string.positive_button_text)
    String positive_button_text;


    private AuthPresenter presenter;
    private PrefUtils prefUtils;

    String mVerificationId;

    android.os.Handler handler;



    private static final String TAG = AuthActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        // initialize PrefUtils
        prefUtils = new PrefUtils(this);


        /**
         * If user is logged in, redirect to MainActivity
         */
        if (prefUtils.isLoggedIn()) {
            Intent intent = new Intent(AuthActivity.this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }

        // Initialize NetworkService and Presenter classes

        NetworkService service = ((MainApplication) getApplicationContext()).getNetworkService();
        presenter = new AuthPresenter(service, this, this);



    }

    @OnClick(R.id.btnVerify)
    public void verify() {


        // Reset errors.
        phoneNum.setError(null);

        // Store values at the time of the verify attempt.
        String phone = phoneNum.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(phone)) {
            phoneNum.setError(error_field_required);
            focusView = phoneNum;
            cancel = true;
        } else if (!phone.matches("^(0|\\+?254)7(\\d){8}$")) {
            phoneNum.setError(error_invalid_phone);
            focusView = phoneNum;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            presenter.verifyPhoneNumber(phone);

            // Pass the phone number inputted by the user to the presenter which will then send it to the API
//            presenter.verifyAgent(phone);
        }
    }

    @Override
    public void showWait() {
        showProgress(true);
    }

    @Override
    public void removeWait() {
        showProgress(false);
    }

    @Override
    public void onFailure(String message, String description, int code) {
        showProgress(false);

        new MaterialDialog.Builder(this)
                .title(message)
                .content(description)
                .positiveText(positive_button_text)
                .show();
    }

    @Override
    public void setVerificationId(String verificationId) {
        mVerificationId = verificationId;
    }

    @Override
    public void setAuthResponse(FirebaseUser user) {

        // store user access token in SharedPreference
        //prefUtils.setUserAccessToken(String.format(Locale.ENGLISH, "%s %s", authResponse.getTokenType(), authResponse.getAccessToken()));
        // store user details in SharedPreference
       // prefUtils.storeUserDetails(authResponse.getUser().getName(), authResponse.getUser().getPhone());
        System.out.println("user "+user.getPhoneNumber());

        prefUtils.storeUserDetails(user.getPhoneNumber());
        prefUtils.setUserId(user.getUid());

        // redirect user to MainActivity
        Intent intent = new Intent(AuthActivity.this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

    public AuthPresenter getPresenter() {
        return presenter;
    }

    public String getVerificationId() {
        return mVerificationId;
    }
    @Override
    public void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }


        // Create and show the dialog.
        DialogFragment newFragment = EnterCodeDialog.newInstance();
        newFragment.show(ft, "dialog");
    }
}
