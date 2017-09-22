package com.ndaani.taxi.taxindaani.auth;



import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ndaani.taxi.taxindaani.model.APIError;
import com.ndaani.taxi.taxindaani.model.AuthData;
import com.ndaani.taxi.taxindaani.model.AuthResponse;
import com.ndaani.taxi.taxindaani.networking.ErrorUtils;
import com.ndaani.taxi.taxindaani.networking.NetworkService;

import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lawrence on 11/04/2017.
 */

public class AuthPresenter {



    private final NetworkService service;
    private final AuthView view;
    private CompositeSubscription subscriptions;
    PhoneAuthProvider phoneAuthProvider;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Context context;
    String TAG = "";
    String mVerificationId;
    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;




    public AuthPresenter(NetworkService service, AuthView view, Context context) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();

        this.phoneAuthProvider = PhoneAuthProvider.getInstance();
        this.context = context;
        TAG = context.getClass().getSimpleName();
    }

    void onStop() {
        subscriptions.unsubscribe();
    }


    public void verifyPhoneNumber(String phoneNumber){
        view.showWait();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                view.removeWait();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    view.onFailure(null, e.getMessage(), 0);
                    Toast.makeText(context, "Invalid phone format", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(context, " The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                view.removeWait();
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                view.setVerificationId(verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                view.showDialog();
                // ...
            }
        };
        phoneAuthProvider.verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS, (Activity) context, mCallbacks);

    }
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        view.showWait();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        view.removeWait();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            view.setAuthResponse(user);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(context, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
