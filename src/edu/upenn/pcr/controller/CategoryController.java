package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.activity.CategoryActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;

public class CategoryController extends Controller {

	private CategoryActivity categoryActivity;
	private ArrayList<HashMap<String, Object>> data;
	
	
	public CategoryController(CategoryActivity activity) {
		super(activity);
		this.categoryActivity = activity;
	}
	
	public List<HashMap<String, Object>> loadData() {
		data = new ArrayList<HashMap<String, Object>>();
		data.add(entry(R.drawable.browse_depts, "All Departments"));
		data.add(entry(R.drawable.browse_instr, "All Instructors"));
		data.add(entry(R.drawable.recent, "Recent"));
		data.add(entry(R.drawable.help, "Help"));
		return data;
	}
	
	private HashMap<String, Object> entry(Object icon, Object title) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(CategoryActivity.IMAGE_ENTRY, icon);
		map.put(CategoryActivity.TEXT_ENTRY, title);
		return map;
	}
	
	public void onListItemClicked(int position) {
		ActivityGroupBase activityGroup = (ActivityGroupBase) categoryActivity.getParent();
		switch (position) {
		case 0:
			activityGroup.startDepartmentsActivity();
			break;
		case 1:
			activityGroup.startInstructorsActivity();
			break;
		case 2:
			activityGroup.startRecentActivity();
			break;
		case 3:
			activityGroup.startHelpActivity();
			break;
		default:
			break;
		}
	}
}
