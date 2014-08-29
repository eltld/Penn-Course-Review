package edu.upenn.pcr.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import edu.upenn.pcr.model.db.entity.Course;
import edu.upenn.pcr.model.db.entity.CourseDetail;
import edu.upenn.pcr.model.db.entity.Semester;

public class CourseDetailDAO extends BaseDAO {

	public CourseDetailDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public int count(){
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseDetail.TABLE_NAME);
		Cursor cursor = query.query(db, new String [] {"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}

	public void add(CourseDetail cd) {
		Log.d("COURSE_DETAIL_DAO_ADD", cd.getId());
		
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(CourseDetail.ID, cd.getId());
		value.put(CourseDetail.COURSE_ID, cd.getCourse().getId());
		value.put(CourseDetail.SEMESTER_ID, cd.getSemester().getId());
		value.put(CourseDetail.DESCRIPTION, cd.getDescription());
		db.insertWithOnConflict(CourseDetail.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	
	public CourseDetail findById(String id) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseDetail.TABLE_NAME + ", " + Course.TABLE_NAME + ", " + Semester.TABLE_NAME);
		query.appendWhere(CourseDetail.COURSE_ID + " = " + Course.ID);
		query.appendWhere(CourseDetail.SEMESTER_ID + " = " + Semester.ID);
		String [] projection = {CourseDetail.ID, CourseDetail.DESCRIPTION, Course.ID, Course.NAME, Semester.ID};
		String selection = CourseDetail.ID + " = ?";
		String [] selectionArgs = {id};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		if (!cursor.moveToFirst()) {
			return null;
		}
		int cdIdIndex = cursor.getColumnIndex(CourseDetail.ID);
		int cdDescriptionIndex = cursor.getColumnIndex(CourseDetail.DESCRIPTION);
		int courseIdIndex = cursor.getColumnIndex(Course.ID);
		int courseNameIndex = cursor.getColumnIndex(Course.NAME);
		int smesterIdIndex = cursor.getColumnIndex(Semester.ID);
		CourseDetail cd = new CourseDetail();
		cd.setId(cursor.getString(cdIdIndex));
		cd.setDescription(cursor.getString(cdDescriptionIndex));
		Course c = new Course();
		c.setId(cursor.getString(courseIdIndex));
		c.setName(cursor.getString(courseNameIndex));
		cd.setCourse(c);
		Semester s = new Semester();
		s.setId(cursor.getString(smesterIdIndex));
		cd.setSemester(s);
		return cd;
	}
}
