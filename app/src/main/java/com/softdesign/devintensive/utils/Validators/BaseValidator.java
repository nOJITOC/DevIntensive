package com.softdesign.devintensive.utils.Validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Иван on 05.07.2016.
 */
public abstract class BaseValidator implements TextWatcher {
    protected final EditText mEditText;
    protected  String mErrorMSG;
    protected String mDefaultValue;
    int mPositionCursor;
    protected final TextInputLayout mTextInputLayout;
    public BaseValidator(EditText editText, String defaultValue,String errorMSG) {
        this.mEditText = editText;
        this.mTextInputLayout = (TextInputLayout) mEditText.getParent();
        this.mDefaultValue=defaultValue;
        this.mErrorMSG=errorMSG;
    }
    protected abstract boolean isValid(String target);
    protected void setError(String msg){
        mTextInputLayout.setError(msg);
    }
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!isValid(mEditText.getText().toString())) {
            mTextInputLayout.setErrorEnabled(true);
            setError(mErrorMSG);
        } else mTextInputLayout.setErrorEnabled(false);
    }
}
