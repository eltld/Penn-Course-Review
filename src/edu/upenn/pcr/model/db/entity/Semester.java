package edu.upenn.pcr.model.db.entity;

public class Semester{

	public static final String TABLE_NAME = "Semester";
	
	public static final String ID = "semester_id";
	public static final String NAME = "semester_name";
	public static final String YEAR = "semester_year";
	public static final String SEASON = "semester_season";
	
	private String id;
	private String name;
	private Integer year;
	private String season;
	
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	
}
