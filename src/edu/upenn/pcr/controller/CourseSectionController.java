package edu.upenn.pcr.controller;

import java.util.List;
import java.util.HashMap;
import edu.upenn.pcr.activity.CourseSectionActivity;

public class CourseSectionController extends Controller {

	private final String DESCRIPTION = "description";
	private List<HashMap<String, Object>> data;
	
	public CourseSectionController(CourseSectionActivity context, List<HashMap<String, Object>> data) {
		super(context);
		this.data = data;
	}

	public void loadInitialPage(){
		//update activity with data
		
	}
	
	public void onCourseSectionClicked(int index){
		HashMap<String, Object> aSectionMap = data.get(index);
		String  description = (String)aSectionMap.get(DESCRIPTION);
	}

	
}
