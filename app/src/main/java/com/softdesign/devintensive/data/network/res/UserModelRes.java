package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModelRes {

    @SerializedName("success")
    @Expose
    private boolean success;

    public Data getData() {
        return data;
    }

    @SerializedName("data")
    @Expose
    private Data data;

    public class Data {

        @SerializedName("user")
        @Expose
        private User user;

        public User getUser() {
            return user;
        }

        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }
    }


}
