package com.nam.keep.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "NamKeep.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryImage = "CREATE TABLE " + DataBaseContract.ImageEntry.TABLE_IMAGE +
                " (" + DataBaseContract.ImageEntry.IMAGE_COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.ImageEntry.IMAGE_COLUMN_PATH + " TEXT, " +
                DataBaseContract.ImageEntry.IMAGE_COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryImage);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.ImageEntry.TABLE_IMAGE);
        onCreate(sqLiteDatabase);
    }

    public void createImage(String imagePath, long idNote) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.ImageEntry.IMAGE_COLUMN_PATH, imagePath);
        values.put(DataBaseContract.ImageEntry.IMAGE_COLUMN_NOTE_ID, idNote);
        long insertId = database.insert(DataBaseContract.ImageEntry.TABLE_IMAGE, null, values);
        if(insertId == -1){
            System.out.println("Lỗi");
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            System.out.println("Thành công");
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
