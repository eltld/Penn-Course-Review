package edu.upenn.pcr.model.db.entity;

public class CourseCode {
	
	public static final String TABLE_NAME = "CourseCode";
	
	public static final String ID = "cc_id";
	public static final String CODE = "cc_codeId";
	public static final String COURSE_ID = "cc_courseId";
	
	private Integer id;
	private Code code;
	private Course course;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Code getCode() {
		return code;
	}
	
	public void setCode(Code code) {
		this.code = code;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
}
