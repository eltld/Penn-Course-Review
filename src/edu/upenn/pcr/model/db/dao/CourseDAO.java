package edu.upenn.pcr.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.Course;

public class CourseDAO extends BaseDAO {
	
	public CourseDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Course.TABLE_NAME);
		Cursor cursor = query.query(db, new String []{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public void add(Course course) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Course.ID, course.getId());
		value.put(Course.NAME, course.getName());
		value.put(Course.VIEWED, course.getViewed());
		db.insertWithOnConflict(Course.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void update(Course course) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Course.ID, course.getId());
		value.put(Course.NAME, course.getName());
		value.put(Course.VIEWED, course.getViewed());
		String [] args = {course.getId()};
		db.update(Course.TABLE_NAME, value, Course.ID + "=?", args);
	}
	
	public Course findById(String courseId) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Course.TABLE_NAME);
		query.appendWhere(Course.ID + " = " + courseId);
		String [] projection = {Course.ID, Course.NAME, Course.VIEWED};
		Cursor cursor = query.query(db, projection, null, null, null, null, null);
		
		int idIndex = cursor.getColumnIndex(Course.ID);
		int nameIndex = cursor.getColumnIndex(Course.NAME);
		int viewedIndex = cursor.getColumnIndex(Course.VIEWED);
		
		Course course = null;
		if (cursor.moveToFirst()) {
			course = new Course();
			course.setId(cursor.getString(idIndex));
			course.setName(cursor.getString(nameIndex));
			course.setViewed(cursor.getInt(viewedIndex));
		}
		return course;
		
	}
}
