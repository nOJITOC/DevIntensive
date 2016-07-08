package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;


public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    public static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY,};

    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }
    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        String[] defaultUsersFields=DevIntensiveApplication.getContext().getResources().getStringArray(R.array.user_info);
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userFields.add(mSharedPreferences.getString(USER_FIELDS[i],defaultUsersFields[i]));
        }
        return userFields;
    }
    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY,uri.toString());
        editor.apply();
    }
    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,"android.resource://com.softdesign.devintensive/drawable/prof"));
    }
}
