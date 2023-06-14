package com.nam.keep.api;

import com.nam.keep.model.AllData;
import com.nam.keep.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/register")
    Call<User> registerUser(@Body User user);

    @POST("auth/login")
    Call<User> loginUser(@Body User user);

    @GET("sync")
    Call<AllData> getAllData();
}
