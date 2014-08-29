package edu.upenn.pcr.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.upenn.pcr.model.db.entity.Semester;

public class SemesterDAO extends BaseDAO {
	
	public SemesterDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Semester.TABLE_NAME, new String [] {"count(*)"}, null, null, null, null, null);
		int result = 0;
		if (!cursor.moveToFirst()) {
			result = 0;
		} else {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
	
	public void add(Semester semester) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Semester.ID, semester.getId());
		value.put(Semester.NAME, semester.getName());
		value.put(Semester.YEAR, semester.getYear());
		value.put(Semester.SEASON, semester.getSeason());
		db.insert(Semester.TABLE_NAME, null, value);
	}
}
