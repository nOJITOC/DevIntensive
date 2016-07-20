package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.softdesign.devintensive.data.storage.models.MainUserDTO;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

/**
 * Class, that contain {@link SharedPreferences}
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;

    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveMainUser(MainUserDTO mainUser){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mainUser);
        editor.putString(ConstantManager.MAIN_USER,json);
        editor.commit();

    }
    public MainUserDTO loadMainUser(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(ConstantManager.MAIN_USER,null);
        return gson.fromJson(json,MainUserDTO.class);
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

    public String getLastUpdateDate(){
        return mSharedPreferences.getString(ConstantManager.LAST_UPDATE,null);
    }
    public void setLastUpdateDate(String date){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LAST_UPDATE, date);

    }

}

