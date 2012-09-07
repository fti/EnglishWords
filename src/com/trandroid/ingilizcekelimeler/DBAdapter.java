package com.trandroid.ingilizcekelimeler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	public static final String KEY_ID = "id";
	public static final String KEY_ORDER = "orderid";
	public static final String KEY_WORD = "kword";
	public static final String KEY_MEAN = "mean";
	public static final String KEY_ISLEARNED = "islearned";
	public static final String KEY_TYPE = "type";
	
	private static final String TAG = "ING_DB_ADAPTER";
	
    public static final String DB_PATH = "/data/data/"+"com.trandroid.ingilizcekelimeler"+"/databases/";

	public static final String DB_NAME = "ingilizcekelimeler";
	private static final String TABLE_WORDS = "words";
	private static final int DB_VERSION = 1;

	private static final String DB_CREATE_WORD_TABLE = "create table "+TABLE_WORDS+" ("+KEY_ID+" INTEGER primary key autoincrement, "+KEY_WORD+" varchar(100), " +
			KEY_MEAN+" varchar(250), "+KEY_ORDER+" INTEGER, " + KEY_TYPE + " varchar(10), "+ KEY_ISLEARNED + " INTEGER default 0);";
			
	
	private final Context context;

	private DatabaseHelper DBHelper;

	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}
	
	public boolean isFirstTime(String type) {
		
		Cursor cs=db.rawQuery("select "+KEY_ID+" from "+TABLE_WORDS+" where "+KEY_TYPE+"='"+type+"'", null);
		if (cs.moveToFirst()) {
			return false;
		}
		return true;
	}

	public long insertWord(Word w) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ORDER, w.orderid);
		initialValues.put(KEY_WORD, w.word);
		initialValues.put(KEY_MEAN, w.mean);
		initialValues.put(KEY_TYPE, w.type);
		return db.insert(TABLE_WORDS, null, initialValues);
	}

	public boolean deleteChannel(int tvid) {
		return db.delete(TABLE_WORDS, KEY_ID + "=" + tvid, null) > 0;
	}

	public List<Word> getWords(String type, int lastShown, int showType) {
		String showTypePart = "";
		if (showType!=Session.SHOW_ALL) {
			showTypePart = " and "+KEY_ISLEARNED+"="+showType;
		}
		Cursor c = db.rawQuery("select * from "+ TABLE_WORDS+
				" where "+KEY_TYPE+"='"+type+
				"' and "+KEY_ID+">="+lastShown+""+
				showTypePart+
				" limit 20;", null);
		return cursorToList(c);
	}
	
	
	public List<Word> cursorToList(Cursor c){

		List<Word> wList = new ArrayList<Word>();
		if (c.moveToFirst()) {
			do {
				Word w = new Word();
				w.id = c.getInt(0);
				w.word = c.getString(1);
				w.mean = c.getString(2).replace("\\n", "\n");
				w.orderid = c.getInt(3);
				w.type = c.getString(4);
				w.islearned = c.getInt(5);
				wList.add(w);
						
			} while (c.moveToNext());
		}
		return wList;
	}
	
	public boolean updateWord(int id, int islearn){
		     
		try {
			String query = "update "+TABLE_WORDS+" set "+KEY_ISLEARNED+"="+islearn+" where "+KEY_ID+"="+id+";";
			 Cursor cu = db.rawQuery(query, null);
			 cu.moveToFirst();
			 cu.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public long getTotalQuestionNumber() {
		return	db.getMaximumSize();
	}
	
	public boolean isDataBaseExist() {
            File dbFile = new File(DB_PATH+DB_NAME);
            return dbFile.exists();
    }
    
	
	public DatabaseHelper getDBHelper() {
		return DBHelper;
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {
		
		   
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_WORD_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORDS);
			onCreate(db);
		}
		   


	}
 }
