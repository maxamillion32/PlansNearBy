package com.arroyo.nolberto.placeswithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.Venue;

/**
 * Created by nolbertoarroyo on 9/14/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Favorites.db";

        // created DataBaseHelper constructor
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //get instance method for DataBaseHelper
        private static DataBaseHelper instance;

        public static DataBaseHelper getInstance(Context context) {
            if (instance == null) {
                instance = new DataBaseHelper(context.getApplicationContext());
            }
            return instance;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES_FAVORITES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(SQL_DELETE_ENTRIES_FAVORITES);
            onCreate(db);

        }

        //Using baseColumns to create final properties
        public static abstract class DataEntryFavorites implements BaseColumns {
            public static final String TABLE_NAME = "Favorites";
            public static final String COL_TITLE = "TITLE";
            public static final String COL_ITEM_ID = "ITEM_ID";
            public static final String COL_ID = "_id";
            public static final String COL_CATEGORY = "CATEGORY";
            public static final String COL_CITY = "CITY";
            public static final String COL_RATING = "RATING";
            public static final String COL_RATING_COLOR ="RATING_COLOR";

        }
        //Create Favorites table
        private static final String SQL_CREATE_ENTRIES_FAVORITES = "CREATE TABLE " +
                DataEntryFavorites.TABLE_NAME + " (" +
                DataEntryFavorites.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataEntryFavorites.COL_TITLE + " TEXT," +
                DataEntryFavorites.COL_CATEGORY + " TEXT," +
                DataEntryFavorites.COL_CITY + " TEXT," +
                DataEntryFavorites.COL_RATING + " TEXT," +
                DataEntryFavorites.COL_RATING_COLOR + " TEXT," +
                DataEntryFavorites.COL_ITEM_ID + " TEXT)";

        private static final String SQL_DELETE_ENTRIES_FAVORITES = "DROP TABLE IF EXISTS " +
                DataEntryFavorites.TABLE_NAME;

        //creating favorites columns
        public static final String[] FAVORITES_COLUMNS = {DataEntryFavorites._ID,DataEntryFavorites.COL_ITEM_ID,DataEntryFavorites.COL_TITLE, DataEntryFavorites.COL_CITY,DataEntryFavorites.COL_RATING,DataEntryFavorites.COL_CATEGORY,DataEntryFavorites.COL_RATING_COLOR};

        // method for inserting Articles properties into favorites table
        public void insertRowFavorities(Venue item){

            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DataEntryFavorites.COL_ITEM_ID, item.getId());
            values.put(DataEntryFavorites.COL_TITLE, item.getName());
            values.put(DataEntryFavorites.COL_RATING, item.getRating().toString());
            values.put(DataEntryFavorites.COL_CITY, item.getLocation().getCity());
            values.put(DataEntryFavorites.COL_CATEGORY, item.getCategories().get(0).getName());
            values.put(DataEntryFavorites.COL_RATING_COLOR, item.getRatingColor());
            database.insertOrThrow(DataEntryFavorites.TABLE_NAME,null,values);
        }

        public Cursor getFavoritesList() {
            //returning favorites list table with cursor

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(DataEntryFavorites.TABLE_NAME, // a. table
                    FAVORITES_COLUMNS, // b. column names
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            return cursor;
        }
        //deleting venue from favorites table by id
        public int deleteFavoritesItem(String id) {
            SQLiteDatabase db = getWritableDatabase();
            int deleteNum = db.delete(DataEntryFavorites.TABLE_NAME,
                    DataEntryFavorites.COL_ITEM_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.close();
            return deleteNum;
        }
        //get venue by id
        public Cursor getVenueById(String id) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(DataEntryFavorites.TABLE_NAME, // a. table
                    FAVORITES_COLUMNS, // b. column names
                    DataEntryFavorites.COL_ITEM_ID + " = ?", // c. selections
                    new String[]{String.valueOf(id)}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

            return cursor;
        }

        public Boolean exists(String id) {
            boolean exists = false;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(DataEntryFavorites.TABLE_NAME, // a. table
                    FAVORITES_COLUMNS, // b. column names
                    DataEntryFavorites.COL_ITEM_ID + " = ?", // c. selections
                    new String[]{String.valueOf(id)}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (cursor.getCount()>=1){
                exists=true;
                cursor.close();
            }

            return exists;
        }




    
}
