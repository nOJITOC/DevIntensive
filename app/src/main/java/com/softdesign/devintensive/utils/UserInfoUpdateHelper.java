package com.softdesign.devintensive.utils;

import android.net.Uri;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 12.07.2016.
 */
public class UserInfoUpdateHelper {

    DataManager mDataManager;

    private User mUserModelRes;

    public UserInfoUpdateHelper(User userRes) {
        this.mUserModelRes = userRes;
        this.mDataManager = DataManager.getInstance();
    }

    public void saveUserValues() {
        int[] userValues = {
                mUserModelRes.getProfileValues().getRating(),
                mUserModelRes.getProfileValues().getLinesCode(),
                mUserModelRes.getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValue(userValues);

    }

    public void saveUserFields() {

        List<String> userProfileData = new ArrayList<>();
        userProfileData.add(mUserModelRes.getContacts().getPhone());
        userProfileData.add(mUserModelRes.getContacts().getEmail());
        userProfileData.add(mUserModelRes.getContacts().getVk());
        userProfileData.add(mUserModelRes.getRepositories().getRepo().get(0).getGit());
        userProfileData.add(mUserModelRes.getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userProfileData);
    }
    public void saveUserPhoto(){
        mDataManager.getPreferencesManager().saveUserPhoto(
                Uri.parse(mUserModelRes.getPublicInfo().getPhoto())
        );
    }
    public void saveUserFio(){
        mDataManager.getPreferencesManager().saveUserFio(
                mUserModelRes.getFirstName()+" "+mUserModelRes.getSecondName()
        );
    }
    public void saveUserAvatar(){
        mDataManager.getPreferencesManager().saveAvatarPhoto(
                Uri.parse(mUserModelRes.getPublicInfo().getAvatar())
        );
    }


}
