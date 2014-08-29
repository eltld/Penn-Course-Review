package edu.upenn.pcr.model.db.entity;

import java.util.ArrayList;
import java.util.List;

public class CourseSection {
	
	public static final String TABLE_NAME = "CourseSection";

	public static final String COURSE_DETAIL_ID = "cs_courseDetailId";
	public static final String PRIMARY_ALIAS = "cs_primaryAlias";
	public static final String SECTION = "cs_section";
	public static final String INSTRUCTOR_ID = "cs_instrId";
	public static final String RATINGS = "cs_ratings";
    
	private CourseDetail courseDetail;
	private String primaryAlias;
	private String section;
	private Instructor instructor;
	private List<Double> ratings;
	
	public CourseSection() {
		ratings = new ArrayList<Double>();
		for (int i = 0; i < 25; i++) {
			ratings.add(-(1.0));
		}
	}
	
	public CourseDetail getCourseDetail() {
		return courseDetail;
	}
	
	public String getPrimaryAlias() {
		return primaryAlias;
	}

	public void setPrimaryAlias(String primaryAlias) {
		this.primaryAlias = primaryAlias;
	}

	public void setCourseDetail(CourseDetail courseDetail) {
		this.courseDetail = courseDetail;
	}
	
	public String getSection() {
		return section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public Instructor getInstructor() {
		return instructor;
	}
	
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	public void setRating(int index, double rating) {
		if (index >= ratings.size()) {
			return;
		}
		ratings.set(index, rating);
	}
	
	public List<Double> getRatings() {
		return this.ratings;
	}
}
