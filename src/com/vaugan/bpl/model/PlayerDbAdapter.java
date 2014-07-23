package com.vaugan.bpl.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Player database access helper class. Defines the basic CRUD operations
 * for the google android notepad example, and gives the ability to list all players as well as
 * retrieve or modify a specific player.
 */
public class PlayerDbAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "PlayerDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
// The player database is created already for now. In future this might change.    
//    private static final String DATABASE_CREATE =
//        "create table players (_id integer primary key autoincrement, "
//        + "name text not null, picture text not null);";

    private String DB_PATH = "/data/data/" + "com.vaugan.bpl" + "/databases/" + "bpl.db";
    private static final String DATABASE_NAME = "bpl.db";
    private static final String DATABASE_TABLE = "players";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

//            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS players");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public PlayerDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the player database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public PlayerDbAdapter open() throws SQLException {
    	
    	InputStream is;
		try {
			is = mCtx.getApplicationContext().getAssets().open(DATABASE_NAME);
	    	 write(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new player using params provided. If the player is
     * successfully created return the new rowId for that player, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the player
     * @param picture the picture of the player
     * @return rowId or -1 if failed
     */
    public long createPlayer(String name, String picture) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PICTURE, picture);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the player with the given rowId
     * 
     * @param rowId id of player to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePlayer(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all players in the database
     * 
     * @return Cursor over all players
     */
    public Cursor fetchAllPlayers() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_PICTURE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the player that matches the given rowId
     * 
     * @param rowId id of player to retrieve
     * @return Cursor positioned to matching player, if found
     * @throws SQLException if player could not be found/retrieved
     */
    public Cursor fetchPlayer(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_NAME, KEY_PICTURE}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the player using the details provided. The player to be updated is
     * specified using the rowId, and it is altered to use the values passed in
     * 
     * @param rowId id of player to update
     * @param name the name of the player
     * @param picture the picture of the player
     * @return true if the player was successfully updated, false otherwise
     */
    public boolean updatePlayer(long rowId, String name, String picture) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PICTURE, picture);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public void write(InputStream is) {
        try {
            OutputStream out = new FileOutputStream(new File(DB_PATH));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            is.close();
            out.flush();
            out.close();
            System.err.println(out + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
