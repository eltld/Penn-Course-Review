package edu.upenn.pcr.model.async;

import java.util.HashMap;

import android.util.SparseArray;

public class TaskGroup {

	private SparseArray<HashMap<String, Integer>> taskStates;

	public TaskGroup() {
		taskStates = new SparseArray<HashMap<String, Integer>>();
	}

	public void registerTask(int id, int max, int weightedMax) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("max", max);
		map.put("weightedMax", weightedMax);
		map.put("current", 0);
		HashMap<String, Integer> exists = taskStates.get(id);
		if (exists != null) {
			map.put("max", exists.get("max")+map.get("max"));
			map.put("weightedMax", exists.get("weightedMax")+map.get("weightedMax"));
			map.put("current", exists.get("current")+map.get("current"));
		} 
		taskStates.put(id, map);
	}
	
	public void updateTaskProgress(int id, int value) {
		HashMap<String, Integer> taskState = taskStates.get(id);
		if (taskState == null) {
			return;
		}
		taskState.put("current", taskState.get("current")+value);
	}
	
	public void finishTask(int id) {
		HashMap<String, Integer> taskState = taskStates.get(id);
		if (taskState == null) {
			return;
		}
		taskState.put("current", taskState.get("max"));
	}
	
	public int currentProgress() {
		int progress = 0;
		for (int i = 0; i < taskStates.size(); i++) {
			int key = taskStates.keyAt(i);
			HashMap<String, Integer> taskState = taskStates.get(key);
			int current = taskState.get("current");
			int max = taskState.get("max");
			int weightedMax = taskState.get("weightedMax");
			progress += ((weightedMax*current)/max);
		}
		return progress;
	}

}
