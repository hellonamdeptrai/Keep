package com.nam.keep.api;

import com.nam.keep.model.AllData;
import com.nam.keep.model.Label;
import com.nam.keep.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    @POST("auth/register")
    Call<User> registerUser(@Body User user);

    @POST("auth/login")
    Call<User> loginUser(@Body User user);

    @GET("sync")
    Call<AllData> getAllData();

    @Multipart
    @POST("sync/user")
    Call<ResponseBody> uploadUser(@Part("user") RequestBody user, @Part MultipartBody.Part avatars);

    @POST("sync/label")
    Call<ResponseBody> uploadLabel(@Body AllData labels);

    @POST("sync/note")
    Call<ResponseBody> uploadNote(@Body AllData notes);

    @POST("sync/note-has-user")
    Call<ResponseBody> uploadNoteHasUser(@Body AllData notes);
}
