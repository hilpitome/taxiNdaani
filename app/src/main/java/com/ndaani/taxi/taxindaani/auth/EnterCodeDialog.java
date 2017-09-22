package com.ndaani.taxi.taxindaani.auth;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ndaani.taxi.taxindaani.R;

/**
 * Created by hilary on 7/24/17.
 */

public class EnterCodeDialog extends DialogFragment implements View.OnClickListener{
    EditText codeEt, newPasswordEt, confirmPasswordEt;

    Button submitBtn;
    AuthActivity authActivity;
    AuthPresenter authPresenter;
    public EnterCodeDialog() { /*empty*/ }

    /** creates a new instance of PropDialogFragment */
    public static EnterCodeDialog newInstance() {
        return new EnterCodeDialog();
    }


    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.

        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_enter_code, null);

        authActivity = (AuthActivity) getActivity();
        authPresenter = authActivity.getPresenter();
        codeEt = (EditText) view.findViewById(R.id.code_et);
        submitBtn = (Button) view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.e("test", "record click event");
        switch(id){
            case R.id.submit:
                sendChanges();
                break;
        }
    }
    public void sendChanges(){
        String code;
        code = codeEt.getText().toString().trim();

        if(validateFields(code)){
            String verificationId = authActivity.getVerificationId();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            authPresenter.signInWithPhoneAuthCredential(credential);
        } else {
            Log.e("test", "we are negative");
        }
    }
    private boolean validateFields(String email) {

        // Reset errors
        codeEt.setError(null);

        if (TextUtils.isEmpty(email)) {
            codeEt.setError("Email is required");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            codeEt.setError("Email is invalid");
            return false;
        }

        return true;
    }
}
