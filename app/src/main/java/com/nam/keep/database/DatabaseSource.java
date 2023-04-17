package com.nam.keep.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nam.keep.model.FileModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSource {
    private Context context;
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;
    private final String[] allImageColumns = {
            DataBaseContract.ImageEntry._ID,
            DataBaseContract.ImageEntry.IMAGE_COLUMN_PATH,
            DataBaseContract.ImageEntry.IMAGE_COLUMN_NOTE_ID
    };

    public DatabaseSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

//    public void createImage(String imagePath, long idNote) {
//        ContentValues values = new ContentValues();
//        values.put(DataBaseContract.ImageEntry.IMAGE_COLUMN_PATH, imagePath);
//        values.put(DataBaseContract.ImageEntry.IMAGE_COLUMN_NOTE_ID, idNote);
//        long insertId = database.insert(DataBaseContract.ImageEntry.TABLE_IMAGE, null, values);
//        if(insertId == -1){
//            System.out.println("Lỗi");
////            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        }else {
//            System.out.println("Thành công");
////            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void deleteImage(FileModel fileModel) {
//        long id = fileModel.getId();
//        database.delete(DataBaseContract.ImageEntry.TABLE_IMAGE, DataBaseContract.ImageEntry._ID + " = " + id, null);
//    }
//
//    public List<FileModel> getAllImages() {
//        List<FileModel> fileModels = new ArrayList<>();
//
//        Cursor cursor = database.query(DataBaseContract.ImageEntry.TABLE_IMAGE,
//                allImageColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            FileModel fileModel = cursorToImage(cursor);
//            fileModels.add(fileModel);
//            cursor.moveToNext();
//        }
//        cursor.close();
//
//        return fileModels;
//    }
//
//    private FileModel cursorToImage(Cursor cursor) {
//        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseContract.ImageEntry._ID));
//        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.ImageEntry.IMAGE_COLUMN_PATH));
//        long idNote = cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseContract.ImageEntry.IMAGE_COLUMN_NOTE_ID));
//        return new FileModel("", imagePath);
//    }
}
