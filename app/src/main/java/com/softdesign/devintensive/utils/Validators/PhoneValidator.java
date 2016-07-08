package com.softdesign.devintensive.utils.Validators;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.utils.ConstantManager;


/**
 * Валидатор для номера телефона. Проверяет на количество цифр в телевоне и форматирует в вид x xxx xxx - xx - xx
 *
 * @author Maruhin Mihail
 */
public class PhoneValidator extends BaseValidator {


    /**
     * Базовый конструктор
     *
     * @param editText     поле для проверки на валидность
     * @param defaultValue значение по умолчанию для проверки на валидность или подставления в поле текста, если что-то пошло не так
     * @param errorMSG     сообщение об ошибки
     */
    public PhoneValidator(EditText editText, String defaultValue, String errorMSG) {
        super(editText, defaultValue, errorMSG);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            if (isValid(editable.toString())) {
                mEditText.removeTextChangedListener(this);
                mEditText.setText(makeFormatted(editable));
                mEditText.setSelection(mPositionCursor);
                mEditText.addTextChangedListener(this);
                super.afterTextChanged(editable);
            }
        } catch (Exception e) {
            Log.e(ConstantManager.TAG_PREFIX, "phone validator throws exception" + e.getStackTrace().toString());
            mEditText.setText(editable.toString());
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

    /**
     * Форматирует номер телефона в вид +x xxx xxx - xx - xx
     *
     * @param phoneNumber номер телефона
     */
    protected String makeFormatted(Editable phoneNumber) throws Exception {
        String phone = phoneNumber.toString().trim().replaceAll("[^0-9]", "");
        String maskedPhone = "";
            if (phone.startsWith("8")) {
                maskedPhone = "+7";
            } else maskedPhone = "+" + phone.substring(0, 1);
            maskedPhone += " " + phone.substring(1, 4) + " " + phone.substring(4, 7) + " - " + phone.substring(7, 9) + " - " + phone.substring(9, 11);
            return maskedPhone;
    }
}
