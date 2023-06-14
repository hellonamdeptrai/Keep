package com.nam.keep.ui.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nam.keep.R;
import com.nam.keep.database.DataBaseContract;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.FileModel;
import com.nam.keep.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    // view
    RoundedImageView avatar;
    EditText nameProfile, emailProfile;
    Button btnUpdate;

    // data
    private DatabaseHelper dataSource;
    SharedPreferences sharedPreferences;
    long idUser;
    User userData = new User();
    Uri uriAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar mToolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");

        dataSource = new DatabaseHelper(this);

        avatar = findViewById(R.id.color_background_imaged);
        nameProfile = findViewById(R.id.edit_text_name_profile);
        emailProfile = findViewById(R.id.edit_text_email_profile);
        btnUpdate = findViewById(R.id.btnUpdateProfile);


        sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getLong("tokenable_id", 0);

        Cursor cursor = dataSource.getUserDetail(idUser);
        if (cursor.moveToFirst()) {
            userData.setId(Long.parseLong(cursor.getString(0)));
            userData.setName(cursor.getString(1));
            userData.setAvatar(cursor.getString(2));
            userData.setEmail(cursor.getString(3));
        }

        nameProfile.setText(userData.getName());
        emailProfile.setText(userData.getEmail());
        if (!userData.getAvatar().isEmpty()) {
            Glide.with(avatar.getContext())
                    .load("file://" + userData.getAvatar())
                    .override(300,300)
                    .apply(new RequestOptions().transform(new CircleCrop()))
                    .into(avatar);
        }

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1034);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPath = "";
                if (uriAvatar != null) {
                    String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uriAvatar, projection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        newPath = saveImageToExternalStorage(name, path);
                        cursor.close();
                    }
                }

                User userDataUpdate = new User();
                userDataUpdate.setId(idUser);
                userDataUpdate.setName(nameProfile.getText().toString());
                userDataUpdate.setAvatar(newPath);
                userDataUpdate.setUpdated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                userDataUpdate.setIsSync(1);
                dataSource.updateUser(userDataUpdate);
                finish();
                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1034 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            uriAvatar = uri;
            Glide.with(avatar.getContext())
                    .load(uriAvatar)
                    .override(300,300)
                    .apply(new RequestOptions().transform(new CircleCrop()))
                    .into(avatar);
        }
    }

    @NonNull
    private String saveImageToExternalStorage(String name, String path) {
        File myDir = new File(getExternalFilesDir(null), "avatar");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // Tạo tên tệp ảnh dựa trên thời gian hiện tại.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_" + name;

        File file = new File(myDir, fileName);
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse("file://"+ path));
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}