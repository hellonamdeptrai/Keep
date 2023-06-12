package com.nam.keep.api;

import com.nam.keep.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.50.144:8000/api/";
    private ApiService apiService;

    public ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void registerUser(User user) {
        Call<User> call = apiService.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("aaaaaaaa "+response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý lỗi kết nối
                System.out.println("ccccccccc"+t);
            }
        });
    }
}
