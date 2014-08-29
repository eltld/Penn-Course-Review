package edu.upenn.pcr.model.db.entity;

public class Code {

	public static final String TABLE_NAME = "Code";
	
	public static final String CODE = "code_code";
	public static final String DEPT_ID = "code_deptId";
	
	private String code;
	private Department dept;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Department getDepartment() {
		return dept;
	}
	
	public void setDepartment(Department dept) {
		this.dept = dept;
	}
	
}
