package com.softdesign.devintensive.utils.Validators;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.utils.ConstantManager;


/**
 * Created by Иван on 06.07.2016.
 */
public class UrlValidator extends BaseValidator{
    private String mBefore;
    public String mAfter;

    public UrlValidator(EditText editText, String defaultValue, String errorMSG) {
        super(editText, defaultValue, errorMSG);
    }

    @Override
    protected boolean isValid(String target) {
        return mEditText.length() > mDefaultValue.length() + 2;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.toString().contains(mDefaultValue)) mAfter =charSequence.toString().substring(charSequence.toString()
                .indexOf(mDefaultValue)+mDefaultValue.length());
        if(i<mDefaultValue.length())mPositionCursor=mDefaultValue.length();
        else mPositionCursor=i+i1;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mBefore=mAfter;
        Log.e(ConstantManager.TAG_PREFIX,i+"  curs  "+ mBefore);


    }



    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.toString().indexOf(mDefaultValue)!=0){
            try {
                mEditText.removeTextChangedListener(this);
                editable.clear();
                editable.append(mDefaultValue+ mAfter);
                mEditText.setSelection(mPositionCursor);
                mEditText.addTextChangedListener(this);

            }catch(Exception e){

                editable.clear();
                editable.append(mDefaultValue + mBefore);

            }
        }
        super.afterTextChanged(editable);
    }
}
