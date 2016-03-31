package com.zepan.caifuyun.cache;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	public static final String TAG = "DBOpenHelper";

	private static DBOpenHelper mInstance = null;
    /** 数据库名*/
    public static final String DATABASE_NAME = "caifuyun.db";
	/**
	 * 热门词汇表字段
	 * */
	public static final String COLUMNS_HISTORY[] = {"_id", "text", "use_time"}; 
	/*** 用户信息缓存表字段* */
	public static final String COLUMNS_USER[] = {"_id", "user_name", "user_avatar"}; 
	/** 数据库版本号*/
    private static final int DATABASE_VERSION = 1;
    /** 热门词汇表*/
    public static final String TABLE_NAME_HISTORY = "t_hot_word";
    /**用户信息缓存表*/
    public static final String TABLE_USER="t_user";
    /** 热门词汇建表语句*/
    private static final String SQL_CREATE_TABLE_HISTORY = "create table if not exists " + TABLE_NAME_HISTORY 
    		+ "(" + COLUMNS_HISTORY[0] + " varchar(36) primary key,"
    		+ COLUMNS_HISTORY[1] + " varchar(32),"
    		+ COLUMNS_HISTORY[2] + " varchar(50)"
    		+ ")";
    /** 用户信息缓存建表语句*/
    private static final String SQL_CREATE_TABLE_USER = "create table if not exists " + TABLE_USER 
    		+ "(" + COLUMNS_USER[0] + " varchar(36) primary key,"
    		+ COLUMNS_USER[1] + " varchar(32),"
    		+ COLUMNS_USER[2] + " varchar(50)"
    		+ ")";
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DBOpenHelper getInstance(Context ctx) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (mInstance == null) {
	      mInstance = new DBOpenHelper(ctx.getApplicationContext());
	    }
	    return mInstance;
	  }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_HISTORY);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
