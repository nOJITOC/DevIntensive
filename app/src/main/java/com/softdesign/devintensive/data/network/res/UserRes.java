package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRes {

    @SerializedName("success")
    @Expose
    private boolean success;

    public UserData getData() {
        return data;
    }

    @SerializedName("data")
    @Expose
    private UserData data;

}