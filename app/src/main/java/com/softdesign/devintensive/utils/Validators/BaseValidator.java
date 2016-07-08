package com.softdesign.devintensive.utils.Validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Базовый валидатор. Родитель для остальных специализированных валидаторов.
 * Содержит основные методы для проверки текста внутри {@link android.widget.EditText}
 * @author Maruhin Mihail
 */
public abstract class BaseValidator implements TextWatcher {
    /**Содержит текст для проверки на валидность. */
    protected final EditText mEditText;
    /**Текст ошибки при неудачной валидации.*/
    protected  String mErrorMSG;
    /**Значение по умолчанию для поля {@link #mEditText} */
    protected String mDefaultValue;
    /**Позиция курсора в поле с текстом */
    int mPositionCursor;
    /**parent of mEditText with hint and error{@link #mErrorMSG}*/
    protected final TextInputLayout mTextInputLayout;
    /**Базовый конструктор
     *@param editText поле для проверки на валидность
     *@param defaultValue значение по умолчанию для проверки на валидность или подставления в поле текста, если что-то пошло не так
     *@param errorMSG сообщение об ошибки
     */
    public BaseValidator(EditText editText, String defaultValue,String errorMSG) {
        this.mEditText = editText;
        this.mTextInputLayout = (TextInputLayout) mEditText.getParent();
        this.mDefaultValue=defaultValue;
        this.mErrorMSG=errorMSG;
    }
    /**
     * Проверяет на валидность(соответствие определённому правилу)
     * @param target текст для проверки
     */
    protected abstract boolean isValid(String target);

    /**
     * Назначает текст ошибки
     * @param errorMSG - текст ошибки
     */
    protected void setError(String errorMSG){
        mTextInputLayout.setError(errorMSG);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterTextChanged(Editable editable) {
        if (!isValid(mEditText.getText().toString())) {
            mTextInputLayout.setErrorEnabled(true);
            setError(mErrorMSG);
        } else mTextInputLayout.setErrorEnabled(false);
    }
}
