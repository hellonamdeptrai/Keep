package com.nam.keep.database;

import android.provider.BaseColumns;

public final class DataBaseContract {
    private DataBaseContract() {}

    public static class ImageEntry implements BaseColumns {
        public static final String TABLE_IMAGE = "image";
        public static final String IMAGE_COLUMN_ID = "_id";
        public static final String IMAGE_COLUMN_PATH = "image_path";
        public static final String IMAGE_COLUMN_NOTE_ID = "image_note_id";
    }
}
