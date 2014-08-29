package edu.upenn.pcr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import edu.upenn.pcr.activity.StorageSettingActivity;
import edu.upenn.pcr.model.async.GetCourseAsync;
import edu.upenn.pcr.model.async.GetDeptsAsync;
import edu.upenn.pcr.model.async.GetInstructorsAsync;
import edu.upenn.pcr.model.async.GetSemestersAsync;
import edu.upenn.pcr.model.async.TaskGroup;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;

public class StorageSettingController extends Controller {

	private static final int TASK_GET_DEPTS = 0;
	private static final int TASK_GET_INSTRUCTORS = 1;
	private static final int TASK_GET_SEMESTERS = 2;
	private static final int TASK_GET_COURSES = 3;
	
	private StorageSettingActivity storageSettingActivity;
	
	private ArrayList<HashMap<String, Object>> data;

	private PennCourseReviewDBMgr dbMgr;
	
	private TaskGroup taskGroup;
	private boolean isRunning = false;
	
	public StorageSettingController(StorageSettingActivity activity) {
		super(activity);
		this.storageSettingActivity = activity;
		dbMgr = PennCourseReviewDBMgr.getInstance(storageSettingActivity);
	}
	
	public List<? extends Map<String, Object>> loadData() {
		data = new ArrayList<HashMap<String, Object>>();
		long size = dbMgr.getDatabaseSize(storageSettingActivity);
		data.add(entry("Total database size", Long.toString(size/1024)+"KB", false));
		data.add(entry("Reload database", "Clear database and download from web", true));
		return data;
	}
	
	private HashMap<String, Object> entry(String title, String explain, boolean enabled) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("titleEntry", title);
		map.put("explainEntry", explain);
		map.put("isEnabled", enabled);
		return map;
	}
	
	public void onListItemClicked(int position) {
		if (position != 1) {
			return;
		}
		if (isRunning) {
			resumeUI();
		} else {
			startReloading();
		}
		
	}
	
	private void startReloading() {
		isRunning = true;
		
		data.get(1).put("explainEntry", "Reloading... Click to see progress");
		storageSettingActivity.refreshList();
		storageSettingActivity.showProgressDialog("Downloading...");
		
		dbMgr.clearDatabase();
		Log.i("pcr", "Database cleared");
		
		taskGroup = new TaskGroup();
		GetDeptsAsync dept = new GetDeptsAsync(TASK_GET_DEPTS, this);
		taskGroup.registerTask(dept.getSequenceNumber(), 10, 10);
		dept.execute();
		Log.i("pcr", "Departments started...");
		
		GetInstructorsAsync instr = new GetInstructorsAsync(TASK_GET_INSTRUCTORS, this);
		taskGroup.registerTask(instr.getSequenceNumber(), 10, 10);
		instr.execute();
		Log.i("pcr", "Instructor started...");
		
		GetSemestersAsync semester = new GetSemestersAsync(TASK_GET_SEMESTERS, this);
		taskGroup.registerTask(semester.getSequenceNumber(), 10, 10);
		semester.execute();
		Log.i("pcr", "Semester started...");
	}
	
	private void resumeUI() {
		storageSettingActivity.showProgressDialog("Downloading...");
		storageSettingActivity.updateProgressDlg(taskGroup.currentProgress());
	}
	
	private void finishReloading() {
		storageSettingActivity.dismissProgressDialog();
		
		long size = dbMgr.getDatabaseSize(storageSettingActivity);
		data.get(0).put("explainEntry", size/(1024)+"KB");
		data.get(1).put("explainEntry", "Clear database and download from web");
		storageSettingActivity.refreshList();
		
		isRunning = false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		switch (what) {
		case TASK_GET_DEPTS:
			handleGetDepts((ArrayList<String>) result);
			break;
		case TASK_GET_INSTRUCTORS:
			handleGetInstructors();
			break;
		case TASK_GET_SEMESTERS:
			handleGetSemesters();
			break;
		case TASK_GET_COURSES:
			handleGetCourses();
			break;
		default:
			break;
		}
		taskGroup.finishTask(sequenceNumber);
		updateProgress();
	}
	
	private void updateProgress() {
		int progress = taskGroup.currentProgress();
		storageSettingActivity.updateProgressDlg(progress);
		if (progress ==  100) {
			finishReloading();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void handleGetDepts(ArrayList<String> deptIds) {
		Log.i("pcr", "Departments finished!");
		int half = deptIds.size()/2;
		
		List<String> first =deptIds.subList(0, half);
		GetCourseAsync course1 = new GetCourseAsync(TASK_GET_COURSES, this);
		taskGroup.registerTask(course1.getSequenceNumber(), first.size(), 35);
		course1.execute(first);
		Log.i("pcr", "Course started...");
		
		List<String> second = deptIds.subList(half, deptIds.size());
		GetCourseAsync course2 = new GetCourseAsync(TASK_GET_COURSES, this);
		taskGroup.registerTask(course2.getSequenceNumber(), second.size(), 35);
		course2.execute(second);
		Log.i("pcr", "Course started...");
	}
	
	private void handleGetInstructors() {
		Log.i("pcr", "Instructor finished!");
	}
	
	private void handleGetSemesters() {
		Log.i("pcr", "Semester finished!");
	}
	
	private void handleGetCourses() {
		Log.i("pcr", "Course finished!");
	}
 
	@Override
	public void onTaskProgress(int what, int sequenceNumber, Object progress) {
		int value = (Integer) progress;
		taskGroup.updateTaskProgress(sequenceNumber, value);
		updateProgress();
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		finishReloading();
		storageSettingActivity.error("Error loading data from web. Connection down!");
	}

}
