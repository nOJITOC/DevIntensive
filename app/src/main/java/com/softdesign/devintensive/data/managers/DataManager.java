package com.softdesign.devintensive.data.managers;


public class DataManager {

    private static DataManager INSTANCE =null;

    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager=new PreferencesManager();
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public static DataManager getInstance(){
        if(INSTANCE==null){
            INSTANCE=new DataManager();
        }
        return INSTANCE;

    }
}
