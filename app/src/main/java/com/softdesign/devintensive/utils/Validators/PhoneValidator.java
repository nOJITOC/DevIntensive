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

                mEditText.removeTextChangedListener(this);
                mEditText.setText(makeFormatted(editable));
                mEditText.setSelection(mPositionCursor);
        } catch (Exception e) {
            Log.e(ConstantManager.TAG_PREFIX, "phone validator throws exception" + e.getClass());
        }
                mEditText.addTextChangedListener(this);
                super.afterTextChanged(editable);


        super.afterTextChanged(editable);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mPositionCursor = i + i2;
        if (i2>i1){
            if(mPositionCursor==2||mPositionCursor==7)mPositionCursor+=2;
            if(mPositionCursor==12||mPositionCursor==15)mPositionCursor+=1;

        }
        if (i2<i1){
            if(mPositionCursor==4||mPositionCursor==9)mPositionCursor-=2;
            if(mPositionCursor==13||mPositionCursor==16)mPositionCursor-=1;

        }
        if(mPositionCursor<0)mPositionCursor=0;
        if(mPositionCursor>charSequence.length())mPositionCursor=charSequence.length()-1;
    }

    @Override
    protected boolean isValid(String target) {
        String phone = target.replaceAll("[^0-9]", "");
        String temp = target.trim().replaceAll("[0-9 \\-\\(\\)]", "");
        if (!(temp.length() == 0 || (temp.length() == 1 && target.indexOf("+") == 0))) {

            return false;
        }
        return phone.length() == 11;
    }

    /**
     * Форматирует номер телефона в вид +x xxx xxx-xx-xx
     *
     * @param phoneNumber номер телефона
     */
    protected String makeFormatted(Editable phoneNumber) throws Exception {
        String phone = phoneNumber.toString().trim().replaceAll("[^0-9]", "");
        String mask = "+x (xxx) xxx-xx-xx";
        if (phone.length() > 0)
            if (phone.charAt(0) == '8') phone = "7" + phone.substring(1);

        char[] charArray = mask.toCharArray();
        for (int i = 0; i < phone.length()&&i<11; i++) {
            charArray[mask.indexOf("x")] = phone.toCharArray()[i];
            mask = new String(charArray);
        }
        String maskedPhone;
        if (mask.contains("x")) {
            maskedPhone = mask.substring(0, mask.indexOf("x"));
        } else maskedPhone = mask;
        return maskedPhone + phone.substring(maskedPhone.replaceAll("[^0-9]", "").length())+"  ";
    }
}
