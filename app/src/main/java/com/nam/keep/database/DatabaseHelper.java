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
import com.nam.keep.model.Label;
import com.nam.keep.model.Note;
import com.nam.keep.model.User;
import com.nam.keep.utils.UtilsFunction;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "NamKeep.db";
    private static final int DATABASE_VERSION = 1;
//    private final String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

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
                DataBaseContract.UserEntry.COLUMN_PASSWORD + " TEXT, " +
                DataBaseContract.UserEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.UserEntry.COLUMN_IS_SYNC + " INTEGER );";
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
                DataBaseContract.NoteEntry.COLUMN_USER_ID + " INTEGER, " +
                DataBaseContract.NoteEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.NoteEntry.COLUMN_IS_SYNC + " INTEGER );";
        sqLiteDatabase.execSQL(queryNote);

        String queryImage = "CREATE TABLE " + DataBaseContract.ImageEntry.TABLE +
                " (" + DataBaseContract.ImageEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.ImageEntry.COLUMN_PATH + " TEXT, " +
                DataBaseContract.ImageEntry.COLUMN_NOTE_ID + " INTEGER, " +
                DataBaseContract.ImageEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.ImageEntry.COLUMN_IS_SYNC + " INTEGER );";
        sqLiteDatabase.execSQL(queryImage);

        String queryFile = "CREATE TABLE " + DataBaseContract.FileEntry.TABLE +
                " (" + DataBaseContract.FileEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.FileEntry.COLUMN_PATH + " TEXT, " +
                DataBaseContract.FileEntry.COLUMN_NOTE_ID + " INTEGER, " +
                DataBaseContract.FileEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.FileEntry.COLUMN_IS_SYNC + " INTEGER );";
        sqLiteDatabase.execSQL(queryFile);

        String queryLabel = "CREATE TABLE " + DataBaseContract.LabelEntry.TABLE +
                " (" + DataBaseContract.LabelEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseContract.LabelEntry.COLUMN_TITLE + " TEXT, " +
                DataBaseContract.LabelEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.LabelEntry.COLUMN_IS_SYNC + " INTEGER, " +
                DataBaseContract.LabelEntry.COLUMN_USER_ID + " INTEGER );";
        sqLiteDatabase.execSQL(queryLabel);

        String queryNoteHasLabel = "CREATE TABLE " + DataBaseContract.NoteHasLabelEntry.TABLE +
                " (" + DataBaseContract.NoteHasLabelEntry.COLUMN_LABEL_ID + " INTEGER, " +
                DataBaseContract.NoteHasLabelEntry.COLUMN_NOTE_ID + " INTEGER, " +
                DataBaseContract.NoteHasLabelEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.NoteHasLabelEntry.COLUMN_IS_SYNC + " INTEGER );";
        sqLiteDatabase.execSQL(queryNoteHasLabel);

        String queryNoteHasUser = "CREATE TABLE " + DataBaseContract.NoteHasUserEntry.TABLE +
                " (" + DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID + " INTEGER, " +
                DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID + " INTEGER, " +
                DataBaseContract.NoteHasUserEntry.COLUMN_UPDATED_AT + " TIMESTAMP, " +
                DataBaseContract.NoteHasUserEntry.COLUMN_IS_SYNC + " INTEGER );";
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

    public void createUser(User user) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.UserEntry.COLUMN_ID, user.getId());
        values.put(DataBaseContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(DataBaseContract.UserEntry.COLUMN_AVATAR, user.getAvatar());
        values.put(DataBaseContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(DataBaseContract.UserEntry.COLUMN_PASSWORD, user.getPassword());
        values.put(DataBaseContract.UserEntry.COLUMN_UPDATED_AT, user.getUpdated_at());
        values.put(DataBaseContract.UserEntry.COLUMN_IS_SYNC, user.getIsSync());
        long insertId = database.insert(DataBaseContract.UserEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUser(User user){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(DataBaseContract.UserEntry.COLUMN_AVATAR, user.getAvatar());
        values.put(DataBaseContract.UserEntry.COLUMN_UPDATED_AT, user.getUpdated_at());
        values.put(DataBaseContract.UserEntry.COLUMN_IS_SYNC, user.getIsSync());
        long insertId = database.update(DataBaseContract.UserEntry.TABLE, values, DataBaseContract.NoteEntry.COLUMN_ID+
                "=?", new String[]{user.getId()+""});
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getUserDetail(long id){
        String query = "SELECT * FROM " + DataBaseContract.UserEntry.TABLE + " WHERE " +
                DataBaseContract.UserEntry.COLUMN_ID + " = " + id;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }


    public Cursor getUser() {
        String query = "SELECT * FROM " + DataBaseContract.UserEntry.TABLE;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getUserNotMe(long id) {
        String query = "SELECT * FROM " + DataBaseContract.UserEntry.TABLE + " WHERE " +
                DataBaseContract.UserEntry.COLUMN_ID + " != " + id;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteUser(long idUser){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.UserEntry.TABLE,
                DataBaseContract.UserEntry.COLUMN_ID+"=?", new String[]{idUser+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteUserSync(int isSync){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.UserEntry.TABLE,
                DataBaseContract.UserEntry.COLUMN_IS_SYNC+"=?", new String[]{isSync+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean login(User user) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseContract.UserEntry.TABLE, null,
                DataBaseContract.UserEntry.COLUMN_EMAIL + "=?", new String[]{user.getEmail()},
                null, null, null);

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(4);
            if (storedPassword.equals(user.getPassword())) {
                // Đăng nhập thành công
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }

    public Cursor getNote(){
        String query = "SELECT * FROM " + DataBaseContract.NoteEntry.TABLE + " ORDER BY "+
                DataBaseContract.NoteEntry.COLUMN_INDEX+" DESC";
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
        values.put(DataBaseContract.NoteEntry.COLUMN_USER_ID, note.getUserId());
        values.put(DataBaseContract.NoteEntry.COLUMN_UPDATED_AT, note.getUpdatedAt());
        values.put(DataBaseContract.NoteEntry.COLUMN_IS_SYNC, note.getIsSync());
        long insertId = database.insert(DataBaseContract.NoteEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed note", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote(Note note){
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
//        values.put(DataBaseContract.NoteEntry.COLUMN_UPDATED_AT, note.getUpdated_at());
        long insertId = database.update(DataBaseContract.NoteEntry.TABLE, values, DataBaseContract.NoteEntry.COLUMN_ID+
                "=?", new String[]{note.getId()+""});
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNote(long idNote){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.NoteEntry.TABLE,
                DataBaseContract.NoteEntry.COLUMN_ID+"=?", new String[]{idNote+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
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

    public Cursor getNoteDetail(long id){
        String query = "SELECT * FROM " + DataBaseContract.NoteEntry.TABLE + " WHERE " +
                DataBaseContract.NoteEntry.COLUMN_ID + " = " + id;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
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
        values.put(DataBaseContract.ImageEntry.COLUMN_UPDATED_AT, file.getUpdated_at());
        long insertId = database.insert(DataBaseContract.ImageEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteImageInNote(long idNote){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.ImageEntry.TABLE,
                DataBaseContract.ImageEntry.COLUMN_NOTE_ID+"=?", new String[]{idNote+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void createFile(FileModel file) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.FileEntry.COLUMN_PATH, file.getPath());
        values.put(DataBaseContract.FileEntry.COLUMN_NOTE_ID, file.getIdNote());
        values.put(DataBaseContract.FileEntry.COLUMN_UPDATED_AT, file.getUpdated_at());
        long insertId = database.insert(DataBaseContract.FileEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFileInNote(long idNote){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.FileEntry.TABLE,
                DataBaseContract.FileEntry.COLUMN_NOTE_ID+"=?", new String[]{idNote+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getFileNote(long idNote){
        String query = "SELECT * FROM " + DataBaseContract.FileEntry.TABLE + " WHERE " +
                DataBaseContract.FileEntry.COLUMN_NOTE_ID + " = " + idNote;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void createLabel(Label label) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.LabelEntry.COLUMN_TITLE, label.getTitle());
        values.put(DataBaseContract.LabelEntry.COLUMN_UPDATED_AT, label.getUpdated_at());
        values.put(DataBaseContract.LabelEntry.COLUMN_USER_ID, label.getUserId());
        long insertId = database.insert(DataBaseContract.LabelEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getLabel(long userId){
        String query = "SELECT * FROM " + DataBaseContract.LabelEntry.TABLE + " WHERE " +
                DataBaseContract.LabelEntry.COLUMN_USER_ID + " = " + userId + " ORDER BY " +
                DataBaseContract.LabelEntry.COLUMN_ID + " DESC";
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateLabel(Label label){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseContract.LabelEntry.COLUMN_TITLE, label.getTitle());
        values.put(DataBaseContract.LabelEntry.COLUMN_UPDATED_AT, label.getUpdated_at());

        long insertId = database.update(DataBaseContract.LabelEntry.TABLE, values, "_id=?", new String[]{label.getId()+""});
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteLabel(Label label){
        database = this.getWritableDatabase();
        long insertId = database.delete(DataBaseContract.LabelEntry.TABLE, "_id=?", new String[]{label.getId()+""});
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void attachLabel(long idNote, long idLabel){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.NoteHasLabelEntry.COLUMN_NOTE_ID, idNote);
        values.put(DataBaseContract.NoteHasLabelEntry.COLUMN_LABEL_ID, idLabel);
//        values.put(DataBaseContract.NoteHasLabelEntry.COLUMN_UPDATED_AT, user.getUpdated_at());
        long insertId = database.insert(DataBaseContract.NoteHasLabelEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void detachLabel(long idNote){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.NoteHasLabelEntry.TABLE,
                DataBaseContract.NoteHasLabelEntry.COLUMN_NOTE_ID+"=?", new String[]{idNote+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getLabelNote(long idNote){
        String query = "SELECT " + DataBaseContract.LabelEntry.TABLE + ".* FROM " + DataBaseContract.NoteHasLabelEntry.TABLE +
        " JOIN " + DataBaseContract.LabelEntry.TABLE + " ON " +
                DataBaseContract.LabelEntry.TABLE +"." +DataBaseContract.LabelEntry.COLUMN_ID + " = " +
                DataBaseContract.NoteHasLabelEntry.TABLE + "." + DataBaseContract.NoteHasLabelEntry.COLUMN_LABEL_ID +
                " WHERE " + DataBaseContract.NoteHasLabelEntry.TABLE +"." +DataBaseContract.NoteHasLabelEntry.COLUMN_NOTE_ID + " = " +
                idNote;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void attachUser(long idNote, long idUser){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID, idNote);
        values.put(DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID, idUser);
//        values.put(DataBaseContract.NoteHasLabelEntry.COLUMN_UPDATED_AT, user.getUpdated_at());
        long insertId = database.insert(DataBaseContract.NoteHasUserEntry.TABLE, null, values);
        if(insertId == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void detachUser(long idNote){
        database = this.getWritableDatabase();
        long result = database.delete(DataBaseContract.NoteHasUserEntry.TABLE,
                DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID+"=?", new String[]{idNote+""});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getUserNote(long idNote){
        String query = "SELECT " + DataBaseContract.UserEntry.TABLE + ".* FROM " + DataBaseContract.NoteHasUserEntry.TABLE +
                " JOIN " + DataBaseContract.UserEntry.TABLE + " ON " +
                DataBaseContract.UserEntry.TABLE +"." +DataBaseContract.UserEntry.COLUMN_ID + " = " +
                DataBaseContract.NoteHasUserEntry.TABLE + "." + DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID +
                " WHERE " + DataBaseContract.NoteHasUserEntry.TABLE +"." +DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID + " = " +
                idNote;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void detachNote(long idNote, long idUser){
        database = this.getWritableDatabase();
        String selection = DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID + "=? AND " +
                DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID + "=?";
        String[] selectionArgs = {String.valueOf(idUser), String.valueOf(idNote)};
        long result = database.delete(DataBaseContract.NoteHasUserEntry.TABLE, selection, selectionArgs);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getNoteUser(long idUser){
        String query = "SELECT " + DataBaseContract.NoteEntry.TABLE + ".* FROM " + DataBaseContract.NoteHasUserEntry.TABLE +
                " JOIN " + DataBaseContract.NoteEntry.TABLE + " ON " +
                DataBaseContract.NoteEntry.TABLE +"." +DataBaseContract.NoteEntry.COLUMN_ID + " = " +
                DataBaseContract.NoteHasUserEntry.TABLE + "." + DataBaseContract.NoteHasUserEntry.COLUMN_NOTE_ID +
                " WHERE " + DataBaseContract.NoteHasUserEntry.TABLE +"." +DataBaseContract.NoteHasUserEntry.COLUMN_USER_ID + " = " +
                idUser;
        database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }
}
