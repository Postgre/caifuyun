package com.zepan.android.sdk.cache;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zepan.android.sdk.entity.User;
public class UserDBManager {

	private static final String TAG = "UserDBManager";
	
	private SQLiteDatabase database;

	private static final String SQL_DROP_TABLE = "drop table if exists "
			+ DBOpenHelper.TABLE_NAME_USER;

	private static final String SQL_INSERT = "insert into "
			+ DBOpenHelper.TABLE_NAME_USER + " values (?,?,?,?)";

	private static final String SQL_QUERY = "select * from "
			+ DBOpenHelper.TABLE_NAME_USER;

	private DBOpenHelper mDatabaseHelper =  null;
	public UserDBManager(Context context) {
		mDatabaseHelper = DBOpenHelper.getInstance(context);
		database = mDatabaseHelper.getWritableDatabase();
	}

	public void close() {
//		if(database != null){
//			database.close();
//		}
//		if(mDatabaseHelper != null){
//			mDatabaseHelper.close();
//		}
	}

	public void dropTable() {
		database.execSQL(SQL_DROP_TABLE);
	}

	public boolean insert(User u){
		if(u == null){
			return false;
		}
		if(getUser() != null){
			deleteUser();
		}
		String values[] = new String[]{u.getEmail(), u.getPassword(), u.getMobile(),u.getNickName()};
		database.execSQL(SQL_INSERT, values);
		return true;
	}
	
	public boolean insert(Object[] values) {
		if(getUser() != null){
			deleteUser();
		}
		database.execSQL(SQL_INSERT, values);
		StringBuffer sb = new StringBuffer();
		for(Object obj : values){
			sb.append(obj.toString() + ",");
		}
		Log.i(TAG, "用户数据插入成功！插入数据为：" + sb.toString());
		return true;
	}

	public int updateUser(User u){
		if(u == null){
			return 0;
		}
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMNS_USER[1], u.getPassword());
		values.put(DBOpenHelper.COLUMNS_USER[2], u.getMobile());
		values.put(DBOpenHelper.COLUMNS_USER[3], u.getNickName());
		return database.update(DBOpenHelper.TABLE_NAME_USER, values, DBOpenHelper.COLUMNS_USER[0] + "=?", new String[]{u.getEmail()});
	}
	
	public User getUser() {
		User u = null;
		Cursor cursor = database.rawQuery(SQL_QUERY, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int emailIndex = cursor
						.getColumnIndex(DBOpenHelper.COLUMNS_USER[0]);
				int passwordIndex = cursor
						.getColumnIndex(DBOpenHelper.COLUMNS_USER[1]);
				int mobileIndex = cursor
						.getColumnIndex(DBOpenHelper.COLUMNS_USER[2]);
				int nicknameIndex = cursor
						.getColumnIndex(DBOpenHelper.COLUMNS_USER[3]);
				String email = cursor.getString(emailIndex);
				String password = cursor.getString(passwordIndex);
				String mobile = cursor.getString(mobileIndex);
				String nickname = cursor.getString(nicknameIndex);
				u = new User();
				u.setEmail(email);
				u.setPassword(password);
				u.setMobile(mobile);
				u.setNickName(nickname);
			}
		}
		cursor.close();
		return u;
	}

	public void deleteUser() {
		database.delete(DBOpenHelper.TABLE_NAME_USER, null, null);
	}

	public void dropDB() {
		database.execSQL("drop database hezong.db;");
	}
}
