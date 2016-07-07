package com.softdesign.devintensive.utils.Validators;

import android.text.Editable;
import android.widget.EditText;



/**
 * Created by Иван on 05.07.2016.
 */
public class PhoneValidator extends BaseValidator {

    public PhoneValidator(EditText editText, String defaultValue, String errorMSG) {
        super(editText, defaultValue, errorMSG);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isValid(editable.toString())) {
            makeFormated(editable);
        }
        super.afterTextChanged(editable);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mPositionCursor = i + i2;
    }

    @Override
    protected boolean isValid(String target) {
        String phone = target.replaceAll("[^0-9]", "");
        String temp = target.trim().replaceAll("[0-9 -]", "");
        if (!(temp.length() == 0 || (temp.length() == 1 && target.indexOf("+") == 0))) {

            return false;
        }
        return phone.length() == 11;
    }


    protected void makeFormated(Editable s) {
        String phone = s.toString().trim().replaceAll("[^0-9]", "");
        String result;

        if (phone.startsWith("8")) {
            result = "+7";
        } else result = "+" + phone.substring(0, 1);
        result += " " + phone.substring(1, 4) + " " + phone.substring(4, 7) + " - " + phone.substring(7, 9) + " - " + phone.substring(9, 11);
        phone = result;
        mEditText.removeTextChangedListener(this);
        s.clear();
        s.append(phone);
        mEditText.setSelection(mPositionCursor);
        mEditText.addTextChangedListener(this);
    }


}
