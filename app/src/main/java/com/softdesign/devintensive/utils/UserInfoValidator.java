package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

public class UserInfoValidator implements TextWatcher {

    private final Resources mResources;
    private final EditText mEditText;
    private final TextInputLayout mTextInputLayout;
    public UserInfoValidator(Context context, EditText editText, TextInputLayout textInputLayout) {
        this.mResources = context.getResources();
        this.mEditText = editText;
        this.mTextInputLayout = textInputLayout;
    }
    @Override
    public void afterTextChanged(Editable editable) {
        validatePhone(editable);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        validatePhone((Editable) charSequence);
    }

    /**
     * Validating form
     */


    private void validatePhone(Editable e){
        String phone =  e.toString().trim();
        if(!isValidPhone(phone)){
            mTextInputLayout.setError("неверно введен телефон");
        }else  mTextInputLayout.setError("введен телефон");
    }
//    private boolean validateEmail() {
//        String email = inputEmail.getText().toString().trim();
//
//        if (email.isEmpty() || !isValidEmail(email)) {
//            inputLayoutEmail.setError(getString(R.string.err_msg_email));
//            requestFocus(inputEmail);
//            return false;
//        } else {
//            inputLayoutEmail.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


}