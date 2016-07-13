package com.softdesign.devintensive.data.network;


import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);
    @GET("user/{userId}")
    Call<UserRes> loginToken(@Path("userId") String userId);
}
