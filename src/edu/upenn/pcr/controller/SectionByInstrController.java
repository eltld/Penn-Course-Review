package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.upenn.pcr.activity.SectionByInstrActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.FetchSectionByInstrAsync;
import edu.upenn.pcr.model.db.entity.CourseSection;
import edu.upenn.pcr.util.PreferenceUtil;
import edu.upenn.pcr.util.RatingConfig;

public class SectionByInstrController extends Controller {
	private static int FETCH_INSTR_COURSES = 0;
	private SectionByInstrActivity sectByIntrActivity;
	private List<CourseSection> rawCourseSection;
	private List<HashMap<String, Object>> extractedSectionList;
	private List<HashMap<String, Object>> coursesToShow;
	private final String COURSE_DETAIL_ID = "courseDetailID";
	private final String COURSE_CODE = "codeEntry";
	private final String COURSE_NAME = "nameEntry";
	private final String INSTR_NAME = "instrName";	
	private final String DESCRIPTION = "description";
	private final String SEM_ID = "semesterID";
	private final String SEM_NAME = "semesterEntry";
	private final String[] Semesters = {"Sem1","Sem2", "Sem3"};
	private final String[] RatingLabels = {"labelEntry1","labelEntry2","labelEntry3", "labelEntry4"};
	private final String[] RatingValues = {"ratingEntry1","ratingEntry2","ratingEntry3", "ratingEntry4"};
	private final String[] RatingAmount = {"ratingAmount1", "ratingAmount2", "ratingAmount3", "ratingAmount4"};

	public SectionByInstrController(SectionByInstrActivity activity) {
		super(activity);
		this.sectByIntrActivity = activity;	
	}

	@SuppressWarnings("unchecked")
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		rawCourseSection = (List<CourseSection>) result;
		sectByIntrActivity.dismissLoadingDialog();
		extractCourseInfoToShow();
		groupByCourse();
	};

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		sectByIntrActivity.error("Error loading course list");
	}

	public void loadInitialPage(String instrID){
		FetchSectionByInstrAsync task = new FetchSectionByInstrAsync(FETCH_INSTR_COURSES, this);
		task.execute(instrID);
		sectByIntrActivity.showLoadingDialog();
	}

	public void onCourseClicked(int index){
		String courseCode = (String)coursesToShow.get(index).get(COURSE_CODE);
		ArrayList<HashMap<String, Object>> sectsOfACourse = new ArrayList<HashMap<String, Object>>();
		for(HashMap<String, Object> sectMap : extractedSectionList){
			if(courseCode.equals(sectMap.get(COURSE_CODE))){
				HashMap<String, Object> sectCopy = new HashMap<String, Object>();
				for(String key: sectMap.keySet()){
					sectCopy.put(key, sectMap.get(key));
				}
				for(int i=0; i<RatingValues.length; i++){
					if(sectCopy.get(RatingValues[i]) == null || (Double)sectCopy.get(RatingValues[i])<0){
						sectCopy.put(RatingValues[i], "N/A");
					}
				}
				sectsOfACourse.add(sectCopy);
			}
		}
		String instrName = (String) sectsOfACourse.get(0).get(INSTR_NAME);
		ActivityGroupBase activityGroup = (ActivityGroupBase) sectByIntrActivity.getParent();
		activityGroup.startCourseSectionActivity(sectsOfACourse,courseCode,instrName);
	}

	private void extractCourseInfoToShow(){
		extractedSectionList = new ArrayList<HashMap<String, Object>>();		
		HashMap<String, Object> aSectionMap;		

		/*Parse each CourseSection, add parsed courseSection to extractedSectionList*/
		for(CourseSection cs: rawCourseSection){
			aSectionMap = new HashMap<String, Object>();
			aSectionMap.put(COURSE_DETAIL_ID, cs.getCourseDetail().getId());
			aSectionMap.put(COURSE_CODE, cs.getPrimaryAlias());
			aSectionMap.put(COURSE_NAME, cs.getCourseDetail().getCourse().getName());
			aSectionMap.put(INSTR_NAME, cs.getInstructor().getLastName()+", "+cs.getInstructor().getFirstName());
			aSectionMap.put(SEM_ID, cs.getCourseDetail().getSemester().getId());
			aSectionMap.put(SEM_NAME, cs.getCourseDetail().getSemester().getName());
			aSectionMap.put(DESCRIPTION, cs.getCourseDetail().getDescription());
			extractPreferedRatings(cs, aSectionMap);
			extractedSectionList.add(aSectionMap);
		}

	}


	public void groupByCourse(){
		HashMap<String, HashMap<String, Object>> groupedCourseInfo = new HashMap<String, HashMap<String, Object>>();
		for(HashMap<String, Object> csMap: extractedSectionList){
			if(groupedCourseInfo.containsKey(csMap.get(COURSE_CODE))){
				updateRatings(groupedCourseInfo.get(csMap.get(COURSE_CODE)), csMap);
				updateSemesters(groupedCourseInfo.get(csMap.get(COURSE_CODE)), (String)csMap.get(SEM_ID));
			}else{
				HashMap<String, Object> aCourseMap = new HashMap<String, Object>();
				aCourseMap.put(COURSE_CODE, csMap.get(COURSE_CODE));
				aCourseMap.put(COURSE_NAME, csMap.get(COURSE_NAME));
				aCourseMap.put(INSTR_NAME, csMap.get(INSTR_NAME));
				aCourseMap.put(Semesters[0], csMap.get(SEM_ID));

				for(int i=0; i<RatingValues.length; i++){
					if(csMap.get(RatingLabels[i]) != null){
						aCourseMap.put(RatingLabels[i], csMap.get(RatingLabels[i]));					
						if((Double)csMap.get(RatingValues[i]) != -1){
							aCourseMap.put(RatingValues[i], csMap.get(RatingValues[i]));
							aCourseMap.put(RatingAmount[i], 1);
						}
					}

				}
				groupedCourseInfo.put((String) csMap.get(COURSE_CODE), aCourseMap);
			}
		}
		coursesToShow = new ArrayList<HashMap<String, Object>>();
		for(String courseID: groupedCourseInfo.keySet()){
			HashMap<String, Object> cMap = groupedCourseInfo.get(courseID);
			for(int i=0; i<Semesters.length; i++){
				cMap.put(Semesters[i], convertSemToShort((String)cMap.get(Semesters[i])));
			}
			String s = "";
			for (int i = 0; i < Semesters.length; i++) {
				String si = cMap.get(Semesters[i]).toString();
				if (si == null) {
					break;
				}
				s += "  ";
				s += si;
			}
			cMap.put("semesterEntry", s);
			for (int i = 0; i < RatingValues.length; i++) {
				if(cMap.get(RatingValues[i]) == null){
					cMap.put(RatingValues[i], "NA");
					continue;
				}
				String rating = cMap.get(RatingValues[i]).toString();				
				if (rating.length() > 4) {
					rating = rating.substring(0, 4);
				}
				cMap.put(RatingValues[i], rating);				

			}
			coursesToShow.add(cMap);
		}
		sectByIntrActivity.refreshList(coursesToShow);

	}


	private void updateRatings(HashMap<String, Object> acourseMap, HashMap<String, Object> csMap){
		//Update Ratings	
		for(int i=0; i<RatingLabels.length; i++){	
			if(csMap.get(RatingLabels[i]) != null){
				if((Double)csMap.get(RatingValues[i]) != -1){
					updateARating(acourseMap, RatingValues[i], (Double)csMap.get(RatingValues[i]), i);
				}
			}
		}

	}


	private void updateSemesters(HashMap<String, Object> csInfoMap, String newSem){
		for(int i=Semesters.length-1; i>=0; i--){
			if(csInfoMap.containsKey(Semesters[i])){
				if(newSem.compareTo((String)csInfoMap.get(Semesters[i]))>0){
					if(i+1<Semesters.length){
						csInfoMap.put(Semesters[i+1], csInfoMap.get(Semesters[i]));
					}
					csInfoMap.put(Semesters[i], newSem);
				}else{
					return;
				}			
			}else{
				csInfoMap.put(Semesters[i], newSem);
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
		for(int i=0; i<RatingLabels.length; i++){
			if(!csInfoMap.containsKey(RatingLabels[i])){
				csInfoMap.put(RatingLabels[i], prefLabel);
				csInfoMap.put(RatingValues[i], prefValue);
				break;
			}
		}
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
