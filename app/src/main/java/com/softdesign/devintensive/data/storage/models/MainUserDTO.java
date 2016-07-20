package com.softdesign.devintensive.data.storage.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserData;

import java.util.ArrayList;
import java.util.List;

public class MainUserDTO implements Parcelable {
    private String mPhoto;
    private String mAvatar;
    private String mFullName;
    private String mRating;
    private String mCodeLines;



    private String mProjects;
    private String mBio;
    private String mPhone;
    private String mEmail;
    private String mVk;

    private List<String> mRepositories;
    public String getAvatar() {
        return mAvatar;
    }
    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodeLines() {
        return mCodeLines;
    }

    public String getProjects() {
        return mProjects;
    }

    public String getBio() {
        return mBio;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getVk() {
        return mVk;
    }

    public List<String> getRepositories() {
        return mRepositories;
    }

    public MainUserDTO(UserData userData) {
        List<String> repoLink=new ArrayList<>();
        for (UserData.Repo repo : userData.getRepositories().getRepo()) {
            repoLink.add(repo.getGit());
        }
        this.mPhoto = userData.getPublicInfo().getPhoto();
        this.mAvatar = userData.getPublicInfo().getAvatar();
        this.mFullName = userData.getFullName();
        this.mRating = String.valueOf(userData.getProfileValues().getRating());
        this.mCodeLines = String.valueOf(userData.getProfileValues().getLinesCode());
        this.mProjects = String.valueOf(userData.getProfileValues().getProjects());
        this.mBio = userData.getPublicInfo().getBio();
        this.mPhone = userData.getContacts().getPhone();
        this.mEmail = userData.getContacts().getEmail();
        this.mVk = userData.getContacts().getVk();
        mRepositories = repoLink;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public void setCodeLines(String codeLines) {
        mCodeLines = codeLines;
    }

    public void setProjects(String projects) {
        mProjects = projects;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setVk(String vk) {
        mVk = vk;
    }

    public void setRepositories(List<String> repositories) {
        mRepositories = repositories;
    }

    protected MainUserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mRating = in.readString();
        mCodeLines = in.readString();
        mProjects = in.readString();
        mBio = in.readString();
        mPhone = in.readString();
        mEmail = in.readString();
        mVk = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRating);
        dest.writeString(mCodeLines);
        dest.writeString(mProjects);
        dest.writeString(mBio);
        dest.writeString(mPhone);
        dest.writeString(mEmail);
        dest.writeString(mVk);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MainUserDTO> CREATOR = new Parcelable.Creator<MainUserDTO>() {
        @Override
        public MainUserDTO createFromParcel(Parcel in) {
            return new MainUserDTO(in);
        }

        @Override
        public MainUserDTO[] newArray(int size) {
            return new MainUserDTO[size];
        }
    };
}