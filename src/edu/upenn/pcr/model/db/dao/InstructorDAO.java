package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.Instructor;

public class InstructorDAO extends BaseDAO {
	
	public InstructorDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Instructor.TABLE_NAME);
		Cursor cursor = query.query(db, new String [] {"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public List<Instructor> findAll(int begin, int count) {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Instructor.TABLE_NAME, null, null, null, null, null, Instructor.LAST_NAME + ", " + Instructor.FIRST_NAME, begin + "," + count);
		List<Instructor> result = new ArrayList<Instructor>();
		int idIndex = cursor.getColumnIndex(Instructor.ID);
		int lastNameIndex = cursor.getColumnIndex(Instructor.LAST_NAME);
		int firstNameIndex = cursor.getColumnIndex(Instructor.FIRST_NAME);
		int viewedIndex = cursor.getColumnIndex(Instructor.VIEWED);
		while (cursor.moveToNext()) {
			Instructor i = new Instructor();
			i.setId(cursor.getString(idIndex));
			i.setLastName(cursor.getString(lastNameIndex));
			i.setFirstName(cursor.getString(firstNameIndex));
			i.setViewed(cursor.getInt(viewedIndex));
			result.add(i);
		}
		cursor.close();
		return result;
	}
	
	public List<Instructor> findAll() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Instructor.TABLE_NAME, null, null, null, null, null, Instructor.LAST_NAME + ", " + Instructor.FIRST_NAME);
		List<Instructor> result = new ArrayList<Instructor>();
		int idIndex = cursor.getColumnIndex(Instructor.ID);
		int lastNameIndex = cursor.getColumnIndex(Instructor.LAST_NAME);
		int firstNameIndex = cursor.getColumnIndex(Instructor.FIRST_NAME);
		int viewedIndex = cursor.getColumnIndex(Instructor.VIEWED);
		while (cursor.moveToNext()) {
			Instructor i = new Instructor();
			i.setId(cursor.getString(idIndex));
			i.setLastName(cursor.getString(lastNameIndex));
			i.setFirstName(cursor.getString(firstNameIndex));
			i.setViewed(cursor.getInt(viewedIndex));
			result.add(i);
		}
		cursor.close();
		return result;
	}
	
	public void add(Instructor instructor) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Instructor.ID, instructor.getId());
		value.put(Instructor.LAST_NAME, instructor.getLastName());
		value.put(Instructor.FIRST_NAME, instructor.getFirstName());
		value.put(Instructor.VIEWED, instructor.getViewed());
		db.insert(Instructor.TABLE_NAME, null, value);
	}
	
	public void update(Instructor instructor) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Instructor.ID, instructor.getId());
		value.put(Instructor.LAST_NAME, instructor.getLastName());
		value.put(Instructor.FIRST_NAME, instructor.getFirstName());
		value.put(Instructor.VIEWED, instructor.getViewed());
		String [] args = {instructor.getId()};
		db.update(Instructor.TABLE_NAME, value, Instructor.ID + "=?", args);
	}
	
	
	public Instructor findById(String instructorId) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Instructor.TABLE_NAME);
		String selection = Instructor.ID + " = ?";
		String [] selectionArgs = {instructorId};
		String [] projection = {Instructor.ID, Instructor.FIRST_NAME, Instructor.LAST_NAME, Instructor.VIEWED};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		
		int idIndex = cursor.getColumnIndex(Instructor.ID);
		int firstNameIndex = cursor.getColumnIndex(Instructor.FIRST_NAME);
		int lastNameIndex = cursor.getColumnIndex(Instructor.LAST_NAME);
		int viewedIndex = cursor.getColumnIndex(Instructor.VIEWED);
		
		Instructor instructor = null;
		if (cursor.moveToFirst()) {
			instructor = new Instructor();
			instructor.setId(cursor.getString(idIndex));
			instructor.setFirstName(cursor.getString(firstNameIndex));
			instructor.setLastName(cursor.getString(lastNameIndex));
			instructor.setViewed(cursor.getInt(viewedIndex));
		}
		return instructor;
		
	}
}
