package bane.innovation.a19kworddictionary.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private final static String DB_PATH = "/data/data/bane.innovation.a19kworddictionary/databases/";

	private static String DB_NAME = "19000-word.db";

	private SQLiteDatabase myDataBase;

	// cmd=select count(canonical) as cn , canonical from mapping group by canonical having cn>1 order by cn desc

	private Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getWritableDatabase();

			try {
				copyDataBase();
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
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

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
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

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

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}


	
	public List<Integer> searchForIds(String canonicalCompositionKey)
	{
		String []keys=canonicalCompositionKey.split("\\s+");
		String SqlQuerry="SELECT DISTINCT a0.[wordid] FROM [mapping] AS a0 ";
		for(int i=1;i<keys.length;i++)
			SqlQuerry+="JOIN   [mapping] AS a"+i+"  ON a0.[wordid]=a"+i+".[wordid] ";
		SqlQuerry+="WHERE ";
		for(int i=0;i<keys.length;i++)
		{
			SqlQuerry+="a"+i+".[canonical]=\""+keys[i]+"\"";
			if(i<keys.length-1)
				SqlQuerry+=" AND ";
		}

		Cursor c = null;
		List<Integer> result=null;
		try {
			c = myDataBase.rawQuery(SqlQuerry, null);
			result = new ArrayList<Integer>(c.getCount());
			for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {
				int wordId = c.getInt(c.getColumnIndex("wordid"));
				result.add(wordId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	
	}

	public List<Word> search(String canonicalCompositionKey) {
		Cursor c = null;
		List<Word> result=null;
		try {
			List<Integer>wordIds=searchForIds(canonicalCompositionKey);
			String set="";
			if(wordIds==null || wordIds.size()<1 )
				return null;
			else
			{
				set+="(";
				for(int i=0;i<wordIds.size()-1; i++)
					set+=wordIds.get(i)+",";
				set+=wordIds.get(wordIds.size()-1)+")";
					
			}
			String cmd = "SELECT * FROM [word] WHERE id IN "+set;
			c = myDataBase.rawQuery(cmd, null);
			result = new ArrayList<Word>(c.getCount());
			for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex("id"));
				String keyWord = c.getString(c.getColumnIndex("key"));
				String value = c.getString(c.getColumnIndex("value"));

				Word word = new Word(id, keyWord, value);
				result.add(word);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	}
	public Word getWord(int wordId) {

		Cursor c = null;
		Word result=null;
		try {
			String cmd = "SELECT id,key,value FROM [word] WHERE [word].[id] = "	+ wordId;
			c = myDataBase.rawQuery(cmd, null);
			if (c.moveToFirst()) {
				int id = c.getInt(c.getColumnIndex("id"));
				String keyWord = c.getString(c.getColumnIndex("key"));
				String value = c.getString(c.getColumnIndex("value"));
				result= new Word(id, keyWord, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	}

	public List<String> inquiryCanonicals(String key) {
		Cursor c = null;
		ArrayList<String> result=null;
		try {
			String cmd = "SELECT DISTINCT canonical FROM [mapping] WHERE [mapping].[canonical] LIKE \""+ key + "%\"";
			c = myDataBase.rawQuery(cmd, null);
			result = new ArrayList<String>(c.getCount());
			for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext())
				result.add(c.getString(c.getColumnIndex("canonical")));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	}
	
	public List<String> lookupHistory(String key) {
		Cursor c = null;
		ArrayList<String> result=null;
		try {
			String cmd = "SELECT DISTINCT query FROM [history] WHERE [history].[query] LIKE \""+ key + "%\"";
			c = myDataBase.rawQuery(cmd, null);
			result = new ArrayList<String>(c.getCount());
			for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext())
				result.add(c.getString(c.getColumnIndex("query")));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	}
	
	public boolean insertToHistory(String query) {
		try {
			ContentValues insertValues = new ContentValues();
			insertValues.put("query", query);
			myDataBase.insertWithOnConflict("history", null, insertValues, SQLiteDatabase.CONFLICT_IGNORE);  
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean clearHistory() {
		
		try {
			myDataBase.delete("history", null, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void setContext(Context contex) {
		this.myContext = contex;

	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

}