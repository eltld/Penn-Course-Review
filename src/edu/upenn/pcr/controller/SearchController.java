package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.activity.SearchActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.AddRecentAsync;
import edu.upenn.pcr.model.async.SearchAsync;
import edu.upenn.pcr.model.db.entity.InvertedIndex;
import edu.upenn.pcr.model.db.entity.Recent;
import edu.upenn.pcr.model.db.entity.Recent.ItemType;

public class SearchController extends Controller {

	private static final int SEARCH = 0;
	private static final int IGNORE = -1;
	
	private SearchActivity searchActivity;
	
	private List<InvertedIndex> data;
	
	public SearchController(SearchActivity activity) {
		super(activity);
		this.searchActivity = activity;
	}
	
	public void onSubmitButtonClicked() {
		if (data == null || data.size() == 0) {
			return;
		}
		onListItemClicked(0);
	}
	
	public void onListItemClicked(int position) {
		InvertedIndex ii = data.get(position);
		Recent recent = new Recent();
		recent.setRefId(ii.getRefId());
		recent.setFullText(ii.getFullText());
		switch (ii.getType()) {
		case DEPARTMENT:
			recent.setType(ItemType.DEPARTMENT);
			break;
		case COURSE:
			recent.setType(ItemType.COURSE);
			break;
		case INSTRUCTOR:
			recent.setType(ItemType.INSTRUCTOR);
			break;
		default:
			break;
		}
		AddRecentAsync task = new AddRecentAsync(IGNORE, this);
		task.execute(recent);
		ActivityGroupBase activityGroup = (ActivityGroupBase) searchActivity.getParent();
		switch(ii.getType()) {
		case DEPARTMENT:
			activityGroup.startCoursesActivity(ii.getRefId(), ii.getKeyword());
			break;
		case INSTRUCTOR:
			activityGroup.startSectionByInstrActivity(ii.getRefId(), ii.getFullText());
			break;
		case COURSE:
			activityGroup.startSectionByCourseActivity(ii.getRefId(), ii.getFullText());
			break;
		default:
			break;
		}
	}
	
	public void search(String term) {
		SearchAsync task = new SearchAsync(SEARCH, this);
		task.execute(term, 0, 100);
	}

	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		if (what == SEARCH) {
			handleSearch(result);
		} else {
			return;
		}
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		if (what == SEARCH) {
			searchActivity.error("Error whiling creating suggestions");
	
		}
	}
	
	@SuppressWarnings("unchecked")
	private void handleSearch(Object result) {
		data = (List<InvertedIndex>) result;
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SearchActivity.TEXT_ENTRY, data.get(i).getFullText());
			switch(data.get(i).getType()) {
			case DEPARTMENT:
				map.put(SearchActivity.IMAGE_ENTRY, R.drawable.dept);
				break;
			case INSTRUCTOR:
				map.put(SearchActivity.IMAGE_ENTRY, R.drawable.person);
				break;
			case COURSE:
				map.put(SearchActivity.IMAGE_ENTRY, R.drawable.course);
				break;
			default:
				break;
			}
			toReturn.add(map);
		}
		searchActivity.refreshList(toReturn);
	}
	
}
