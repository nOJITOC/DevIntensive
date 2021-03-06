package com.softdesign.devintensive.utils.Validators;

import android.content.Context;
import android.widget.EditText;

import com.softdesign.devintensive.R;

import java.util.List;


public class ValidateManager {


    private BaseValidator[] mInfoValidators;

    public ValidateManager(Context context, List<EditText> forValidate) {
        String[] defaultValue = context.getResources().getStringArray(R.array.default_user_info);
        String[] validationError = context.getResources().getStringArray(R.array.validation_error);
        //array of userfields validator
        this.mInfoValidators =new BaseValidator[]{
                new PhoneValidator(forValidate.get(0),defaultValue[0],validationError[0]),//phone
                new EmailValidator(forValidate.get(1),defaultValue[1],validationError[1]),//email
                new UrlValidator(forValidate.get(2),defaultValue[2],validationError[2],3),//vk.com
                new UrlValidator(forValidate.get(3),defaultValue[3],validationError[3],3),//github
        };
    }

    public BaseValidator getValidator(int i) {
        return mInfoValidators[i];
    }

}