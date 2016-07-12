package com.softdesign.devintensive.utils;

import android.net.Uri;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 12.07.2016.
 */
public class UserDataResHelper {

    DataManager mDataManager;

    private UserModelRes mUserModelRes;

    public UserDataResHelper(UserModelRes userModelRes) {
        this.mUserModelRes = userModelRes;
        this.mDataManager = DataManager.getInstance();
    }

    public void saveUserValues() {
        int[] userValues = {
                mUserModelRes.getData().getUser().getProfileValues().getRating(),
                mUserModelRes.getData().getUser().getProfileValues().getLinesCode(),
                mUserModelRes.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValue(userValues);

    }

    public void saveUserFields() {

        List<String> userProfileData = new ArrayList<>();
        userProfileData.add(mUserModelRes.getData().getUser().getContacts().getPhone());
        userProfileData.add(mUserModelRes.getData().getUser().getContacts().getEmail());
        userProfileData.add(mUserModelRes.getData().getUser().getContacts().getVk());
        userProfileData.add(mUserModelRes.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userProfileData.add(mUserModelRes.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userProfileData);
    }
    public void saveUserPhoto(){
        mDataManager.getPreferencesManager().saveUserPhoto(
                Uri.parse(mUserModelRes.getData().getUser().getPublicInfo().getPhoto())
        );
    }
    public void saveUserFio(){
        mDataManager.getPreferencesManager().saveUserFio(
                mUserModelRes.getData().getUser().getFirstName()+" "+mUserModelRes.getData().getUser().getSecondName()
        );
    }
    public void saveUserAvatar(){
        mDataManager.getPreferencesManager().saveAvatarPhoto(
                Uri.parse(mUserModelRes.getData().getUser().getPublicInfo().getAvatar())
        );
    }


}
