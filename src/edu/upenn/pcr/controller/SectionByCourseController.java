package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.activity.SectionByCourseActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.FetchSectionByCourseAsync;
import edu.upenn.pcr.model.db.entity.CourseSection;
import edu.upenn.pcr.util.PreferenceUtil;
import edu.upenn.pcr.util.RatingConfig;

public class SectionByCourseController extends Controller {

	private static int FETCH_DPT_COURSE = 0;
	private String courseID;
	private SectionByCourseActivity sbcActivity;

	private List<CourseSection> allCourseSection;
	private List<HashMap<String, Object>>castSectionList;
	private List<HashMap<String, Object>>coursesToShow;

	private final String COURSE_CODE = "codeEntry";
	private final String COURSE_NAME = "nameEntry";
	private final String INSTR_ID = "Instr_Id";
	private final String INSTR_NAME = "instrNameEntry";
	private final String COURSE_DETAIL_ID = "Course_detailId";
	private final String SEM_ID = "Semester_Id";
	private final String SEM_NAME = "semesterEntry";
	private final String[] SEMS = {"SemOne", "SemTwo", "SemThree"};
	private final String[] RatingLabels = {"labelEntry1","labelEntry2","labelEntry3", "labelEntry4"};
	private final String[] RatingValues = {"ratingEntry1","ratingEntry2","ratingEntry3", "ratingEntry4"};
	private final String[] RatingAmount = {"ratingAmount1", "ratingAmount2", "ratingAmount3", "ratingAmount4"};
	private final String SECT_AMOUNT = "sectionAmount";

	public SectionByCourseController(SectionByCourseActivity activity, String courseId) {
		super(activity);
		this.sbcActivity = activity;
		this.courseID = courseId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		allCourseSection = (List<CourseSection>) result;
		sbcActivity.dismissLoadingDialog();
		castToCourseMap();
		groupByInstructor();
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		sbcActivity.error("Error loading course list");
	}

	public void loadInitialPage() {
		FetchSectionByCourseAsync task = new FetchSectionByCourseAsync(FETCH_DPT_COURSE, this);
		task.execute(courseID);
		sbcActivity.showLoadingDialog();
	}

	public void onInstructorClicked(int index) {
		String instrID = (String) coursesToShow.get(index).get(INSTR_ID);
		ArrayList<HashMap<String, Object>>sectionsOfCourse = new ArrayList<HashMap<String, Object>>();
		for(HashMap<String, Object>map : castSectionList) {
			if(instrID.equals(map.get(INSTR_ID))){
				HashMap<String, Object> sectCopy = new HashMap<String, Object>();
				for(String key: map.keySet()){
					sectCopy.put(key, map.get(key));
				}
				for(int i=0; i<RatingValues.length; i++){
					if(sectCopy.get(RatingValues[i]) == null || (Double)sectCopy.get(RatingValues[i])<0){
						sectCopy.put(RatingValues[i], "N/A");
					}
				}
				sectionsOfCourse.add(sectCopy);
			}
		}
		String courseId = (String)sectionsOfCourse.get(0).get(COURSE_CODE);
		ActivityGroupBase activityGroup = (ActivityGroupBase) sbcActivity.getParent();
		activityGroup.startCourseSectionActivity(sectionsOfCourse, courseId, instrID);
		//return sectionsOfCourse;
	}

	private void castToCourseMap() {
		castSectionList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> courseSectionMap;
		for(CourseSection cs:allCourseSection) {
			courseSectionMap = new HashMap<String, Object>();
			courseSectionMap.put(INSTR_ID, cs.getInstructor().getId());
			courseSectionMap.put(COURSE_DETAIL_ID, cs.getCourseDetail().getId());
			courseSectionMap.put(COURSE_CODE, cs.getPrimaryAlias());
			courseSectionMap.put(COURSE_NAME, cs.getCourseDetail().getCourse().getName());
			courseSectionMap.put(SEM_ID, cs.getCourseDetail().getSemester().getId());
			courseSectionMap.put(SEM_NAME, cs.getCourseDetail().getSemester().getName());
			courseSectionMap.put(INSTR_NAME, cs.getInstructor().getLastName() + "," + cs.getInstructor().getFirstName());

			extractPreferedRatings(cs, courseSectionMap);
			castSectionList.add(courseSectionMap);
		}
	}

	public void groupByInstructor() {
		HashMap<String, HashMap<String, Object>>groupedCourseMap = new HashMap<String, HashMap<String, Object>>();
		for(HashMap<String, Object> courseMap : castSectionList) {
			if(groupedCourseMap.containsKey(courseMap.get(INSTR_ID))) {
				updateRatings(groupedCourseMap.get(courseMap.get(INSTR_ID)), courseMap);
				updateSemesters(groupedCourseMap.get(courseMap.get(INSTR_ID)), (String) courseMap.get(SEM_ID));
			}else {
				HashMap<String, Object> seleInfoMap = new HashMap<String, Object>();
				seleInfoMap.put(INSTR_ID, courseMap.get(INSTR_ID));
				seleInfoMap.put(COURSE_CODE, courseMap.get(COURSE_CODE));
				seleInfoMap.put(COURSE_NAME, courseMap.get(COURSE_NAME));
				seleInfoMap.put(INSTR_NAME, courseMap.get(INSTR_NAME));
				seleInfoMap.put(SEMS[0], courseMap.get(SEM_ID));
				seleInfoMap.put(SECT_AMOUNT, 1);
				for(int i=0; i<RatingLabels.length; i++){
					if(courseMap.get(RatingLabels[i]) != null){
						seleInfoMap.put(RatingLabels[i], courseMap.get(RatingLabels[i]));				
						if((Double)courseMap.get(RatingValues[i]) != -1){
							seleInfoMap.put(RatingValues[i], courseMap.get(RatingValues[i]));
							seleInfoMap.put(RatingAmount[i], 1);
						}
					}
				}
				groupedCourseMap.put((String) courseMap.get(INSTR_ID), seleInfoMap);
			}
		}
		coursesToShow = new ArrayList<HashMap<String, Object>>();
		for(String instrId:groupedCourseMap.keySet()) {
			HashMap<String, Object> gMap = groupedCourseMap.get(instrId);
			for(int i=0; i<SEMS.length; i++) {
				gMap.put(SEMS[i], convertSemToShort((String) gMap.get(SEMS[i])));
			}
			String s = "";
			for (int i = 0; i < SEMS.length; i++) {
				String si = gMap.get(SEMS[i]).toString();
				if (si == null) {
					break;
				}
				s += "  ";
				s += si;
			}
			gMap.put("semesterEntry", s);
			for (int i = 0; i < RatingValues.length; i++) {
				if(gMap.get(RatingValues[i]) == null){
					gMap.put(RatingValues[i], "NA");
					continue;
				}
				String rating = gMap.get(RatingValues[i]).toString();
				if (rating.length() > 4) {
					rating = rating.substring(0, 4);
				}
				gMap.put(RatingValues[i], rating);
			}
			coursesToShow.add(gMap);
		}
		sbcActivity.refreshList(coursesToShow);
	}

	private void extractPreferedRatings(CourseSection cs, HashMap<String, Object> sectionMap){
		List<RatingConfig> ratingConfigs = PreferenceUtil.getRatingConfigsByPriority();
		for (int i = 0; i < ratingConfigs.size(); i++) {
			RatingConfig config = ratingConfigs.get(i);
			if (config.isChecked()) {
				addPreferedRating(sectionMap, config.getShortName(), cs.getRatings().get(config.getId()));
			}
		}
	} 

	private void addPreferedRating(HashMap<String, Object> csInfoMap, String prefLabel, double prefValue){
		for(int i=0; i<RatingLabels.length; i++) {
			if(!csInfoMap.containsKey(RatingLabels[i])) {
				csInfoMap.put(RatingLabels[i], prefLabel);
				csInfoMap.put(RatingValues[i], prefValue);
				break;
			}
		}
	}


	private void updateRatings(HashMap<String, Object> csInfoMap, HashMap<String, Object> csMap){
		//Update Ratings	
		for(int i=0; i<RatingLabels.length; i++){
			if(csMap.get(RatingLabels[i]) != null){
				if((Double)csMap.get(RatingValues[i]) != -1){
					updateARating(csInfoMap, RatingValues[i], (Double)csMap.get(RatingValues[i]), i);
				}
			}
		}
		csInfoMap.put(SECT_AMOUNT, (Integer)csInfoMap.get(SECT_AMOUNT)+1);
	}


	private void updateSemesters(HashMap<String, Object> csInfoMap, String newSem){
		for(int i=SEMS.length-1; i>=0; i--){
			if(csInfoMap.containsKey(SEMS[i])){
				if(newSem.compareTo((String)csInfoMap.get(SEMS[i]))>0){
					if(i+1<SEMS.length){
						csInfoMap.put(SEMS[i+1], csInfoMap.get(SEMS[i]));
					}
					csInfoMap.put(SEMS[i], newSem);
				}else{
					return;
				}			
			}else{
				csInfoMap.put(SEMS[i], newSem);
			}	
		}
	}


	private void updateARating(HashMap<String, Object> aCourseMap, String rvKey, double rValue, int index){

		double oldValue = (Double)aCourseMap.get(rvKey);

		int sectAmount = (Integer) aCourseMap.get(RatingAmount[index]);
		double newValue = (oldValue * sectAmount + rValue)/(sectAmount + 1);
		aCourseMap.put(RatingAmount[index], (Integer)aCourseMap.get(RatingAmount[index]) + 1);
		aCourseMap.put(rvKey, newValue);	

	}


	private String convertSemToShort(String sem){
		if (sem == null) return "";

		String semester = "";
		String twoDigYear = sem.substring(2,  4);
		String season = sem.substring(4);
		if(season.equals("A")) season = "Spr'";
		else if(season.equals("B")) season = "Smr'";
		else season = "Fal'";

		semester = season+twoDigYear;
		return semester;
	}

}
