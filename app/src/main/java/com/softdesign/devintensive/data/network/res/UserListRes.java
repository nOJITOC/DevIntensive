package com.softdesign.devintensive.data.network.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserListRes {
    @SerializedName("success")
    @Expose
    private boolean success;

    public List<UserData> getData() {
        return data;
    }

    @SerializedName("data")
    @Expose
    private List<UserData> data = new ArrayList<UserData>();
}
