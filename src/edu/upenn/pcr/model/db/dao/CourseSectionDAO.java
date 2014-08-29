package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import edu.upenn.pcr.model.db.entity.Course;
import edu.upenn.pcr.model.db.entity.CourseDetail;
import edu.upenn.pcr.model.db.entity.CourseSection;
import edu.upenn.pcr.model.db.entity.Instructor;
import edu.upenn.pcr.model.db.entity.Semester;

public class CourseSectionDAO extends BaseDAO {

	public CourseSectionDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public int count() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(CourseSection.TABLE_NAME, new String [] {"count(*)"}, null, null, null, null, null);
		int result = 0;
		if (cursor.moveToFirst()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
	
	public void add(CourseSection cs) {
		Log.d("CourseSectionDAO_add", cs.getCourseDetail().getId());
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(CourseSection.SECTION, cs.getSection());
		value.put(CourseSection.COURSE_DETAIL_ID, cs.getCourseDetail().getId());
		value.put(CourseSection.PRIMARY_ALIAS, cs.getPrimaryAlias());
		value.put(CourseSection.INSTRUCTOR_ID, cs.getInstructor().getId());
		StringBuffer buffer = new StringBuffer();
		List<Double> ratings = cs.getRatings();
		for (int i = 0; i < ratings.size(); i++) {
			buffer.append(ratings.get(i).toString());
			buffer.append(",");
		}
		value.put(CourseSection.RATINGS, buffer.toString());
		db.insertWithOnConflict(CourseSection.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
	}

	public List<CourseSection> findByInstructor(String instrId) {
		Log.d("CourseSectionDAO_findByInstructor", instrId);
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseSection.TABLE_NAME + ", " + 
						CourseDetail.TABLE_NAME + ", " + 
						Course.TABLE_NAME + ", " + 
						Instructor.TABLE_NAME + ", " +
						Semester.TABLE_NAME);
		query.appendWhere(CourseSection.COURSE_DETAIL_ID + " = " + CourseDetail.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseDetail.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseDetail.SEMESTER_ID + " = " + Semester.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseSection.INSTRUCTOR_ID + " = " + Instructor.ID);
		String [] projection = {CourseSection.PRIMARY_ALIAS, CourseSection.RATINGS,
								CourseDetail.DESCRIPTION, Course.ID, Course.NAME, 
								Semester.ID, Semester.NAME, Instructor.LAST_NAME, Instructor.FIRST_NAME,
		};
		String selection = CourseSection.INSTRUCTOR_ID + " = ?";
		String [] selectionArgs = {instrId};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		List<CourseSection> result = new ArrayList<CourseSection>();
		
		int primaryAliasIndex = cursor.getColumnIndex(CourseSection.PRIMARY_ALIAS);
		int descriptionIndex = cursor.getColumnIndex(CourseDetail.DESCRIPTION);
		int semesterIdIndex = cursor.getColumnIndex(Semester.ID);
		int semesterNameIndex = cursor.getColumnIndex(Semester.NAME);
		int courseIdIndex = cursor.getColumnIndex(Course.ID);
		int courseNameIndex = cursor.getColumnIndex(Course.NAME);
		int instrLastNameIndex = cursor.getColumnIndex(Instructor.LAST_NAME);
		int instrFirstNameIndex = cursor.getColumnIndex(Instructor.FIRST_NAME);
		int ratingsIndex = cursor.getColumnIndex(CourseSection.RATINGS);
		
		while (cursor.moveToNext()) {
			CourseSection cs = new CourseSection();
			cs.setPrimaryAlias(cursor.getString(primaryAliasIndex));
			
			CourseDetail cd = new CourseDetail();
			cd.setDescription(cursor.getString(descriptionIndex));
			Semester s = new Semester();
			s.setId(cursor.getString(semesterIdIndex));
			s.setName(cursor.getString(semesterNameIndex));
			cd.setSemester(s);
			Course c = new Course();
			c.setId(cursor.getString(courseIdIndex));
			c.setName(cursor.getString(courseNameIndex));
			cd.setCourse(c);
			cs.setCourseDetail(cd);
			
			Instructor instr = new Instructor();
			instr.setLastName(cursor.getString(instrLastNameIndex));
			instr.setFirstName(cursor.getString(instrFirstNameIndex));
			cs.setInstructor(instr);
			
			String ratings = cursor.getString(ratingsIndex);
			String [] tokens = ratings.split(",");
			for (int i = 0; i < tokens.length; i++) {
				cs.setRating(i, Double.parseDouble(tokens[i]));
			}
			result.add(cs);
		}
		return result;
	}
	
	public List<CourseSection> findByCourseId(String id) {
		Log.d("CourseSectionDAO_findByCourseId", id);
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(CourseSection.TABLE_NAME + ", " + 
						CourseDetail.TABLE_NAME + ", " + 
						Course.TABLE_NAME + ", " + 
						Instructor.TABLE_NAME + ", " +
						Semester.TABLE_NAME);
		query.appendWhere(CourseSection.COURSE_DETAIL_ID + " = " + CourseDetail.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseDetail.COURSE_ID + " = " + Course.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseDetail.SEMESTER_ID + " = " + Semester.ID);
		query.appendWhere(" AND ");
		query.appendWhere(CourseSection.INSTRUCTOR_ID + " = " + Instructor.ID);
		String [] projection = {CourseSection.PRIMARY_ALIAS, CourseSection.RATINGS,
								CourseDetail.DESCRIPTION, Course.ID, Course.NAME, 
								Semester.ID, Semester.NAME, Instructor.ID, Instructor.LAST_NAME, Instructor.FIRST_NAME,
		};
		String selection = Course.ID + " = ?";
		String [] selectionArgs = {id};
		Cursor cursor = query.query(db, projection, selection, selectionArgs, null, null, null);
		List<CourseSection> result = new ArrayList<CourseSection>();
		
		int primaryAliasIndex = cursor.getColumnIndex(CourseSection.PRIMARY_ALIAS);
		int descriptionIndex = cursor.getColumnIndex(CourseDetail.DESCRIPTION);
		int semesterIdIndex = cursor.getColumnIndex(Semester.ID);
		int semesterNameIndex = cursor.getColumnIndex(Semester.NAME);
		int courseIdIndex = cursor.getColumnIndex(Course.ID);
		int courseNameIndex = cursor.getColumnIndex(Course.NAME);
		int instrIdIndex = cursor.getColumnIndex(Instructor.ID);
		int instrLastNameIndex = cursor.getColumnIndex(Instructor.LAST_NAME);
		int instrFirstNameIndex = cursor.getColumnIndex(Instructor.FIRST_NAME);
		int ratingsIndex = cursor.getColumnIndex(CourseSection.RATINGS);
		
		while (cursor.moveToNext()) {
			CourseSection cs = new CourseSection();
			cs.setPrimaryAlias(cursor.getString(primaryAliasIndex));
			
			CourseDetail cd = new CourseDetail();
			cd.setDescription(cursor.getString(descriptionIndex));  
			Semester s = new Semester();
			s.setId(cursor.getString(semesterIdIndex));
			s.setName(cursor.getString(semesterNameIndex));
			cd.setSemester(s);
			Course c = new Course();
			c.setId(cursor.getString(courseIdIndex));
			c.setName(cursor.getString(courseNameIndex));
			cd.setCourse(c);
			cs.setCourseDetail(cd);
			
			Instructor instr = new Instructor();
			instr.setId(cursor.getString(instrIdIndex));
			instr.setLastName(cursor.getString(instrLastNameIndex));
			instr.setFirstName(cursor.getString(instrFirstNameIndex));
			cs.setInstructor(instr);
			
			String ratings = cursor.getString(ratingsIndex);
			String [] tokens = ratings.split(",");
			for (int i = 0; i < tokens.length; i++) {
				cs.setRating(i, Double.parseDouble(tokens[i]));
			}
			result.add(cs);
		}
		return result;
	}
}
