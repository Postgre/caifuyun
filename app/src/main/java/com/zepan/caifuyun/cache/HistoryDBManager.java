package com.zepan.caifuyun.cache;
import java.util.ArrayList;
import java.util.List;

import com.zepan.caifuyun.entity.History;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class HistoryDBManager {

	private SQLiteDatabase database;

	private static final String SQL_DROP_TABLE = "drop table if exists "
			+ DBOpenHelper.TABLE_NAME_HISTORY;

	private static final String SQL_INSERT = "insert into "
			+ DBOpenHelper.TABLE_NAME_HISTORY + " values (?,?,?)";

	private static final String SQL_QUERY_BY_TEXT = "select * from "
			+ DBOpenHelper.TABLE_NAME_HISTORY + " where " + DBOpenHelper.COLUMNS_HISTORY[1] + "=?";
	private static final String SQL_QUERY_ALL_TABLE = "select * from "
			+ DBOpenHelper.TABLE_NAME_HISTORY;
	/*private static final String SQL_DELETE_FROM_TABLE = "delete from"
			+ DBOpenHelper.TABLE_NAME_HOT_WORD+"where"+ DBOpenHelper.COLUMNS_HOT_WORD[0] +"=?";*/
	public HistoryDBManager(Context context) {
		DBOpenHelper databaseHelper = DBOpenHelper.getInstance(context);
		database = databaseHelper.getWritableDatabase();
	}

	public void close() {
//		if(database != null){
//			database.close();
//		}
	}

	public void dropTable() {
		database.execSQL(SQL_DROP_TABLE);
	}

	/**
	 * 判断是否含有相同文字
	 * @see 文字历史记录
	 * @param text 文字
	 * @return 包含返回true，否则返回false
	 * */
	public boolean contains(String text){
		Cursor cursor = database.rawQuery(SQL_QUERY_BY_TEXT, new String[]{text});
		return cursor.getCount() != 0;
	}
	
	/**
	 * 更新文字使用时间
	 * @param id
	 * @return 被更新的条数
	 * */
	public int updateHistoryTime(String id){
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMNS_HISTORY[2], System.currentTimeMillis() + "");
		return database.update(DBOpenHelper.TABLE_NAME_HISTORY, values, DBOpenHelper.COLUMNS_HISTORY[0] + "=?", new String[]{id});
	}
	
	public void insert(History h){
		if(h == null){
			return;
		}
		// 判断是否已包含相同文字，如果包含，仅更新使用时间；否则插入文字
		
		if(contains(h.getText())){
			updateHistoryTime(h.getId());
		}else{
			Object[] values = new Object[]{h.getId(), h.getText(),System.currentTimeMillis() + ""};
			database.execSQL(SQL_INSERT, values);
		}
	}
	
	public void insert(Object[] values) {
		database.execSQL(SQL_INSERT, values);
	}
	public void deleteFromTable(String id) {
		String[] values={id};
		database.delete(DBOpenHelper.TABLE_NAME_HISTORY, DBOpenHelper.COLUMNS_HISTORY[0]+"=?",values);
	}
	public List<History> getHistoryList(){
		List<History> hotWordList = new ArrayList<History>();
		Cursor cursor = database.rawQuery(SQL_QUERY_ALL_TABLE, new String[]{});
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			int index0 = cursor
					.getColumnIndex(DBOpenHelper.COLUMNS_HISTORY[0]);
			int index1 = cursor
					.getColumnIndex(DBOpenHelper.COLUMNS_HISTORY[1]);
			int index2 = cursor
					.getColumnIndex(DBOpenHelper.COLUMNS_HISTORY[2]);
			String id = cursor.getString(index0);
			String text = cursor.getString(index1);
			String time = cursor.getString(index2);
			History m = new History();
			m.setId(id);
			m.setText(text);
			m.setTime(time);
			hotWordList.add(m);
		}
		cursor.close();
		return hotWordList;
	}
	
	public void deleteHistory() {
		database.delete(DBOpenHelper.TABLE_NAME_HISTORY, null, null);
	}

	public void dropDB() {
		database.execSQL("drop database " + DBOpenHelper.DATABASE_NAME + ";");
	}
}
