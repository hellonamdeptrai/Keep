package com.nam.keep.ui.note;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nam.keep.R;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.FileModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseHelper dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dataSource = new DatabaseHelper(this);
//
//        List<Image> images = dataSource.getAllImages();
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MyRecyclerAdapter(images));

        Button addImageButton = findViewById(R.id.add_image_button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                FileModel fileModel = new FileModel(name,path);
                String newPath = saveImageToExternalStorage(fileModel);
                fileModel.setPath(newPath);
//                saveImageToDatabase(image);
                cursor.close();
            }
            //                dataSource.createImage(imagePath, 2);

        }
    }

    private String saveImageToExternalStorage(FileModel fileModel) {
        File myDir = new File(getExternalFilesDir(null), "data");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // Tạo tên tệp ảnh dựa trên thời gian hiện tại.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_" + fileModel.getName() + ".jpg";

        File file = new File(myDir, fileName);
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse("file://"+ fileModel.getPath()));
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

//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        dataSource.close();
//    }
}