package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.Recent;

public class RecentDAO extends BaseDAO {

	public RecentDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Recent.TABLE_NAME);
		Cursor cursor = query.query(db, new String []{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public void add(Recent recent) {
		recent.setTimeStamp(System.currentTimeMillis());
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Recent.FULL_TEXT, recent.getFullText());
		value.put(Recent.TYPE, recent.getType().name());
		value.put(Recent.REFERENCE_ID, recent.getRefId());
		value.put(Recent.TIME_STAMP, recent.getTimeStamp());
		db.insertWithOnConflict(Recent.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void update(Recent recent) {
		add(recent);
	}
	
	public List<Recent> findAll(int begin, int count) {
		SQLiteDatabase db = this.getDatabase();
		String [] projection = {Recent.FULL_TEXT, Recent.TYPE, Recent.REFERENCE_ID};
		Cursor cursor = db.query(Recent.TABLE_NAME, projection, null, null, null, null, Recent.TIME_STAMP + " DESC", begin + ", " + count);
		List<Recent> result = new ArrayList<Recent>();
		int fullTextIndex = cursor.getColumnIndex(Recent.FULL_TEXT);
		int typeIndex = cursor.getColumnIndex(Recent.TYPE);
		int refIdIndex = cursor.getColumnIndex(Recent.REFERENCE_ID);
		while (cursor.moveToNext()) {
			Recent r = new Recent();
			r.setFullText(cursor.getString(fullTextIndex));
			r.setType(Recent.ItemType.valueOf(cursor.getString(typeIndex)));
			r.setRefId(cursor.getString(refIdIndex));
			result.add(r);
		}
		cursor.close();
		return result;
	}
}
