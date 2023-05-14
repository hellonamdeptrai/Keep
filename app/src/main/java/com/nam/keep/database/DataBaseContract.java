package com.nam.keep.database;

import android.provider.BaseColumns;

public final class DataBaseContract {
    private DataBaseContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE = "user";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "_name";
        public static final String COLUMN_AVATAR = "_avatar";
        public static final String COLUMN_EMAIL = "_email";
        public static final String COLUMN_PASSWORD = "_password";
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE = "note";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_INDEX = "_index";
        public static final String COLUMN_TITLE = "_title";
        public static final String COLUMN_CONTENT = "_content";
        public static final String COLUMN_IS_CHECKBOX_OR_CONTENT = "_is_check_box_or_content";
        public static final String COLUMN_DEADLINE = "_deadline";
        public static final String COLUMN_COLOR = "_color";
        public static final String COLUMN_BACKGROUND = "_background";
        public static final String COLUMN_UPDATED_AT = "_updated_at";
        public static final String COLUMN_USER_ID = "_user_id";
    }

    public static class ImageEntry implements BaseColumns {
        public static final String TABLE = "image";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PATH = "_path";
        public static final String COLUMN_NOTE_ID = "_note_id";
    }

    public static class FileEntry implements BaseColumns {
        public static final String TABLE = "file";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PATH = "_path";
        public static final String COLUMN_NOTE_ID = "_note_id";
    }

    public static class LabelEntry implements BaseColumns {
        public static final String TABLE = "label";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "_title";
    }

    public static class NoteHasLabelEntry implements BaseColumns {
        public static final String TABLE = "note_has_label";
        public static final String COLUMN_LABEL_ID = "_label_id";
        public static final String COLUMN_NOTE_ID = "_note_id";
    }

    public static class NoteHasUserEntry implements BaseColumns {
        public static final String TABLE = "note_has_user";
        public static final String COLUMN_USER_ID = "_user_id";
        public static final String COLUMN_NOTE_ID = "_note_id";
    }
}
