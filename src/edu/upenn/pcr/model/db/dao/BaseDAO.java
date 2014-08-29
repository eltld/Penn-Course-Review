package edu.upenn.pcr.model.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class BaseDAO {

	private SQLiteDatabase db;
	
	public BaseDAO(SQLiteDatabase db) {
		this.db = db;
	}
	
	protected SQLiteDatabase getDatabase() {
		return db;
	}
	
}
