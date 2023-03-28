package com.nam.keep.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTE = "my_note";
    private static final String NOTE_COLUMN_ID = "_id";
    private static final String NOTE_COLUMN_TITLE = "note_title";
    private static final String NOTE_COLUMN_CONTENT = "note_content";
    private static final String NOTE_COLUMN_IS_CHECKBOX_OR_CONTENT = "note_is_check_box_or_content";
    private static final String NOTE_COLUMN_COLOR = "note_color";
    private static final String NOTE_COLUMN_BACKGROUND = "note_background";
    private static final String NOTE_COLUMN_CATEGORY_ID = "note_category_id";

    private static final String TABLE_IMAGE = "my_image";
    private static final String IMAGE_COLUMN_ID = "_id";
    private static final String IMAGE_COLUMN_BITMAP = "image_bitmap";
    private static final String IMAGE_COLUMN_NOTE_ID = "image_note_id";

    private static final String TABLE_LABEL = "my_label";
    private static final String LABEL_COLUMN_ID = "_id";
    private static final String LABEL_COLUMN_CONTENT = "label_content";
    private static final String LABEL_COLUMN_NOTE_ID = "label_note_id";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInByte;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NOTE +
                " (" + NOTE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTE_COLUMN_TITLE + " TEXT, " +
                NOTE_COLUMN_CONTENT + " TEXT, " +
                NOTE_COLUMN_IS_CHECKBOX_OR_CONTENT + " INTEGER, " +
                NOTE_COLUMN_COLOR + " TEXT, " +
                NOTE_COLUMN_BACKGROUND + " BLOB, " +
                NOTE_COLUMN_CATEGORY_ID + " INTEGER);";
        sqLiteDatabase.execSQL(query);

        String queryImage = "CREATE TABLE " + TABLE_IMAGE +
                " (" + IMAGE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE_COLUMN_BITMAP + " BLOB, " +
                IMAGE_COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryImage);

        String queryLabel = "CREATE TABLE " + TABLE_LABEL +
                " (" + LABEL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LABEL_COLUMN_CONTENT + " TEXT, " +
                LABEL_COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryLabel);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LABEL);
        onCreate(sqLiteDatabase);
    }

    public void addNote(String title, String content, int isCheckBoxOrContent, int color, Bitmap background, int categoryId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(background != null) {
            Bitmap bitmap = background;

            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInByte = byteArrayOutputStream.toByteArray();
        }

        cv.put(NOTE_COLUMN_TITLE, title);
        cv.put(NOTE_COLUMN_CONTENT, content);
        cv.put(NOTE_COLUMN_IS_CHECKBOX_OR_CONTENT, isCheckBoxOrContent);
        cv.put(NOTE_COLUMN_COLOR, color);
        cv.put(NOTE_COLUMN_BACKGROUND, imageInByte);
        cv.put(NOTE_COLUMN_CATEGORY_ID, categoryId);
        long result = db.insert(TABLE_NOTE,null, cv);
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllNote(){
        String query = "SELECT * FROM " + TABLE_NOTE+ " ORDER BY _id DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String title, String content, int isCheckBoxOrContent, int color, Bitmap background, int categoryId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(background != null) {
            Bitmap bitmap = background;

            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInByte = byteArrayOutputStream.toByteArray();
        }

        cv.put(NOTE_COLUMN_TITLE, title);
        cv.put(NOTE_COLUMN_CONTENT, content);
        cv.put(NOTE_COLUMN_IS_CHECKBOX_OR_CONTENT, isCheckBoxOrContent);
        cv.put(NOTE_COLUMN_COLOR, color);
        cv.put(NOTE_COLUMN_BACKGROUND, imageInByte);
        cv.put(NOTE_COLUMN_CATEGORY_ID, categoryId);

        long result = db.update(TABLE_NOTE, cv, "_id=?", new String[]{row_id});
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NOTE, "_id=?", new String[]{row_id});
        if(result == -1){
//            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTE);
        db.execSQL("DELETE FROM " + TABLE_IMAGE);
        db.execSQL("DELETE FROM " + TABLE_LABEL);
    }

//    -----------------

    public void addImage(Bitmap image, int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(image != null) {
            Bitmap bitmap = image;

            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInByte = byteArrayOutputStream.toByteArray();
        }

        cv.put(IMAGE_COLUMN_BITMAP, imageInByte);
        cv.put(IMAGE_COLUMN_NOTE_ID, noteId);
        long result = db.insert(TABLE_IMAGE,null, cv);
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Added Images Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateImage(String id, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(image != null) {
            Bitmap bitmap = image;

            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInByte = byteArrayOutputStream.toByteArray();
        }

        cv.put(IMAGE_COLUMN_BITMAP, imageInByte);

        long result = db.update(TABLE_IMAGE, cv, "_id=?", new String[]{id});
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Updated Images Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowImage (String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_IMAGE, "_id=?", new String[]{row_id});
        if(result == -1){
//            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(context, "Successfully Image Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getNoteIdNew(){
        String query = "SELECT * FROM " + TABLE_NOTE + " ORDER BY _id DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        int id = 0;
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                id = Integer.parseInt(cursor.getString(0));
            }
        }
        return id;
    }

    public Cursor readNoteImage(int idNote){
        String query = "SELECT * FROM " + TABLE_IMAGE + " WHERE image_note_id = " + idNote;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //    -----------------

    public Cursor readAllLabel(){
        String query = "SELECT * FROM " + TABLE_LABEL+ " ORDER BY _id DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readNoteLabel(int idNote){
        String query = "SELECT * FROM " + TABLE_LABEL + " WHERE label_note_id = " + idNote;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addLabel(String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LABEL_COLUMN_CONTENT, content);
        long result = db.insert(TABLE_LABEL,null, cv);
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Added Labeled Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLabel(String id, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LABEL_COLUMN_CONTENT, content);

        long result = db.update(TABLE_LABEL, cv, "_id=?", new String[]{id});
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Updated Labeled Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLabelIdNote(String id, int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LABEL_COLUMN_NOTE_ID, noteId);

        long result = db.update(TABLE_LABEL, cv, "_id=?", new String[]{id});
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Updated Labeled Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowLabel(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_LABEL, "_id=?", new String[]{row_id});
        if(result == -1){
//            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(context, "Successfully Labeled Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
