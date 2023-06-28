package com.nam.keep.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nam.keep.MainActivity;
import com.nam.keep.api.helper.AsyncTaskCompleteListener;
import com.nam.keep.api.helper.MyAsyncTask;
import com.nam.keep.database.DataBaseContract;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.AllData;
import com.nam.keep.model.Label;
import com.nam.keep.model.Note;
import com.nam.keep.model.User;
import com.nam.keep.utils.UtilsFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    private static final String BASE_URL = "http://192.168.50.143:8000/api/";
    private static final String BASE_URL_STORAGE = "http://192.168.50.143:8000/storage/";
    private ApiService apiService;
    DatabaseHelper myDatabase;
    SharedPreferences sharedPreferences;
    public ApiClient(Context context) {
        sharedPreferences = context.getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);

        apiService = createApiService();
    }
    private ApiService createApiService() {
        String authToken = sharedPreferences.getString("token", "");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (!authToken.isEmpty()) {
            // Tạo interceptor với token được cung cấp
            AuthInterceptor authInterceptor = new AuthInterceptor(authToken);

            // Thêm interceptor vào OkHttpClient
            httpClient.addInterceptor(authInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(ApiService.class);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Tạo OkHttpClient mới mỗi khi đăng xuất
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void getAll(Context context, LottieAnimationView lottieAnimationView, FrameLayout frameLayout, long idUser) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);

        myDatabase = new DatabaseHelper(context);
        Call<AllData> call = apiService.getAllData();
        call.enqueue(new Callback<AllData>() {
            @Override
            public void onResponse(Call<AllData> call, Response<AllData> response) {
                AllData allData = response.body();
                MyAsyncTask myAsyncTask = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                    @Override
                    public void onDoInBackground() throws IOException {
                        getUserApi(allData, context);
                    }

                    @Override
                    public void onTaskComplete(String result) {
                        MyAsyncTask myAsyncTask2 = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onDoInBackground() {
                                uploadDataUserApi(context, idUser);
                            }

                            @Override
                            public void onTaskComplete(String result) {

                            }
                        });
                        myAsyncTask2.execute();
                    }
                });
                myAsyncTask.execute();

                MyAsyncTask myAsyncTaskLabel = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                    @Override
                    public void onDoInBackground() throws IOException {
                        getLabelApi(allData, context);
                    }

                    @Override
                    public void onTaskComplete(String result) {
                        MyAsyncTask myAsyncTaskUploadLabel = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onDoInBackground() {
                                uploadDataLabelApi(context, idUser);
                            }

                            @Override
                            public void onTaskComplete(String result) {

                            }
                        });
                        myAsyncTaskUploadLabel.execute();
                    }
                });
                myAsyncTaskLabel.execute();

                MyAsyncTask myAsyncTaskNote = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                    @Override
                    public void onDoInBackground() throws IOException {
                        getNoteApi(allData, context);
                    }

                    @Override
                    public void onTaskComplete(String result) {
                        MyAsyncTask myAsyncTaskUploadNote = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onDoInBackground() {
                                uploadDataNoteApi(context, idUser);
                            }

                            @Override
                            public void onTaskComplete(String result) {

                            }
                        });
                        myAsyncTaskUploadNote.execute();
                    }
                });
                myAsyncTaskNote.execute();

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

    private void getUserApi(AllData allData, Context context) throws IOException {
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.deleteUserSync(1);
            }
        });
        deleteThread.start();

        try {
            deleteThread.join(); // Đợi cho đến khi deleteThread hoàn thành
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Long> userListId = new ArrayList<>();

        Cursor cursor = myDatabase.getUser();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                userListId.add(Long.parseLong(cursor.getString(0)));
            }
            for (User user : allData.getUsers()) {
                boolean userExists = false;
                for (Long userMyId : userListId) {
                    if (userMyId == user.getId()) {
                        userExists = true;
                        break;
                    }
                }
                if (!userExists) {
                    createUserFromUserObject(user, context);
                }
            }
        } else {
            for (User user : allData.getUsers()) {
                createUserFromUserObject(user, context);
            }
        }
    }

    private void createUserFromUserObject(User user, Context context) throws IOException {
        User userCreate = new User();

        if (user.getAvatar() != null) {
            MyAsyncTask myAsyncTask2 = new MyAsyncTask(new AsyncTaskCompleteListener<String>() {
                String avatar;
                @Override
                public void onDoInBackground() throws IOException {
                    avatar = saveImageToExternalStorage(context, BASE_URL_STORAGE + user.getAvatar());

                }

                @Override
                public void onTaskComplete(String result) {
                    userCreate.setId(user.getId());
                    userCreate.setName(user.getName());
                    userCreate.setAvatar(avatar);
                    userCreate.setEmail(user.getEmail());
                    userCreate.setPassword(user.getPassword());
                    userCreate.setUpdated_at(user.getUpdated_at());
                    userCreate.setIsSync(1);
                    myDatabase.createUser(userCreate);
                }
            });
            myAsyncTask2.execute();
        } else {
            userCreate.setId(user.getId());
            userCreate.setName(user.getName());
            userCreate.setAvatar("");
            userCreate.setEmail(user.getEmail());
            userCreate.setPassword(user.getPassword());
            userCreate.setUpdated_at(user.getUpdated_at());
            userCreate.setIsSync(1);
            myDatabase.createUser(userCreate);
        }
    }

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

    public void uploadDataUserApi(Context context, long idUser) {
        User user = new User();
        MultipartBody.Part avatarPart = null;
        Cursor cursor = myDatabase.getUserDetail(idUser);
        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(1));
            if (cursor.getString(2) != null && !Objects.equals(cursor.getString(2), "")) {
                File avatarFile = new File(cursor.getString(2));
                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), avatarFile);
                avatarPart = MultipartBody.Part.createFormData("avatarFiles", avatarFile.getName(), fileRequestBody);
            }
        }
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user));
        Call<ResponseBody> call = apiService.uploadUser(userRequestBody, avatarPart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("SUCCESS", response.raw() + " ");
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(context, "Tải lên danh sách người dùng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi
                    Toast.makeText(context, "Lỗi tải lên danh sách người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải lên dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String saveImageToExternalStorage(Context context, String fileUrl) {
        File myDir = new File(context.getExternalFilesDir(null), "avatar");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // Lấy phần mở rộng của tập tin từ URL
        String fileExtension = getFileExtensionFromUrl(fileUrl);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_download." + fileExtension;

        final File file = new File(myDir, fileName);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Sử dụng CountDownLatch để đếm số yêu cầu tải xuống cần chờ đợi
        CountDownLatch latch = new CountDownLatch(1);

        // Tạo một tùy chỉnh Request<byte[]> để tải dữ liệu dưới dạng mảng byte
        com.android.volley.Request<byte[]> request = new com.android.volley.Request<byte[]>(com.android.volley.Request.Method.GET, fileUrl, error -> {
            System.out.println("Error downloading image: " + error.getMessage());

            // Nếu có lỗi, giảm giá trị của CountDownLatch để tiếp tục thực hiện hành động tiếp theo
            latch.countDown();
        }) {
            @Override
            protected com.android.volley.Response<byte[]> parseNetworkResponse(NetworkResponse response) {
                try {
                    byte[] data = response.data;
                    return com.android.volley.Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    e.printStackTrace();
                    return com.android.volley.Response.error(new VolleyError(e));
                }
            }

            @Override
            protected void deliverResponse(byte[] response) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(response);
                    fileOutputStream.close();

                    System.out.println("File downloaded successfully.");

                    // Nếu tải xuống thành công, giảm giá trị của CountDownLatch để tiếp tục thực hiện hành động tiếp theo
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Đặt tùy chọn không sử dụng bộ nhớ cache
        request.setShouldCache(false);
        requestQueue.add(request);

        try {
            // Chờ đợi cho đến khi tất cả các yêu cầu tải xuống hoàn thành
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private String getFileExtensionFromUrl(String url) {
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < url.length() - 1) {
            return url.substring(lastDotIndex + 1);
        }
        return "";
    }

    private void getLabelApi(AllData allData, Context context) {
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.deleteLabelSync(1);
            }
        });
        deleteThread.start();

        try {
            deleteThread.join(); // Đợi cho đến khi deleteThread hoàn thành
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Label labelItem : allData.getLabels()) {
            Label label = new Label();
            label.setTitle(labelItem.getTitle());
            label.setUpdated_at(labelItem.getUpdated_at());
            label.setUserId(Long.parseLong(labelItem.getUser_id()));
            label.setIsSync(1);
            myDatabase.createLabel(label);
        }
    }

    public void uploadDataLabelApi(Context context, long idUser) {
        List<Label> labels = new ArrayList<>();
        AllData allData = new AllData();
        Cursor cursor = myDatabase.getLabel(idUser);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Label label = new Label();
                label.setId(Long.parseLong(cursor.getString(0)));
                label.setTitle(cursor.getString(1));
                label.setIsSync(1);
                labels.add(label);
                myDatabase.updateLabel(label);
            }
        }
        allData.setLabels(labels);
        Call<ResponseBody> call = apiService.uploadLabel(allData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("SUCCESS", response.raw() + " ");
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(context, "Tải lên danh sách nhãn thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi
                    Toast.makeText(context, "Lỗi tải lên danh sách nhãn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải lên dữ liệu nhãn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNoteApi(AllData allData, Context context) throws IOException {
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.detachNoteSync(1);
                myDatabase.deleteNoteSync(1);

//                myDatabase.deleteFileInNote(idNote);
//                myDatabase.detachLabel(idNote);
//                myDatabase.detachUser(idNote);
//                myDatabase.deleteImageInNote(idNote);
            }
        });
        deleteThread.start();

        try {
            deleteThread.join(); // Đợi cho đến khi deleteThread hoàn thành
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Note noteItem : allData.getNotes()) {
            String dateString = noteItem.getDeadline() != null ? noteItem.getDeadline() : "";
            String updatedAtString = noteItem.getUpdated_at() != null ? noteItem.getUpdated_at() : "";
            if (!dateString.isEmpty()) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString);
                    assert date != null;
                    dateString = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).format(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!updatedAtString.isEmpty()) {
                try {
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault()).parse(updatedAtString);
                    assert date2 != null;
                    updatedAtString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date2);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            Note note = new Note();
            note.setIndex(noteItem.getIndex());
            note.setTitle(noteItem.getTitle());
            note.setContent(noteItem.getContent());
            note.setIsCheckBoxOrContent(Integer.parseInt(noteItem.getIs_check_box_or_content()));
            note.setDeadline(dateString);
            note.setColor(noteItem.getColor());
            note.setBackground(noteItem.getBackground());
            note.setUpdatedAt(updatedAtString);
            note.setUserId(Long.parseLong(noteItem.getUser_id()));
            note.setIsSync(1);
            note.setArchive(noteItem.getArchive());
            myDatabase.createNote(note);
            long idNewNote = myDatabase.getNoteIdNew();
            myDatabase.attachUser(idNewNote, note.getUserId());
        }
    }

    public void uploadDataNoteApi(Context context, long idUser) {

        List<Note> notes = new ArrayList<>();
        AllData allData = new AllData();
        Cursor cursor = myDatabase.getSearchNoteUser("", idUser);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String dateString = cursor.getString(5);
                if (!dateString.isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(dateString);
                        assert date != null;
                        dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setIndex(Integer.parseInt(cursor.getString(1)));
                note.setTitle(cursor.getString(2));
                note.setContent(cursor.getString(3));
                note.setIsCheckBoxOrContent(Integer.parseInt(cursor.getString(4)));
                note.setDeadline(dateString);
                note.setColor(Integer.parseInt(cursor.getString(6)));
                note.setBackground(cursor.getString(7) != null ? cursor.getString(7) : "");
                note.setUpdatedAt(cursor.getString(9));
                note.setUserId(Long.parseLong(cursor.getString(8)));
                note.setArchive(Integer.parseInt(cursor.getString(11)));
                note.setIsSync(1);
                notes.add(note);
                myDatabase.updateNoteSync(note);
            }
        }
        allData.setNotes(notes);
        Call<ResponseBody> call = apiService.uploadNote(allData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("SUCCESS", response.raw() + " ");
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(context, "Tải lên danh sách ghi chú thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi
                    Toast.makeText(context, "Lỗi tải lên danh sách ghi chú", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải lên dữ liệu ghi chú", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
