package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UserRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import retrofit2.Call;

/**
 * Class-Singleton  for {@link PreferencesManager}
 */
public class DataManager {

    private static DataManager INSTANCE =null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    public DataManager() {
        this.mPreferencesManager=new PreferencesManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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
    public Context getContex(){
        return mContext;
    }
    //region =========Network===========
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }
    public Call<UserRes> loginToken(String userId){
        return mRestService.loginToken(userId);
    }
    //endregion
    //============================Database===============

}
