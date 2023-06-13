package com.nam.keep.api;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.AllData;
import com.nam.keep.model.Note;
import com.nam.keep.model.User;
import com.nam.keep.utils.UtilsFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://172.20.10.7:8000/api/";
    private static Retrofit retrofit;
    private final ApiService apiService;
    DatabaseHelper myDatabase;

    public ApiClient() {
        Retrofit retrofit = getRetrofitInstance("1|xQnq25nQxWb6kwY5M5siuBfg7WFBPpmflHSQLMw7");

        apiService = retrofit.create(ApiService.class);
    }

    public Retrofit getRetrofitInstance(String authToken) {
        if (retrofit == null) {
            // Tạo interceptor với token được cung cấp
            AuthInterceptor authInterceptor = new AuthInterceptor(authToken);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Thêm interceptor vào OkHttpClient
            httpClient.addInterceptor(authInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public void getAll(Context context, LottieAnimationView lottieAnimationView, FrameLayout frameLayout) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);

        myDatabase = new DatabaseHelper(context);
        Call<AllData> call = apiService.getAllData();
        call.enqueue(new Callback<AllData>() {
            @Override
            public void onResponse(Call<AllData> call, Response<AllData> response) {
                AllData allData = response.body();
                assert allData != null;

                getUserApi(allData);

                lottieAnimationView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Đồng bộ thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AllData> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Lỗi đồng bộ", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getUserApi(AllData allData) {
        Cursor cursor = myDatabase.getUser();
        if(cursor.getCount() != 0){
            myDatabase.deleteUserSync(1);
            for (User user: allData.getUsers()) {
                myDatabase.createUser(new User(
                        user.getName(),
                        user.getAvatar(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUpdated_at(),
                        1
                ));
            }
        } else {
            for (User user: allData.getUsers()) {
                myDatabase.createUser(new User(
                        user.getName(),
                        user.getAvatar(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUpdated_at(),
                        1
                ));
            }
        }

    }

//    private void getUserApi(AllData allData) {
//        // sqlite
//        List<User> users = new ArrayList<>();
//        Cursor cursor = myDatabase.getUser();
//        if(cursor.getCount() != 0){
//            while (cursor.moveToNext()){
//                User user = new User();
//                user.setId(Long.parseLong(cursor.getString(0)));
//                user.setName(cursor.getString(1));
////                        user.setAvatar(cursor.getString(2));
//                user.setEmail(cursor.getString(3));
//                user.setPassword(cursor.getString(4));
//                user.setUpdated_at(cursor.getString(5));
//                user.setIsSync(Integer.parseInt(cursor.getString(6)));
//                if (user.getIsSync() == 1) {
//                    users.add(user);
//                }
//
//            }
//
//            for (User user : allData.getUsers()) {
//                boolean found = false;
//                for (User us : users) {
//                    if (user.getUpdated_at().equals(us.getUpdated_at())) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) {
//                    // Thời gian cập nhật khác nhau, lưu dữ liệu vào SQLite
//                    myDatabase.createUser(new User(
//                            user.getName(),
//                            user.getAvatar(),
//                            user.getEmail(),
//                            user.getPassword(),
//                            user.getUpdated_at(),
//                            1
//                    ));
//                }
//            }
//        } else {
//            for (User user: allData.getUsers()) {
//                myDatabase.createUser(new User(
//                        user.getName(),
//                        user.getAvatar(),
//                        user.getEmail(),
//                        user.getPassword(),
//                        user.getUpdated_at(),
//                        1
//                ));
//            }
//        }
//    }

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
