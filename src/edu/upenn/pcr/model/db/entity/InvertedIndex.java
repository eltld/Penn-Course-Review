package edu.upenn.pcr.model.db.entity;

public class InvertedIndex {

	public static final String TABLE_NAME = "InvertedIndex";
	
	public static final String ID = "ii_id";
	public static final String KEYWORD = "ii_keyword";
	public static final String FULL_TEXT = "ii_fullText";
	public static final String ITEM_TYPE = "ii_type";
	public static final String REFERENCE_ID = "ii_refId";
	
	public enum ItemType {
		DEPARTMENT,
		INSTRUCTOR,
		COURSE;
	}
	
	private Integer id;
	private String keyword;
	private String fullText;
	private ItemType type;
	private String refId;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getFullText() {
		return fullText;
	}
	
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
	
	public ItemType getType() {
		return type;
	}
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public String getRefId() {
		return refId;
	}
	
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
}
