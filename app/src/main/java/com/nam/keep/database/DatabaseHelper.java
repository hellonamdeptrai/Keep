package com.nam.keep.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nam.keep.model.FileModel;
import com.nam.keep.model.Note;

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
        String queryUser = "CREATE TABLE " + DataBaseContract.UserEntry.TABLE +
                " (" + DataBaseContract.UserEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.UserEntry.COLUMN_NAME + " TEXT, " +
                DataBaseContract.UserEntry.COLUMN_AVATAR + " TEXT, " +
                DataBaseContract.UserEntry.COLUMN_EMAIL + " TEXT, " +
                DataBaseContract.UserEntry.COLUMN_PASSWORD + " TEXT );";
        sqLiteDatabase.execSQL(queryUser);

        String queryNote = "CREATE TABLE " + DataBaseContract.NoteEntry.TABLE +
                " (" + DataBaseContract.NoteEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.NoteEntry.COLUMN_INDEX + " INTEGER, " +
                DataBaseContract.NoteEntry.COLUMN_TITLE + " TEXT, " +
                DataBaseContract.NoteEntry.COLUMN_CONTENT + " TEXT, " +
                DataBaseContract.NoteEntry.COLUMN_IS_CHECKBOX_OR_CONTENT + " INTEGER, " +
                DataBaseContract.NoteEntry.COLUMN_DEADLINE + " DATETIME, " +
                DataBaseContract.NoteEntry.COLUMN_COLOR + " INTEGER, " +
                DataBaseContract.NoteEntry.COLUMN_BACKGROUND + " BLOB, " +
                DataBaseContract.NoteEntry.COLUMN_UPDATED_AT + " DATETIME, " +
                DataBaseContract.NoteEntry.COLUMN_USER_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryNote);

        String queryImage = "CREATE TABLE " + DataBaseContract.ImageEntry.TABLE +
                " (" + DataBaseContract.ImageEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.ImageEntry.COLUMN_PATH + " TEXT, " +
                DataBaseContract.ImageEntry.COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryImage);

        String queryFile = "CREATE TABLE " + DataBaseContract.FileEntry.TABLE +
                " (" + DataBaseContract.FileEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.FileEntry.COLUMN_PATH + " TEXT, " +
                DataBaseContract.FileEntry.COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryFile);

        String queryLabel = "CREATE TABLE " + DataBaseContract.LabelEntry.TABLE +
                " (" + DataBaseContract.LabelEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.LabelEntry.COLUMN_TITLE + " TEXT);";
        sqLiteDatabase.execSQL(queryLabel);

        String queryNoteHasLabel = "CREATE TABLE " + DataBaseContract.NoteHasLabelEntry.TABLE +
                " (" + DataBaseContract.NoteHasLabelEntry.COLUMN_LABEL_ID + " INTEGER, " +
                DataBaseContract.NoteHasLabelEntry.COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryNoteHasLabel);

        String queryNoteHasUser = "CREATE TABLE " + DataBaseContract.NoteHasUserEntry.TABLE +
                " (" + DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID + " INTEGER, " +
                DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(queryNoteHasUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.UserEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.NoteEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.ImageEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.FileEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.LabelEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.NoteHasLabelEntry.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.NoteHasUserEntry.TABLE);
        onCreate(sqLiteDatabase);
    }

    public Cursor getNote(){
        String query = "SELECT * FROM " + DataBaseContract.NoteEntry.TABLE + " ORDER BY _id DESC";
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void createNote(Note note) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.NoteEntry.COLUMN_INDEX, note.getIndex());
        values.put(DataBaseContract.NoteEntry.COLUMN_TITLE, note.getTitle());
        values.put(DataBaseContract.NoteEntry.COLUMN_CONTENT, note.getContent());
        values.put(DataBaseContract.NoteEntry.COLUMN_IS_CHECKBOX_OR_CONTENT, note.getIsCheckBoxOrContent());
        values.put(DataBaseContract.NoteEntry.COLUMN_DEADLINE, note.getDeadline());
        values.put(DataBaseContract.NoteEntry.COLUMN_COLOR, note.getColor());
        values.put(DataBaseContract.NoteEntry.COLUMN_BACKGROUND, note.getBackground());
        values.put(DataBaseContract.NoteEntry.COLUMN_UPDATED_AT, note.getUpdatedAt());
        values.put(DataBaseContract.NoteEntry.COLUMN_USER_ID, note.getUserId());
        long insertId = database.insert(DataBaseContract.NoteEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public int getCountNote() {
        String query = "SELECT COUNT(*) FROM " + DataBaseContract.NoteEntry.TABLE;
        database = this.getReadableDatabase();
        int index = 0;
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                index = Integer.parseInt(cursor.getString(0));
            }
        }
        return index;
    }

    public long getNoteIdNew(){
        String query = "SELECT * FROM " + DataBaseContract.NoteEntry.TABLE + " ORDER BY _id DESC LIMIT 1";
        database = this.getReadableDatabase();
        long id = 0;
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                id = Long.parseLong(cursor.getString(0));
            }
        }
        return id;
    }

    public Cursor readNoteImage(long idNote){
        String query = "SELECT * FROM " + DataBaseContract.ImageEntry.TABLE + " WHERE _note_id = " + idNote;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void createImage(FileModel file) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.ImageEntry.COLUMN_PATH, file.getPath());
        values.put(DataBaseContract.ImageEntry.COLUMN_NOTE_ID, file.getIdNote());
        long insertId = database.insert(DataBaseContract.ImageEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
