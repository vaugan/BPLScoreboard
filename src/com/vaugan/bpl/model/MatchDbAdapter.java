package com.vaugan.bpl.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple match database access helper class.
 */
public class MatchDbAdapter {


    public static final String KEY_P1 = "player1";
    public static final String KEY_P2 = "player2";
    public static final String KEY_SET1_RESULT = "set1_result";
    public static final String KEY_SET2_RESULT = "set2_result";
    public static final String KEY_SET3_RESULT = "set3_result";

    public static final String KEY_ROWID = "_id";

    private static final String TAG = "MatchDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table matches (_id integer primary key autoincrement, "
        + "player1 text not null, player2 text not null, set1_result text not null, set2_result text not null, set3_result text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "matches";
    private static final int DATABASE_VERSION = 6;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public MatchDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the  database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public MatchDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createMatch(String p1, String p2, String set1Result, String set2Result, String set3Result) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_P1, p1);
        initialValues.put(KEY_P2, p2);
        initialValues.put(KEY_SET1_RESULT, set1Result);
        initialValues.put(KEY_SET2_RESULT, set2Result);
        initialValues.put(KEY_SET3_RESULT, set3Result);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteMatch(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all  in the database
     * 
     * @return Cursor over all 
     */
    public Cursor fetchAllMatches() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_P1, KEY_P2, KEY_SET1_RESULT, KEY_SET2_RESULT, KEY_SET3_RESULT}, null, null, null, null, null);
    } 

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchMatch(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
            		KEY_P1, KEY_P2, KEY_SET1_RESULT, KEY_SET2_RESULT, KEY_SET3_RESULT}, KEY_ROWID + "=" + rowId, null,
                    null, null,null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateMatch(long rowId, String p1, String p2, String set1Result, String set2Result, String set3Result) {
        ContentValues args = new ContentValues();

        args.put(KEY_P1, p1);
        args.put(KEY_P2, p2);
        args.put(KEY_SET1_RESULT, set1Result);
        args.put(KEY_SET2_RESULT, set2Result);
        args.put(KEY_SET3_RESULT, set3Result);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateMatchResult(long rowId, String set1Result, String set2Result, String set3Result) {
        ContentValues args = new ContentValues();
        args.put(KEY_SET1_RESULT, set1Result);
        args.put(KEY_SET2_RESULT, set2Result);
        args.put(KEY_SET3_RESULT, set3Result);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}