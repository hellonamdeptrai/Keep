package com.nam.keep.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.nam.keep.MainActivity;
import com.nam.keep.database.DataBaseContract;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.AllData;
import com.nam.keep.model.Note;
import com.nam.keep.model.User;
import com.nam.keep.utils.UtilsFunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    public ApiClient(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Retrofit retrofit = getRetrofitInstance(token);

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
                uploadDataUserApi(context);
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
            while (cursor.moveToNext()) {
                boolean userExists = false;
                for (User user : allData.getUsers()) {
                    if (Long.parseLong(cursor.getString(0)) == user.getId()) {
                        userExists = true;
                        break;
                    }
                }
                if (!userExists) {
                    User userCreate = new User();
                    userCreate.setId(cursor.getLong(0));
                    userCreate.setName(cursor.getString(1));
                    userCreate.setAvatar(cursor.getString(2) != null ? cursor.getString(2) : "");
                    userCreate.setEmail(cursor.getString(3));
                    userCreate.setPassword(cursor.getString(4));
                    userCreate.setUpdated_at(cursor.getString(5));
                    userCreate.setIsSync(1);
                    myDatabase.createUser(userCreate);
                }
            }
        } else {
            for (User user: allData.getUsers()) {
                User userCreate = new User();
                userCreate.setId(user.getId());
                userCreate.setName(user.getName());
                userCreate.setAvatar(user.getAvatar() != null ? user.getAvatar()  : "");
                userCreate.setEmail(user.getEmail());
                userCreate.setPassword(user.getPassword());
                userCreate.setUpdated_at(user.getUpdated_at());
                userCreate.setIsSync(1);
                myDatabase.createUser(userCreate);
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

    public void registerUser(Context context, User user) {
        Call<User> call = apiService.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                myDatabase = new DatabaseHelper(context);
                User userData = response.body();
                assert userData != null;

                User userCreate = new User();
                userCreate.setId(userData.getId());
                userCreate.setName(userData.getName());
                userCreate.setAvatar(userData.getAvatar() != null ? userData.getAvatar()  : "");
                userCreate.setEmail(userData.getEmail());
                userCreate.setPassword(userData.getPassword());
                userCreate.setUpdated_at(userData.getUpdated_at());
                userCreate.setIsSync(1);
                myDatabase.createUser(userCreate);

                Toast.makeText(context, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Lỗi kết nối đăng ký tài khoản", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser(Context context, User user) {
        Call<User> call = apiService.loginUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userData = response.body();
                assert userData != null;

                // Lưu token vào SharedPreferences
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", userData.getToken());
                editor.putLong("tokenable_id", userData.getTokenable_id());
                editor.apply();


                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadDataUserApi(Context context) {
        List<User> userList = new ArrayList<>();
        Cursor cursor = myDatabase.getUser();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                User userCreate = new User();
//                userCreate.setId(cursor.getLong(0));
                userCreate.setName(cursor.getString(1));
                userCreate.setAvatar(cursor.getString(2) != null ? cursor.getString(2) : "");
                userCreate.setEmail(cursor.getString(3));
                userCreate.setPassword(cursor.getString(4));
                userCreate.setUpdated_at(cursor.getString(5));
                userList.add(userCreate);
            }
        }
        Call<Void> call = apiService.uploadUser(userList);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Tải lên dữ liệu người dùng thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải lên dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
