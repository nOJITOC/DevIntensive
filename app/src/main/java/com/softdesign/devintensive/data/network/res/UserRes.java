package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRes {

    @SerializedName("success")
    @Expose
    private boolean success;

    public User getData() {
        return data;
    }

    @SerializedName("data")
    @Expose
    private User data;

}