package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.Department;

public class DepartmentDAO extends BaseDAO {
	
	public DepartmentDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(Department.TABLE_NAME);
		Cursor cursor = query.query(db, new String []{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public List<Department> findAll(int begin, int count) {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Department.TABLE_NAME, null, null, null, null, null, Department.ID + " ASC", begin + "," + count);
		List<Department> result = new ArrayList<Department>();
		int idIndex = cursor.getColumnIndex(Department.ID);
		int nameIndex = cursor.getColumnIndex(Department.NAME);
		int viewedIndex = cursor.getColumnIndex(Department.VIEWED);
		while (cursor.moveToNext()) {
			Department d = new Department();
			d.setId(cursor.getString(idIndex));
			d.setName(cursor.getString(nameIndex));
			d.setViewed(cursor.getInt(viewedIndex));
			result.add(d);
		}
		cursor.close();
		return result;
	}
	
	public List<Department> findAll() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Department.TABLE_NAME, null, null, null, null, null, Department.ID + " ASC");
		List<Department> result = new ArrayList<Department>();
		int idIndex = cursor.getColumnIndex(Department.ID);
		int nameIndex = cursor.getColumnIndex(Department.NAME);
		int viewedIndex = cursor.getColumnIndex(Department.VIEWED);
		while (cursor.moveToNext()) {
			Department d = new Department();
			d.setId(cursor.getString(idIndex));
			d.setName(cursor.getString(nameIndex));
			d.setViewed(cursor.getInt(viewedIndex));
			result.add(d);
		}
		cursor.close();
		return result;
	}
	
	public void add(Department department) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Department.ID, department.getId());
		value.put(Department.NAME, department.getName());
		value.put(Department.VIEWED, department.getViewed());
		db.insert(Department.TABLE_NAME, null, value);
	}
	
	public void update(Department department) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Department.ID, department.getId());
		value.put(Department.NAME, department.getName());
		value.put(Department.VIEWED, department.getViewed());
		String [] args = {department.getId()};
		db.update(Department.TABLE_NAME, value, Department.ID + "=?", args);
	}
}
