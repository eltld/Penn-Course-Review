package edu.upenn.pcr.model.db.entity;

public class Recent {

	public static final String TABLE_NAME = "Recent";
	
	public static final String FULL_TEXT = "recent_fullText";
	public static final String TYPE = "recent_type";
	public static final String REFERENCE_ID = "recent_refId";
	public static final String TIME_STAMP = "recent_timeStamp";
	
	private String fullText;
	private ItemType type;
	private String refId;
	private Long timeStamp;
	
	public enum ItemType {
		DEPARTMENT,
		COURSE,
		INSTRUCTOR;
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
	
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
