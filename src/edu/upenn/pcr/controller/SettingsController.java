package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.pcr.R;
import edu.upenn.pcr.activity.SettingsActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;

public class SettingsController extends Controller {

	private SettingsActivity settingsActivity;
	private ArrayList<HashMap<String, Object>> data;
	
	public SettingsController(SettingsActivity activity) {
		super(activity);
		this.settingsActivity = activity;
	}
	
	public List<? extends Map<String, Object>> loadData() {
		data = new ArrayList<HashMap<String, Object>>();
		data.add(entry(R.drawable.storage,"Storage"));
		data.add(entry(R.drawable.display,"Display"));
		return data;
	}
	
	private HashMap<String, Object> entry(Object icon, String label) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(SettingsActivity.IMAGE_ENTRY, icon);
		map.put(SettingsActivity.TEXT_ENTRY, label);		
		return map;
	}
	
	public void onListItemClicked(int position) {
		ActivityGroupBase activityGroup = (ActivityGroupBase) settingsActivity.getParent();
		if (position == 0) {
			activityGroup.startStorageSettingsActivity();
		} else if (position == 1) {
			activityGroup.startDisplaySettingsActivity();
		}
	}
}
