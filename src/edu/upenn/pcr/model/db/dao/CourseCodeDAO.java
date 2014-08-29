package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.Code;
import edu.upenn.pcr.model.db.entity.Course;
import edu.upenn.pcr.model.db.entity.CourseCode;
import edu.upenn.pcr.model.db.entity.Department;

public class CourseCodeDAO extends BaseDAO {
	
	public CourseCodeDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public void add(CourseCode cc) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(CourseCode.CODE, cc.getCode().getCode());
		value.put(CourseCode.COURSE_ID, cc.getCourse().getId());
		db.insert(CourseCode.TABLE_NAME, null, value);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(CourseCode.TABLE_NAME, new String [] {"count(*)"}, null, null, null, null, null);
		int result = 0;
		if (!cursor.moveToFirst()) {
			result = 0;
		} else {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}

	public Integer countByDepartment(String deptId) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseCode.TABLE_NAME + ", "  + Course.TABLE_NAME + ", " + Code.TABLE_NAME);
		query.appendWhere(CourseCode.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseCode.CODE + " = " + Code.CODE);
		String [] projection = {"count(*)"};
		String selection = Code.DEPT_ID + " = ?";
		String [] selectionArgs = {deptId};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		int result = 0;
		if (!cursor.moveToFirst()) {
			result = 0;
		} else {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
	
	public Integer countByCode(String code) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseCode.TABLE_NAME + ", "  + Course.TABLE_NAME + ", " + Code.TABLE_NAME);
		query.appendWhere(CourseCode.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseCode.CODE + " = " + Code.CODE);
		String [] projection = {"count(*)"};
		String selection = Code.CODE + " = ?";
		String [] selectionArgs = {code};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		int result = 0;
		if (!cursor.moveToFirst()) {
			result = 0;
		} else {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
	
	public List<CourseCode> findByDepartment(String deptId, int begin, int count) {
		SQLiteDatabase db = this.getDatabase();

		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseCode.TABLE_NAME +  ", " + Course.TABLE_NAME + ", " + Code.TABLE_NAME);
		query.appendWhere(CourseCode.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseCode.CODE + " = " + Code.CODE);
		String [] projection = {Code.CODE, Course.ID, Course.NAME, Course.VIEWED, Code.DEPT_ID};
		String selection = Code.DEPT_ID + " = ?";
		String [] selectionArgs = {deptId};
		String orderBy = Code.CODE;
		String limit = begin + ", " + count;
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, orderBy, limit);
		List<CourseCode> result = new ArrayList<CourseCode>();
		int codeIndex = cursor.getColumnIndex(Code.CODE);
		int coureIdIndex = cursor.getColumnIndex(Course.ID);
		int courseNameIndex = cursor.getColumnIndex(Course.NAME);
		int courseViewedIndex = cursor.getColumnIndex(Course.VIEWED);
		int deptIdIndex = cursor.getColumnIndex(Code.DEPT_ID);
		
		while (cursor.moveToNext()) {
			CourseCode cc = new CourseCode();
			Department dept = new Department();
			dept.setId(cursor.getColumnName(deptIdIndex));
			Code code = new Code();
			code.setCode(cursor.getString(codeIndex));
			code.setDepartment(dept);
			Course course = new Course();
			course.setId(cursor.getString(coureIdIndex));
			course.setName(cursor.getString(courseNameIndex));
			course.setViewed(cursor.getInt(courseViewedIndex));
			cc.setCourse(course);
			cc.setCode(code);
			result.add(cc);
		}
		cursor.close();
		return result;
	}
	
	public List<CourseCode> findByCode(String code, int begin, int count) {
		SQLiteDatabase db = this.getDatabase();

		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseCode.TABLE_NAME +  ", " + Course.TABLE_NAME + ", " + Code.TABLE_NAME);
		query.appendWhere(CourseCode.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseCode.CODE + " = " + Code.CODE);
		String [] projection = {Code.CODE, Course.ID, Course.NAME, Course.VIEWED, Code.DEPT_ID};
		String selection = Code.DEPT_ID + " = ?";
		String [] selectionArgs = {code};
		String orderBy = Code.CODE;
		String limit = begin + ", " + count;
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, orderBy, limit);
		List<CourseCode> result = new ArrayList<CourseCode>();
		int codeIndex = cursor.getColumnIndex(Code.CODE);
		int coureIdIndex = cursor.getColumnIndex(Course.ID);
		int courseNameIndex = cursor.getColumnIndex(Course.NAME);
		int courseViewedIndex = cursor.getColumnIndex(Course.VIEWED);
		int deptIdIndex = cursor.getColumnIndex(Code.DEPT_ID);
		
		while (cursor.moveToNext()) {
			CourseCode cc = new CourseCode();
			Department dept = new Department();
			dept.setId(cursor.getColumnName(deptIdIndex));
			Code c = new Code();
			c.setCode(cursor.getString(codeIndex));
			c.setDepartment(dept);
			Course course = new Course();
			course.setId(cursor.getString(coureIdIndex));
			course.setName(cursor.getString(courseNameIndex));
			course.setViewed(cursor.getInt(courseViewedIndex));
			cc.setCourse(course);
			cc.setCode(c);
			result.add(cc);
		}
		cursor.close();
		return result;
	}
}
