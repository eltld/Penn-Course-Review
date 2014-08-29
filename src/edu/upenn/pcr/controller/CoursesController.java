package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.activity.CoursesActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.AddRecentAsync;
import edu.upenn.pcr.model.async.FetchCoursesAsync;
import edu.upenn.pcr.model.async.FetchCoursesCountAsync;
import edu.upenn.pcr.model.db.entity.CourseCode;
import edu.upenn.pcr.model.db.entity.Recent;
import edu.upenn.pcr.model.db.entity.Recent.ItemType;

public class CoursesController extends Controller {

	private static final int FETCH_COURSES_COUNT = 0;
	private static final int FETCH_COURSES = 1;
	private static final int IGNORE = -1;
	
	private static final int PAGE_LENGTH = 20;
	
	private CoursesActivity coursesActivity;
	
	private String depId;
	
	private List<CourseCode> data = new ArrayList<CourseCode>();
	
	private int coursesCount;
	private int currentOffset;
	
	public CoursesController(CoursesActivity activity, String depId) {
		super(activity);
		this.coursesActivity = activity;
		this.depId = depId;
	}
	
	public void loadInitialPage() {
		FetchCoursesCountAsync task = new FetchCoursesCountAsync(FETCH_COURSES_COUNT, this);
		task.execute(depId);
	}
	
	public void onListItemClicked(int position) {
		CourseCode cc = data.get(position);
		Recent recent = new Recent();
		recent.setRefId(cc.getCourse().getId());
		recent.setType(ItemType.COURSE);
		recent.setFullText(cc.getCode().getCode() + "  " + cc.getCourse().getName());
		AddRecentAsync task = new AddRecentAsync(IGNORE, this);
		task.execute(recent);
		ActivityGroupBase activityGroup = (ActivityGroupBase) coursesActivity.getParent();
		activityGroup.startSectionByCourseActivity(cc.getCourse().getId(), cc.getCode().getCode() + "  " + cc.getCourse().getName());
	}
	
	/*public void onPrevPageClicked() {
		int start = currentOffset - PAGE_LENGTH;
		if(start <= 0) {
			start = 0;
			//prev button grey
		}
		int num = currentOffset - start;
		FetchCoursesAsync task = new FetchCoursesAsync(FETCH_COURSES, this);
		task.execute(depId, start, num);
		currentOffset = start;
	}*/
	
	public boolean hasNext() {
		if (currentOffset + PAGE_LENGTH >= coursesCount) {
			return false;
		}
		return true;
	}
	
	public void onNextPageClicked() {
		int start = currentOffset + PAGE_LENGTH;
		if (start >= coursesCount) {
			return;
		}
		int num = PAGE_LENGTH;
		if(start + num >= coursesCount) {
			num = coursesCount - start;
		}
		FetchCoursesAsync task = new FetchCoursesAsync(FETCH_COURSES, this);
		task.execute(depId, start, num);
	}
	
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result){
		switch(what) {
		case FETCH_COURSES_COUNT:
			handleCoursesCount(result);
			break;
		case FETCH_COURSES:
			handleFetchCourses(result);
			break;
		default:
			break;
		}
	}
	
	public void handleCoursesCount(Object result) {
		coursesCount = (Integer)result;
		currentOffset = -PAGE_LENGTH;
		onNextPageClicked();
	}
	
	public void handleFetchCourses(Object result) {
		@SuppressWarnings("unchecked")
		List<CourseCode> courses = (List<CourseCode>)result;
		data.addAll(courses);
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < courses.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(CoursesActivity.CODE_ENTRY, courses.get(i).getCode().getCode());
			map.put(CoursesActivity.NAME_ENTRY, courses.get(i).getCourse().getName());
			toReturn.add(map);
		}
		coursesActivity.nextPage(toReturn);
		currentOffset += PAGE_LENGTH;
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		coursesActivity.error("Error loading course list");
	}
}
