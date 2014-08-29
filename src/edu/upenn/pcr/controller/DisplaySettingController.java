package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.pcr.activity.DisplaySettingActivity;
import edu.upenn.pcr.util.PreferenceUtil;
import edu.upenn.pcr.util.RatingConfig;

public class DisplaySettingController extends Controller {

	private DisplaySettingActivity displaySettingActivity;
	
	private List<HashMap<String, Object>> data;
	
	private int checkedCount;
	
	public DisplaySettingController(DisplaySettingActivity activity) {
		super(activity);
		this.displaySettingActivity = activity;
		//PreferenceUtil.reset();
	}
	
	public List<? extends Map<String, Object>> loadData() {
		List<RatingConfig> ratingConfigs = PreferenceUtil.getRatingConfigsByPriority();
		data = new ArrayList<HashMap<String, Object>>();
		checkedCount = 0;
		for (int i = 0; i < ratingConfigs.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			RatingConfig config = ratingConfigs.get(i);
			map.put("id", config.getId());
			map.put(DisplaySettingActivity.TITLE_ENTRY, config.getTitle());
			map.put(DisplaySettingActivity.EXPLAIN_ENTRY, config.getDescription());
			boolean value = config.isChecked();
			if (value) {
				checkedCount ++;
			}
			map.put(DisplaySettingActivity.CHECK_ENTRY, value);
			data.add(map);
		}
		updateEnableStatus();
		return data;
	}
	
	private void updateEnableStatus() {
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = data.get(i);
			map.put("isEnabled", isEnabled(i));
		}
	}
	
	private boolean isEnabled(int position) {
		if (checkedCount < 4) {
			return true;
		}
		return (Boolean) data.get(position).get(DisplaySettingActivity.CHECK_ENTRY);
	}
	
	public void onListItemClicked(int position) {
		HashMap<String, Object> map = data.get(position);
		boolean checked = (Boolean) map.get(DisplaySettingActivity.CHECK_ENTRY);
		map.put(DisplaySettingActivity.CHECK_ENTRY, !checked);
		RatingConfig config = PreferenceUtil.findRatingConfigById((Integer) data.get(position).get("id"));
		config.setChecked(!checked);
		PreferenceUtil.updateRatingConfig(config);
		if (checked) {
			checkedCount --;
		} else {
			checkedCount ++;
		}
		updateEnableStatus();
		displaySettingActivity.refreshList();
	}
}
