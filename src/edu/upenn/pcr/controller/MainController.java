package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.LocalActivityManager;
import edu.upenn.pcr.R;
import edu.upenn.pcr.activity.MainActivity;
import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import edu.upenn.pcr.activitygroup.CategoryActivityGroup;
import edu.upenn.pcr.activitygroup.SearchActivityGroup;
import edu.upenn.pcr.activitygroup.SettingsActivityGroup;

public class MainController extends Controller {

	private MainActivity mainActivity;
	private ArrayList<HashMap<String, Object>> tabs;
	
	public MainController(MainActivity activity) {
		super(activity);
		this.mainActivity = activity;
	}
	
	public List<? extends Map<String, Object>> loadTabs() {
		tabs = new ArrayList<HashMap<String, Object>>();
		tabs.add(tab("activitygroup.CategoryActivityGroup", "Categories", R.drawable.categories_tab, CategoryActivityGroup.class));
		tabs.add(tab("activitygroup.SearchActivityGroup", "Search", R.drawable.search_tab, SearchActivityGroup.class));
		tabs.add(tab("activitygroup.SettingsActivityGroup", "Settings", R.drawable.settings_tab, SettingsActivityGroup.class));		
		return tabs;
	}
	
	private HashMap<String, Object> tab(String id, String label, int icon, Class<?> clazz) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("label", label);
		map.put("icon", icon);
		map.put("class", clazz);
		return map;
	}
	
	public void onTabClicked(int position) {
		LocalActivityManager activityMgr = mainActivity.getLocalActivityManager();
		String id = tabs.get(position).get("id").toString();
		ActivityGroupBase activityGroup = (ActivityGroupBase) activityMgr.getActivity(id);
		if (activityGroup != null) {
			activityGroup.resetContent();
		}
	}
}
