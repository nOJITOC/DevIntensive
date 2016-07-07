package com.softdesign.devintensive.utils.Validators;

import android.text.Editable;
import android.widget.EditText;


/**
 * Created by Иван on 06.07.2016.
 */
public class UrlValidator extends BaseValidator{
    private String mBefore;

    public UrlValidator(EditText editText, String defaultValue, String errorMSG) {
        super(editText, defaultValue, errorMSG);
    }

    @Override
    protected boolean isValid(String target) {
        return mEditText.length() > mDefaultValue.length() + 2;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.toString().contains(mDefaultValue)) mBefore =charSequence.toString().substring(charSequence.toString()
                .indexOf(mDefaultValue)+mDefaultValue.length());
        else mBefore ="";
        if(i<mDefaultValue.length())mPositionCursor=mDefaultValue.length();
        else mPositionCursor=i+i1;
//        Log.e(ConstantManager.TAG_PREFIX,i+"  curs  "+ mBefore);


    }



    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.toString().indexOf(mDefaultValue)!=0){
            try {
                mEditText.removeTextChangedListener(this);
                editable.clear();
                editable.append(mDefaultValue+ mBefore);
                mEditText.setSelection(mPositionCursor);
                mEditText.addTextChangedListener(this);

            }catch(Exception e){

                editable.clear();
                editable.append(mDefaultValue);

            }
        }
        super.afterTextChanged(editable);
    }
}
