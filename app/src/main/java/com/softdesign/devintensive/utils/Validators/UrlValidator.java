package com.softdesign.devintensive.utils.Validators;

import android.text.Editable;
import android.widget.EditText;


/**
 * Валидатор для Url
 * @author Maruhin Mihail
 */
public class UrlValidator extends BaseValidator{
    /**
     * значение текстового поля {@link #mEditText} до изменения, исключая {@link #mDefaultValue}
     */
    private String mBefore="";
    /**
     * значение текстового поля {@link #mEditText} после изменения, исключая {@link #mDefaultValue}
     */
    private String mAfter="";

    /**
     * Базовый конструктор
     *@param editText поле для проверки на валидность
     *@param defaultValue значение по умолчанию для проверки на валидность или подставления в поле текста,
     *                    если что-то пошло не так
     *@param errorMSG сообщение об ошибки
     */
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
