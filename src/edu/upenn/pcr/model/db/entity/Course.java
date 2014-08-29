package edu.upenn.pcr.model.db.entity;

import android.database.Cursor;

public class Course {

	public static final String TABLE_NAME = "Course";
	
	public static final String ID = "course_id";
	public static final String NAME = "course_name";
	public static final String VIEWED = "course_viewed";
	
	private String id;
	private String name;
	private Integer viewed;
	
	public Course() {
		
	}
	
	public Course(Cursor cursor) {
		
	}
	
	public Integer getViewed() {
		return viewed;
	}

	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String courseId) {
		this.id = courseId;
	}
	
}
