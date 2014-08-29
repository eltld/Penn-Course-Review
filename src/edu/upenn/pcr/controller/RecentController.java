package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.activity.RecentActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.LoadRecentAsync;
import edu.upenn.pcr.model.db.entity.Recent;

public class RecentController extends Controller {

	private RecentActivity recentActivity;
	private List<Recent> data;
	
	public RecentController(RecentActivity activity) {
		super(activity);
		this.recentActivity = activity;
	}

	public void loadData() {
		LoadRecentAsync task = new LoadRecentAsync(0, this);
		task.execute(0, 30);
	}

	public void onListItemClicked(int position) {
		Recent recent = data.get(position);
		ActivityGroupBase activityGroup = (ActivityGroupBase) recentActivity.getParent();
		switch(recent.getType()) {
		case DEPARTMENT:
			activityGroup.startCoursesActivity(recent.getRefId(), recent.getFullText());
			break;
		case INSTRUCTOR:
			activityGroup.startSectionByInstrActivity(recent.getRefId(), recent.getFullText());
			break;
		case COURSE:
			activityGroup.startSectionByCourseActivity(recent.getRefId(), recent.getFullText());
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		data = (List<Recent>) result;
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			Recent recent = data.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(RecentActivity.TEXT_ENTRY, recent.getFullText());
			switch(data.get(i).getType()) {
			case DEPARTMENT:
				map.put(RecentActivity.IMAGE_ENTRY, R.drawable.dept);
				break;
			case INSTRUCTOR:
				map.put(RecentActivity.IMAGE_ENTRY, R.drawable.person);
				break;
			case COURSE:
				map.put(RecentActivity.IMAGE_ENTRY, R.drawable.course);
				break;
			default:
				break;
			}
			toReturn.add(map);
		}
		recentActivity.refreshList(toReturn);
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		recentActivity.error("Error getting recent views");
	}
	
	
}
