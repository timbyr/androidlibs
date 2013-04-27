package com.timbyr.lib.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractSQLCipherDatabaseHelper extends SQLiteOpenHelper {
	private final File mPath;
	private final String mName;
	private int mNewVersion;
	private final CursorFactory mFactory;
	private SQLiteDatabase mDatabase;
	private final Context mContext;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	/*
	 * public DatabaseHelper(Context context, String name, CursorFactory
	 * factory, int version) { super(context, name, factory, version); // TODO
	 * Auto-generated constructor stub }
	 */

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public AbstractSQLCipherDatabaseHelper(Context context, String DB_NAME,
			CursorFactory factory, int DATABASE_VERSION) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.mName = DB_NAME;
		this.mNewVersion = DATABASE_VERSION;
		this.mContext = context;
		this.mFactory = factory;
		mPath = context.getFilesDir();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(getClass().getCanonicalName(), "onCreate");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(getClass().getCanonicalName(), "onUpgrade - oldVersion: "
				+ oldVersion + "\tnewVersion: " + newVersion);
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */

	private void createDataBase(String passphrase) throws IOException {
		boolean dbExist = checkDataBase(passphrase);
		Log.v(getClass().getCanonicalName(), "createDataBase - dbExist: "
				+ dbExist);

		if (dbExist) {
			// do nothing - database already exist
		} else {

			try {
				copyDataBase(passphrase);
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(String passphrase) {
		Log.v(getClass().getCanonicalName(), "checkDataBase");
		SQLiteDatabase checkDB = null;

		try {
			String myPath = new File(mPath, mName).getAbsolutePath();
			checkDB = SQLiteDatabase.openDatabase(myPath, passphrase, null, SQLiteDatabase.OPEN_READONLY);
			Log.v(getClass().getCanonicalName(), "checkDataBase - database does exist");
			Log.v(getClass().getCanonicalName(), "checkDataBase - checkDB.getVersion(): "
					+ checkDB.getVersion());
			Log.v(getClass().getCanonicalName(), "checkDatabase - current version: " + checkDB.getVersion());
			boolean needsUpgrade = checkDB.needUpgrade(mNewVersion);
			Log.v(getClass().getCanonicalName(), "checkDataBase - needsUpgrade: " + needsUpgrade);
			if (needsUpgrade) {
				checkDB.close();
				return false;
			}
		} catch (SQLiteException e) {
			Log.v(getClass().getCanonicalName(),
					"checkDataBase - database doesn't exist yet");
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase(String passphrase) throws IOException {
		Log.v(getClass().getCanonicalName(), "copyDataBase - start");
		// Open your local db as the input stream
		// InputStream myInput = new
		// GZIPInputStream(mContext.getAssets().open(DB_NAME + ".jpg"));
		InputStream myInput = mContext.getAssets().open(mName);
		// Path to the just created empty db
		File outFileName = new File(mPath, mName);

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

		SQLiteDatabase.openOrCreateDatabase(outFileName, passphrase, null).close();

		Log.v(getClass().getCanonicalName(), "copyDataBase - finished");
	}

	private SQLiteDatabase getDataBase(String passphrase, boolean writable) throws SQLException {
		if (mDatabase != null) {
			if (!mDatabase.isOpen()) {
				// Darn!  The user closed the database by calling mDatabase.close().
				mDatabase = null;
			} else if (!writable || !mDatabase.isReadOnly()) {
				// The database is already open for business.
				return mDatabase;
			}
		}
		if (mDatabase == null)
		{
			try
			{
				this.createDataBase(passphrase);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			File myPath = new File(mPath, mName);
			int flags = writable ? SQLiteDatabase.OPEN_READWRITE : SQLiteDatabase.OPEN_READONLY;
			mDatabase = SQLiteDatabase.openDatabase(myPath.getAbsolutePath(), passphrase, mFactory, flags);
		}
		return mDatabase;
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase(String passphrase) {
		return getDataBase(passphrase, false);
	}
	
	@Override
	public synchronized void close() {
		super.close();
		mDatabase = null;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase(String passphrase) {
		return getDataBase(passphrase, true);

	}
}
