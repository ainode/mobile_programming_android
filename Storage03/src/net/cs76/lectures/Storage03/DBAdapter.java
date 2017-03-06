package net.cs76.lectures.Storage03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * DBAdapter
 * Dan Armendariz
 * Computer Science E-76
 * 
 * Provides an interface through which we can perform queries
 * against the SQLite database.
 */

public class DBAdapter {

	// define the layout of our table in fields
	// "_id" is used by Android for Content Providers and should
	// generally be an auto-incrementing key in every table.
	public static final String KEY_ROWID = "_id";
	public static final String KEY_USER = "user";
	public static final String KEY_PASS = "pass";

	// define some SQLite database fields
	// Take a look at your DB on the emulator with:
	// 	adb shell 
	//  sqlite3 /data/data/<pkg_name>/databases/<DB_NAME>
	private static final String DB_NAME = "db_example";
	private static final String DB_TABLE = "users";
	private static final int    DB_VER = 1;

	// a SQL statement to create a new table
	private static final String DB_CREATE = 
		"CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		 "user TEXT NOT NULL, pass TEXT NOT NULL);";


	// define an extension of the SQLiteOpenHelper to handle the
	// creation and upgrade of a table
	private static class DatabaseHelper extends SQLiteOpenHelper {

		// Class constructor
		DatabaseHelper(Context c) {
			// instantiate a SQLiteOpenHelper by passing it
			// the context, the database's name, a CursorFactory 
			// (null by default), and the database version.
			super(c, DB_NAME, null, DB_VER);
		}

		// called by the parent class when a DB doesn't exist
		public void onCreate(SQLiteDatabase db) {
			// Execute our DB_CREATE statement
			db.execSQL(DB_CREATE);
		}
		
		// called by the parent when a DB needs to be upgraded
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
			// remove the old version and create a new one.
			// If we were really upgrading we'd try to move data over
			db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
			onCreate(db);
		}
	}


	// useful fields in the class
    private final Context context;	
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    // DBAdapter class constructor
	public DBAdapter(Context c) {
		this.context = c;
	}
	
	/** Open the DB, or throw a SQLException if we cannot open
	  * or create a new DB.
	  */ 
	public DBAdapter open() throws SQLException {
		// instantiate a DatabaseHelper class (see above)
		helper = new DatabaseHelper(context);

		// the SQLiteOpenHelper class (a parent of DatabaseHelper)
		// has a "getWritableDatabase" method that returns an
		// object of type SQLiteDatabase that represents an open
		// connection to the database we've opened (or created).
		db = helper.getWritableDatabase();

		return this;
	}
	
	/** Close the DB
	  */
	public void close() {
		helper.close();
	}

	/** Insert a user and password into the db
	  * 
	  * @param user username (string)
	  * @param pass user's password (string)
	  * @return the row id, or -1 on failure
	 */
	public long insertUser(String user, String pass) {
		ContentValues vals = new ContentValues();
		vals.put(KEY_USER, user);
		vals.put(KEY_PASS, pass);
		return db.insert(DB_TABLE, null, vals);
	}

	/** Authenticate a user by querying the table to see
	  * if that user and password exist. We expect only one row
	  * to be returned if that combination exists, and if so, we
	  * have successfully authenticated.
	  * 
	  * @param user username (string)
	  * @param pass user's password (string)
	  * @return true if authenticated, false otherwise
	 */
	public boolean authenticateUser(String user, String pass) {
		// Perform a database query
		Cursor cursor = db.query(
				DB_TABLE, // table to perform the query
				new String[] { KEY_USER }, //resultset columns/fields
				KEY_USER+"=? AND "+KEY_PASS+"=?", //condition or selection
				new String[] { user, pass },  //selection arguments (fills in '?' above)
				null,  //groupBy
				null,  //having
				null   //orderBy
			);

		// if a Cursor object was returned by the query and
		// that query returns exactly 1 row, then we've authenticated
		if(cursor != null && cursor.getCount() == 1) {
			return true;
		}
		
		// The query returned no results or the incorrect
		// number of rows
		return false;
	}

}
