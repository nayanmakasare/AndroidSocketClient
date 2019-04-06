package data;

import android.provider.BaseColumns;

/**
 * Created by cognoscis on 6/4/18.
 */

public class ProfileContract {

    private ProfileContract() {
    }

    public static class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "profile_table";

        public static final String COLUMN_PROFILE_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_MOBILE_NUMBER = "mobile_number";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_LANGUAGES = "languages";
        public static final String COLUMN_CONTENT_TYPES = "content_types";
        public static final String COLUMN_FAVOURITES = "favourites";
        public static final String COLUMN_IS_DEFAULT = "isDefault";
        public static final String COLUMN_IS_SELECTED = "isSelected";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DOB + " INTEGER, " +
                COLUMN_MOBILE_NUMBER + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_GENRES + " TEXT, " +
                COLUMN_LANGUAGES + " TEXT, " +
                COLUMN_CONTENT_TYPES + " TEXT, " +
                COLUMN_FAVOURITES + " TEXT, " +
                COLUMN_IS_DEFAULT + " INTEGER, " +
                COLUMN_IS_SELECTED + " INTEGER " +
                ")";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
