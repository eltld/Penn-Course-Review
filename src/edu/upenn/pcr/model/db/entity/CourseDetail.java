package edu.upenn.pcr.model.db.entity;

public class CourseDetail {

	public static final String TABLE_NAME = "CourseDetail";
	
	public static final String ID = "cd_id";
	public static final String COURSE_ID = "cd_courseId";
	public static final String SEMESTER_ID = "cd_semesterId";
	public static final String DESCRIPTION = "cd_description";
	
	private String id;
	private Course course;
	private Semester semester;
	private String description;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public Semester getSemester() {
		return semester;
	}
	
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
