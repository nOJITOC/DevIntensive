package com.softdesign.devintensive.data.network.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserListRes {
    @SerializedName("success")
    @Expose
    private boolean success;

    public List<User> getData() {
        return data;
    }

    @SerializedName("data")
    @Expose
    private List<User> data = new ArrayList<User>();
}
