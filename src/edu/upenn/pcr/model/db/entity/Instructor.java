package edu.upenn.pcr.model.db.entity;

public class Instructor {
	
	public static final String TABLE_NAME = "Instructor";

	public static final String ID = "instr_id";
	public static final String LAST_NAME = "instr_lastName";
	public static final String FIRST_NAME = "instr_firstName";
	public static final String VIEWED = "instr_viewed";
	
	private String id;
	private String lastName;
	private String firstName;
	private Integer viewed;
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Integer getViewed() {
		return viewed;
	}

	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}
	
}
