package edu.upenn.pcr.model.db.entity;

public class Department {

	public static final String TABLE_NAME = "Department";
	
	public static final String ID = "dept_id";
	public static final String NAME = "dept_name";
	public static final String VIEWED = "dept_viewd";
	
	private String id;
	private String name;
	private Integer viewed;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Integer getViewed() {
		return viewed;
	}

	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}

}
