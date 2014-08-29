package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.activity.DepartmentsActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.AddRecentAsync;
import edu.upenn.pcr.model.async.FetchDeptsAsync;
import edu.upenn.pcr.model.async.FetchDeptsCountAsync;
import edu.upenn.pcr.model.db.entity.Department;
import edu.upenn.pcr.model.db.entity.Recent;
import edu.upenn.pcr.model.db.entity.Recent.ItemType;

public class DeptsController extends Controller {

	private static final int FETCH_DEPTS_COUNT = 0;
	private static final int FETCH_DEPTS_NEXT = 1;
	private static final int IGNORE = -1;
	//private static final int FETCH_DEPTS_PREVIOUS = 2;
	
	private static final int PAGE_LENGTH = 20;
	
	private DepartmentsActivity deptsActivity;
	
	private List<Department> data = new ArrayList<Department>();
	
	private int deptsCount = 0;
	private int currentOffset;
	
	public DeptsController(DepartmentsActivity activity) {
		super(activity);
		this.deptsActivity = activity;
	}
	
	public void loadInitialPage() {
		FetchDeptsCountAsync task = new FetchDeptsCountAsync(FETCH_DEPTS_COUNT, this);
		task.execute();
	}
	
	public void onListItemClicked(int position) {
		Department dept = data.get(position);
		Recent recent = new Recent();
		recent.setRefId(dept.getId());
		recent.setType(ItemType.DEPARTMENT);
		recent.setFullText(dept.getId() + " - " + dept.getName());
		AddRecentAsync task = new AddRecentAsync(IGNORE, this);
		task.execute(recent);
		ActivityGroupBase activityGroup = (ActivityGroupBase) deptsActivity.getParent();

		activityGroup.startCoursesActivity(data.get(position).getId(), data.get(position).getName());

	}
	
	/*public boolean hasPrevious() {
		if (currentOffset <= 0) {
			return false;
		}
		return true;
	}
	
	public void onPrevPageClicked() {
		if (currentOffset <= 0) {
			return;
		}
		int start = currentOffset - PAGE_LENGTH;
		if (start <= 0) {
			start  = 0;
		}
		int num = currentOffset - start;
		FetchDeptsAsync task = new FetchDeptsAsync(FETCH_DEPTS_PREVIOUS, this);
		task.execute(start, num);
	}*/
	
	public boolean hasNext() {
		if (currentOffset + PAGE_LENGTH >= deptsCount) {
			return false;
		}
		return true;
	}
	
	public void onNextPageClicked() {
		int start = currentOffset + PAGE_LENGTH;
		if (start >= deptsCount) {
			return;
		}
		int num = PAGE_LENGTH;
		if (start+num >= deptsCount) {
			num = deptsCount - start;
		}
		FetchDeptsAsync task = new FetchDeptsAsync(FETCH_DEPTS_NEXT, this);
		task.execute(start, num);
	}
	
	
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		switch (what) {
		case FETCH_DEPTS_COUNT:
			handleFetchDeptsCount(result);
			break;
		case FETCH_DEPTS_NEXT:
			handleFetchDeptsNext(result);
			break;
		//case FETCH_DEPTS_PREVIOUS:
			//break;
		default:
			break;
		}
	}
	
	private void handleFetchDeptsCount(Object result) {
		deptsCount = (Integer) result;
		currentOffset = -PAGE_LENGTH;
		onNextPageClicked();
	}
	
	@SuppressWarnings("unchecked")
	private void handleFetchDeptsNext(Object result) {
		List<Department> depts = (List<Department>) result;
		data.addAll(depts);
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < depts.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(DepartmentsActivity.ID_ENTRY, depts.get(i).getId());
			map.put(DepartmentsActivity.NAME_ENTRY, depts.get(i).getName());
			toReturn.add(map);
		}
		deptsActivity.nextPage(toReturn);
		currentOffset += PAGE_LENGTH;
	}
	
	/*private void handleFetchDeptsPrevious(Object result) {
		List<Department> depts = (List<Department>) result;
		currentOffset -= depts.size();
	}*/

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		deptsActivity.error("Error reading data from database");
	}
}
