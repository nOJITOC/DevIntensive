package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Class, that contain {@link SharedPreferences}
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    public static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_BIO_KEY,
    };
    public static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE,
    };

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
        String[] defaultUsersFields = DevIntensiveApplication.getContext().getResources().getStringArray(R.array.default_user_info);
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userFields.add(mSharedPreferences.getString(USER_FIELDS[i], defaultUsersFields[i]));
        }
        return userFields;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/prof"));
    }

    public void saveAvatarPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadAvatarPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, "android.resource://com.softdesign.devintensive/drawable/ava"));
    }
    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public List<String> loadUserProfileValue() {
        List<String> userValues = new ArrayList<>();
        for (int i = 0; i < USER_VALUES.length; i++) {
            userValues.add(mSharedPreferences.getString(USER_VALUES[i], "0"));
        }
        return userValues;
    }

    public void saveUserProfileValue(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }

        editor.apply();
    }

    public void saveUserFio(String fio) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_FIO_KEY, fio);

    }

    public String getUserFio() {
        return mSharedPreferences.getString(
                ConstantManager.USER_FIO_KEY,
                DevIntensiveApplication.getContext().getResources().getString(R.string.user_fio)
        );
    }

    public String getEmail() {
        return mSharedPreferences.getString(
                ConstantManager.USER_MAIL_KEY,
                DevIntensiveApplication.getContext().getResources().getString(R.string.e_mail)
        );
    }
    public String getLastUpdateDate(){
        return mSharedPreferences.getString(ConstantManager.LAST_UPDATE,null);
    }
    public void setLastUpdateDate(String date){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LAST_UPDATE, date);

    }

}

