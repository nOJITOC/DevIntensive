package com.softdesign.devintensive.utils.Validators;

import android.widget.EditText;

/**
 * Валидатор для почты
 */
public class EmailValidator extends BaseValidator {

    public EmailValidator(EditText editText, String defaultValue, String errorMSG) {
        super(editText, defaultValue, errorMSG);
    }

    @Override
    protected boolean isValid(String target) {
        String pattern = "^[\\w\\+\\.\\%\\-]{3,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{1,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25})[ ]*";
        return target.matches(pattern);
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


}
