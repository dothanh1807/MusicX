package com.example.exercise2.model.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.exercise2.model.entitys.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataSongProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.thanh.data";

    private static final String URL = "content://" + PROVIDER_NAME + "/music";

    public static final Uri CONTENT_DATA_SONG_URI = Uri.parse(URL);

    private SQLiteDatabase mDataBase;

    private static final UriMatcher uriMatcher;

    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "data", 1);
        uriMatcher.addURI(PROVIDER_NAME, "data/*", 1);
    }

    @Override
    public boolean onCreate() {
        SongDataBase songDataBase = new SongDataBase(getContext());
        mDataBase = songDataBase.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_SONG);
        if( uriMatcher.match(uri) == 1){
            queryBuilder.setProjectionMap( values);
        }

        Cursor cursor = queryBuilder.query( mDataBase, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    // Nếu muốn cho bên ngoài sửa đổi dữ liệu của database thì bỏ cmt các method dưới.

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri,ContentValues values) {
//        long rowID = mDataBase.insert( TABLE_SONG, "", values);
//        if( rowID > 0){
//            Uri uri1 = ContentUris.withAppendedId(CONTENT_DATA_SONG_URI, rowID);
//            getContext().getContentResolver().notifyChange( uri1, null);
//            return uri1;
//        }
//        throw new SQLException(" Add data to " + uri +" failed!");
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int rowCount = 0;
//        if( uriMatcher.match(uri) == 1){
//            rowCount = mDataBase.delete( TABLE_SONG, selection, selectionArgs);
//        }
//        getContext().getContentResolver().notifyChange( uri, null);
//
//        return rowCount;
        return 0;
    }

    @Override
    public int update(Uri uri,ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    ////////////////////////////////// DataBase //////////////////////////////////////////////

    public static final String DATABASE_NAME = "DataBaseSong";

    public static final int VERSION = 23;

    public static final String TABLE_SONG = "Music";

    public static final String QUERY_ALL = "SELECT * FROM "  + TABLE_SONG ;

    public static final String COLUMN_ROW_ID = "_Id";

    public static final String COLUMN_SONG_ID = "Music_Id";

    public static final String COLUMN_SONG_NAME_SONG = "Music_Name_Song";

    public static final String COLUMN_SONG_NAME_AUTHOR = "Music_Name_Author";

    public static final String COLUMN_SONG_TIME_SONG = "Music_Duration";

    public static final String COLUMN_SONG_PATH_SONG = "Music_Path";

    public static final String COLUMN_SONG_IMAGE = "Music_Image";

    public static final String SCRIPT = "CREATE TABLE " + TABLE_SONG + "(" +
            COLUMN_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SONG_ID + " TEXT," +
            COLUMN_SONG_NAME_SONG + " TEXT," +
            COLUMN_SONG_NAME_AUTHOR + " TEXT," +
            COLUMN_SONG_TIME_SONG + " TEXT," +
            COLUMN_SONG_PATH_SONG + " TEXT," +
            COLUMN_SONG_IMAGE + " TEXT" + ")";

    public static final class SongDataBase extends SQLiteOpenHelper implements DataBaseHandler{

        private SQLiteDatabase mDatabase;

        public SongDataBase(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
            mDatabase = getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
            onCreate(db);
        }

        @Override
        public long insert(Song song) {
            ContentValues values = getContentValues(song);
            Log.i("XXX",  " insert");
            return mDatabase.insert(TABLE_SONG, null, values);
        }

        @Override
        public int delete(Song song) {
            return mDatabase.delete(TABLE_SONG,
                    COLUMN_SONG_NAME_SONG + " = ?",
                    new String[]{String.valueOf(song.getmNameSong())});
        }

        @Override
        public int update(Song song) {
            ContentValues values = getContentValues(song);

            return mDatabase.update(TABLE_SONG,
                    values,
                    COLUMN_SONG_NAME_SONG + " = ?",
                    new String[]{String.valueOf(song.getmNameSong())});
        }

        @Override
        public List<Song> getAllSong(String[] selectionArgs) {
            List<Song> songsList = new ArrayList<Song>();
            Cursor cursor = mDatabase.rawQuery(QUERY_ALL, selectionArgs);
            Song song = null;
            if (cursor.moveToFirst()) {
                do {
                    song = new Song( cursor.getString(cursor.getColumnIndex(COLUMN_SONG_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME_SONG)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME_AUTHOR)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SONG_TIME_SONG)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SONG_PATH_SONG)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SONG_IMAGE)));
                    songsList.add(song);
                } while (cursor.moveToNext());
            }
            cursor.close();

            return songsList;
        }

        @Override
        public boolean saveListSong(List<Song> listSongs) {


            return true;
        }

        @Override
        public void closeDataBase() {
            mDatabase.close();
        }

        public int getCount() {
            Cursor cursor = mDatabase.rawQuery(QUERY_ALL, null);
            int count = cursor.getCount();
            cursor.close();

            return count;
        }

        private ContentValues getContentValues(Song song){
            ContentValues values = new ContentValues();

            values.put(COLUMN_SONG_ID, song.getmIDSong());
            values.put(COLUMN_SONG_NAME_SONG, song.getmNameSong());
            values.put(COLUMN_SONG_NAME_AUTHOR, song.getmNameAuthor());
            values.put(COLUMN_SONG_TIME_SONG, song.getmTimeSong());
            values.put(COLUMN_SONG_PATH_SONG, song.getmPathSong());
            values.put(COLUMN_SONG_IMAGE, song.getmImageSong());

            return values;
        }

    }

}
