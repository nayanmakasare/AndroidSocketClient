package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.ProfileObject;

/**
 * Created by cognoscis on 17/1/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cloudwalker_launcher";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(HistoryContract.HistoryEntry.CREATE_TABLE);
//        sqLiteDatabase.execSQL(FavouritesContract.FavouritesEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(ProfileContract.ProfileEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL(HistoryContract.HistoryEntry.DELETE_TABLE);
//        sqLiteDatabase.execSQL(FavouritesContract.FavouritesEntry.DELETE_TABLE);
        sqLiteDatabase.execSQL(ProfileContract.ProfileEntry.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Creating a History entry
     */
//    public boolean createHistoryEntry(HistoryObject historyObject) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TITLE, historyObject.getTitle());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, historyObject.getTimestamp());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI, historyObject.getImageUri());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID, historyObject.getTileId());
//        //insert one row
//        long id = db.insertWithOnConflict(HistoryContract.HistoryEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
//        if (id > 0) {
//            return true;
//        } else {
//            int result = db.update(HistoryContract.HistoryEntry.TABLE_NAME, contentValues, HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID + " =? ",
//                    new String[]{String.valueOf(historyObject.getTileId())});
//            return result > 0;
//        }
//    }

//    public ArrayList<HistoryObject> getAllHistoryEntries() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT * FROM " + HistoryContract.HistoryEntry.TABLE_NAME + " ORDER BY "
//                + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//        ArrayList<HistoryObject> HistoryList = new ArrayList<>();
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c.moveToFirst()) {
//            do {
//                HistoryObject HistoryObject = new HistoryObject();
//                HistoryObject.setId(c.getInt(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_ID)));
//                HistoryObject.setTitle(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TITLE)));
//                HistoryObject.setTimestamp(c.getInt(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP)));
//                HistoryObject.setImageUri(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI)));
//                HistoryObject.setTileId(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID)));
//
//                HistoryList.add(HistoryObject);
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        return HistoryList;
//    }

//    public ArrayList<HistoryObject> getAllHistoryEntriesBetween(int from, int to) {
//        ArrayList<HistoryObject> HistoryList = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + HistoryContract.HistoryEntry.TABLE_NAME +
//                " WHERE " + HistoryContract.HistoryEntry.COLUMN_NAME_ID + " BETWEEN " + from + " AND "
//                + to + " ORDER BY " + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//        /*String selectQuery = "SELECT * FROM " + HistoryContract.HistoryEntry.TABLE_NAME +
//                " WHERE " + HistoryContract.HistoryEntry.COLUMN_NAME_ID + " > " + oid + " ORDER BY " + HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI + " DESC LIMIT ";*/
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to news_list
//        if (c.moveToFirst()) {
//            do {
//                HistoryObject HistoryObject = new HistoryObject();
//                HistoryObject.setId(c.getInt(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_ID)));
//                HistoryObject.setTitle(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TITLE)));
//                HistoryObject.setTimestamp(c.getLong(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP)));
//                HistoryObject.setImageUri(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI)));
//                HistoryObject.setTileId(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID)));
//
//                HistoryList.add(HistoryObject);
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        // return contact news_list
//        return HistoryList;
//    }

    /**
     * Fetch single History
     */
//    public HistoryObject getOneHistory(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT  * FROM " + HistoryContract.HistoryEntry.TABLE_NAME + " WHERE "
//                + HistoryContract.HistoryEntry.COLUMN_NAME_ID + " = " + id;
//
//        Cursor c = db.rawQuery(selectQuery, null);
//        HistoryObject HistoryObject = new HistoryObject();
//        if (c != null) {
//            c.moveToFirst();
//            HistoryObject.setId(c.getInt(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_ID)));
//            HistoryObject.setTitle(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TITLE)));
//            HistoryObject.setTimestamp(c.getLong(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP)));
//            HistoryObject.setImageUri(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI)));
//            HistoryObject.setTileId(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID)));
//            c.close();
//        }
//
//        return HistoryObject;
//    }

    /**
     * Update an History
     */
//    public boolean updateHistory(HistoryObject HistoryObject) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TITLE, HistoryObject.getTitle());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, HistoryObject.getTimestamp());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_IMAGE_URI, HistoryObject.getImageUri());
//        contentValues.put(HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID, HistoryObject.getTileId());
//
//        int result = db.update(HistoryContract.HistoryEntry.TABLE_NAME, contentValues, HistoryContract.HistoryEntry.COLUMN_NAME_TILE_ID + " =? ",
//                new String[]{String.valueOf(HistoryObject.getTileId())});
//
//        return result > 0;
//    }

    /**
     * Delete an History
     */
//    public boolean deleteHistory(int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        int result = db.delete(HistoryContract.HistoryEntry.TABLE_NAME, HistoryContract.HistoryEntry.COLUMN_NAME_ID + " =? ",
//                new String[]{String.valueOf(id)});
//
//        return result > 0;
//    }

    /**
     * Is tile present in favourite table
     */
//    public boolean isAlreadyFavourited(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT  * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME + " WHERE "
//                + FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID + " = \"" + id + "\"";
//
//        Cursor c = db.rawQuery(selectQuery, null);
//        FavouritesObject FavouritesObject = new FavouritesObject();
//        if (c != null) {
//            return c.moveToFirst();
//        }
//
//        return false;
//    }

    /**
     * Creating a Favourites entry
     */
//    public boolean createFavouritesEntry(FavouritesObject FavouritesObject) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE, FavouritesObject.getTitle());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP, FavouritesObject.getTimestamp());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI, FavouritesObject.getImageUri());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID, FavouritesObject.getTileId());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_PROFILE, FavouritesObject.getProfileId());
//        //insert one row
//        long id = db.insertWithOnConflict(FavouritesContract.FavouritesEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
//        return id > 0;
//        /*if(id > 0){
//            return true;
//        }else{
//            int result = db.update(FavouritesContract.FavouritesEntry.TABLE_NAME, contentValues, FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID + " =? ",
//                    new String[] {String.valueOf(FavouritesObject.getTileId())});
//            return result > 0;
//        }*/
//    }

//    public ArrayList<FavouritesObject> getAllFavouritesEntries() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME + " ORDER BY "
//                + FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//        ArrayList<FavouritesObject> FavouritesList = new ArrayList<>();
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c.moveToFirst()) {
//            do {
//                FavouritesObject FavouritesObject = new FavouritesObject();
//                FavouritesObject.setId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_ID)));
//                FavouritesObject.setTitle(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE)));
//                FavouritesObject.setTimestamp(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP)));
//                FavouritesObject.setImageUri(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI)));
//                FavouritesObject.setTileId(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID)));
//                FavouritesObject.setProfileId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_PROFILE)));
//
//                FavouritesList.add(FavouritesObject);
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        return FavouritesList;
//    }

//    public ArrayList<FavouritesObject> getSelectedProfileFavs(int profileId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME + " WHERE " +
//                FavouritesContract.FavouritesEntry.COLUMN_PROFILE + " = " + Integer.toString(profileId) +
//                " ORDER BY " + FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//        ArrayList<FavouritesObject> FavouritesList = new ArrayList<>();
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c.moveToFirst()) {
//            do {
//                FavouritesObject FavouritesObject = new FavouritesObject();
//                FavouritesObject.setId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_ID)));
//                FavouritesObject.setTitle(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE)));
//                FavouritesObject.setTimestamp(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP)));
//                FavouritesObject.setImageUri(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI)));
//                FavouritesObject.setTileId(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID)));
//                FavouritesObject.setProfileId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_PROFILE)));
//
//                FavouritesList.add(FavouritesObject);
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        return FavouritesList;
//    }
//
//    public ArrayList<FavouritesObject> getAllFavouritesEntriesBetween(int from, int to) {
//        ArrayList<FavouritesObject> FavouritesList = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME +
//                " WHERE " + FavouritesContract.FavouritesEntry.COLUMN_NAME_ID + " BETWEEN " + from + " AND "
//                + to + " ORDER BY " + FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP + " DESC";
//
//    /*String selectQuery = "SELECT * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME +
//            " WHERE " + FavouritesContract.FavouritesEntry.COLUMN_NAME_ID + " > " + oid + " ORDER BY " + FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI + " DESC LIMIT ";*/
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to news_list
//        if (c.moveToFirst()) {
//            do {
//                FavouritesObject FavouritesObject = new FavouritesObject();
//                FavouritesObject.setId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_ID)));
//                FavouritesObject.setTitle(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE)));
//                FavouritesObject.setTimestamp(c.getLong(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP)));
//                FavouritesObject.setImageUri(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI)));
//                FavouritesObject.setTileId(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID)));
//                FavouritesObject.setProfileId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_PROFILE)));
//
//                FavouritesList.add(FavouritesObject);
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        // return contact news_list
//        return FavouritesList;
//    }
//
//    /**
//     * Fetch single Favourites
//     */
//    public FavouritesObject getOneFavourites(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT  * FROM " + FavouritesContract.FavouritesEntry.TABLE_NAME + " WHERE "
//                + FavouritesContract.FavouritesEntry.COLUMN_NAME_ID + " = " + id;
//
//        Cursor c = db.rawQuery(selectQuery, null);
//        FavouritesObject FavouritesObject = new FavouritesObject();
//        if (c != null) {
//            c.moveToFirst();
//            FavouritesObject.setId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_ID)));
//            FavouritesObject.setTitle(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE)));
//            FavouritesObject.setTimestamp(c.getLong(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP)));
//            FavouritesObject.setImageUri(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI)));
//            FavouritesObject.setTileId(c.getString(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID)));
//            FavouritesObject.setProfileId(c.getInt(c.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_PROFILE)));
//            c.close();
//        }
//
//        return FavouritesObject;
//    }
//
//    /**
//     * Update an Favourites
//     */
//    public boolean updateFavourites(FavouritesObject FavouritesObject) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE, FavouritesObject.getTitle());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TIMESTAMP, FavouritesObject.getTimestamp());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_IMAGE_URI, FavouritesObject.getImageUri());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID, FavouritesObject.getTileId());
//        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_PROFILE, FavouritesObject.getProfileId());
//
//        int result = db.update(FavouritesContract.FavouritesEntry.TABLE_NAME, contentValues, FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID + " =? ",
//                new String[]{String.valueOf(FavouritesObject.getTileId())});
//
//        return result > 0;
//    }
//
//    /**
//     * Delete an Favourites
//     */
//    public boolean deleteFavourites(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        int result = db.delete(FavouritesContract.FavouritesEntry.TABLE_NAME, FavouritesContract.FavouritesEntry.COLUMN_NAME_TILE_ID + " =? ",
//                new String[]{String.valueOf(id)});
//
//        return result > 0;
//    }

    /**
     * Creating a Profile entry
     */
    public int createProfileEntry(ProfileObject profileObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_NAME, profileObject.getName());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_DOB, profileObject.getDob());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_MOBILE_NUMBER, profileObject.getMobileNumber());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_GENDER, profileObject.getGender());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_GENRES, profileObject.getGenresString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_LANGUAGES, profileObject.getLanguagesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_CONTENT_TYPES, profileObject.getContentTypesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_FAVOURITES, profileObject.getFavouritesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_IS_DEFAULT, profileObject.isDefault() ? 1 : 0);
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_IS_SELECTED, profileObject.isSelected() ? 1 : 0);
        //insert one row
        long id = db.insertWithOnConflict(ProfileContract.ProfileEntry.TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id > 0) {
            return (int) id;
        } else {
            int result = db.update(ProfileContract.ProfileEntry.TABLE_NAME, contentValues,
                    ProfileContract.ProfileEntry.COLUMN_PROFILE_ID + " =? ",
                    new String[]{String.valueOf(profileObject.getId())});
            return (int) result;
        }
    }

    public ArrayList<ProfileObject> getAllProfileEntries() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ProfileContract.ProfileEntry.TABLE_NAME;

        ArrayList<ProfileObject> profileList = new ArrayList<>();

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ProfileObject ProfileObject = new ProfileObject();
                ProfileObject.setId(c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_PROFILE_ID)));
                ProfileObject.setName(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_NAME)));
                ProfileObject.setDob(c.getLong(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_DOB)));
                ProfileObject.setMobileNumber(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_MOBILE_NUMBER)));
                ProfileObject.setGender(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENDER)));
                ProfileObject.setGenresString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENRES)));
                ProfileObject.setLanguagesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_LANGUAGES)));
                ProfileObject.setContentTypesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_CONTENT_TYPES)));
                ProfileObject.setFavouritesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_FAVOURITES)));

                int def = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_DEFAULT));
                ProfileObject.setDefault(def == 1);

                int selected = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_SELECTED));
                ProfileObject.setSelected(selected == 1);

                profileList.add(ProfileObject);
            } while (c.moveToNext());
        }
        c.close();

        return profileList;
    }

    /**
     * Fetch single Profile
     */
    public ProfileObject getOneProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ProfileContract.ProfileEntry.TABLE_NAME + " WHERE "
                + ProfileContract.ProfileEntry.COLUMN_PROFILE_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        ProfileObject profileObject = null;
        if (c != null && c.moveToFirst()) {
            profileObject = new ProfileObject();
            profileObject.setId(c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_PROFILE_ID)));
            profileObject.setName(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_NAME)));
            profileObject.setDob(c.getLong(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_DOB)));
            profileObject.setMobileNumber(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_MOBILE_NUMBER)));
            profileObject.setGender(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENDER)));
            profileObject.setGenresString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENRES)));
            profileObject.setLanguagesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_LANGUAGES)));
            profileObject.setContentTypesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_CONTENT_TYPES)));
            profileObject.setFavouritesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_FAVOURITES)));

            int def = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_DEFAULT));
            profileObject.setDefault(def == 1);

            int selected = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_SELECTED));
            profileObject.setSelected(selected == 1);

            c.close();
        }

        return profileObject;
    }

    /**
     * Update a Profile
     */
    public boolean updateProfile(ProfileObject profileObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_NAME, profileObject.getName());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_DOB, profileObject.getDob());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_MOBILE_NUMBER, profileObject.getMobileNumber());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_GENDER, profileObject.getGender());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_GENRES, profileObject.getGenresString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_LANGUAGES, profileObject.getLanguagesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_CONTENT_TYPES, profileObject.getContentTypesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_FAVOURITES, profileObject.getFavouritesString());
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_IS_DEFAULT, profileObject.isDefault() ? 1 : 0);
        contentValues.put(ProfileContract.ProfileEntry.COLUMN_IS_SELECTED, profileObject.isSelected() ? 1 : 0);


        int result = db.update(ProfileContract.ProfileEntry.TABLE_NAME, contentValues,
                ProfileContract.ProfileEntry.COLUMN_PROFILE_ID + " =? ",
                new String[]{String.valueOf(profileObject.getId())});

        return result > 0;
    }

    /**
     * Delete a Profile
     */
    public boolean deleteProfile(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(ProfileContract.ProfileEntry.TABLE_NAME,
                ProfileContract.ProfileEntry.COLUMN_PROFILE_ID + " =? ",
                new String[]{String.valueOf(id)});

        return result > 0;
    }

    public ProfileObject getSelectedProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + ProfileContract.ProfileEntry.TABLE_NAME + " WHERE "
                + ProfileContract.ProfileEntry.COLUMN_IS_SELECTED + " = 1";

        Cursor c = db.rawQuery(selectQuery, null);
        ProfileObject profileObject = null;
        if (c.moveToFirst()) {
            profileObject = new ProfileObject();
            profileObject.setId(c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_PROFILE_ID)));
            profileObject.setName(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_NAME)));
            profileObject.setDob(c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_DOB)));
            profileObject.setMobileNumber(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_MOBILE_NUMBER)));
            profileObject.setGender(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENDER)));
            profileObject.setGenresString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_GENRES)));
            profileObject.setLanguagesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_LANGUAGES)));
            profileObject.setContentTypesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_CONTENT_TYPES)));
            profileObject.setFavouritesString(c.getString(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_FAVOURITES)));

            int def = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_DEFAULT));
            profileObject.setDefault(def == 1);

            int selected = c.getInt(c.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_IS_SELECTED));
            profileObject.setSelected(selected == 1);

            c.close();
        }
        c.close();

        return profileObject;
    }

    public boolean isProfileNamePresent(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + ProfileContract.ProfileEntry.TABLE_NAME + " WHERE "
                + ProfileContract.ProfileEntry.COLUMN_NAME + " = '" + name + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        return c.moveToFirst();
    }
}
