package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.activity.InstructorsActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.model.async.AddRecentAsync;
import edu.upenn.pcr.model.async.FetchInstrsAsync;
import edu.upenn.pcr.model.async.FetchInstrsCountAsync;
import edu.upenn.pcr.model.db.entity.Instructor;
import edu.upenn.pcr.model.db.entity.Recent;
import edu.upenn.pcr.model.db.entity.Recent.ItemType;

public class InstrsController extends Controller {

	private static final int FETCH_INSTRS_COUNT = 0;
	private static final int FETCH_INSTRS = 1;
	private static final int IGNORE = -1;
	
	private static final int PAGE_LENGTH = 20;
	
	private InstructorsActivity instrActivity;
	
	private List<Instructor> data = new ArrayList<Instructor>();
	
	private int instrsCount = 0;
	private int currentOffset;
	
	public InstrsController(InstructorsActivity activity) {
		super(activity);
		this.instrActivity = activity;
	}
	
	public void loadInitialPage() {
		FetchInstrsCountAsync task = new FetchInstrsCountAsync(FETCH_INSTRS_COUNT, this);
		task.execute();
	}
	
	public void onListItemClicked(int position) {
		Instructor instr = data.get(position);
		Recent recent = new Recent();
		recent.setRefId(instr.getId());
		recent.setType(ItemType.INSTRUCTOR);
		recent.setFullText(instr.getLastName() + ", " + instr.getFirstName());
		AddRecentAsync task = new AddRecentAsync(IGNORE, this);
		task.execute(recent);
		ActivityGroupBase activityGroup = (ActivityGroupBase) instrActivity.getParent();
		activityGroup.startSectionByInstrActivity(instr.getId(), instr.getLastName() + ", " + instr.getFirstName());
	}
	
	public boolean hasNext() {
		if (currentOffset + PAGE_LENGTH >= instrsCount) {
			return false;
		}
		return true;
	}
	
	public void onNextPageClicked() {
		int start = currentOffset + PAGE_LENGTH;
		if (start >= instrsCount) {
			return;
		}
		int num = PAGE_LENGTH;
		if (start+num >= instrsCount) {
			num = instrsCount - start;
		}
		FetchInstrsAsync task = new FetchInstrsAsync(FETCH_INSTRS, this);
		task.execute(start, num);
	}
	
	/*public boolean hasPrevious() {
		if (currentOffset <= 0) {
			return false;
		}
		return true;
	}
	
	public void onPrevPageClicked() {
		int start = currentOffset - PAGE_LENGTH;
		if (start <= 0) {
			start  = 0;
		}
		int num = currentOffset - start;
		FetchInstrsAsync task = new FetchInstrsAsync(FETCH_INSTRS, this);
		task.execute(start, num);
		currentOffset = start;
	}*/
	
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		switch (what) {
		case FETCH_INSTRS_COUNT:
			handleFetchInstrsCount(result);
			break;
		case FETCH_INSTRS:
			handleFetchInstrs(result);
			break;
		default:
			break;
		}
	}
	
	private void handleFetchInstrsCount(Object result){
		instrsCount = (Integer) result;
		currentOffset = -PAGE_LENGTH;
		onNextPageClicked();
	}
	
	private void handleFetchInstrs(Object result){
		@SuppressWarnings("unchecked")
		List<Instructor> instructors = (List<Instructor>) result;
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < instructors.size(); i++) {
			Instructor instr = instructors.get(i);
			String lastName = instr.getLastName();
			String firstName = instr.getFirstName();
			if ("".equals(lastName.trim())) {
				continue;
			}
			data.add(instr);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(InstructorsActivity.NAME_ENTRY, lastName + ", " + firstName);
			toReturn.add(map);
		}
		instrActivity.nextPage(toReturn);
		currentOffset += PAGE_LENGTH;
	}
	
	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		instrActivity.error("Error loading instructor list");
	}

}
