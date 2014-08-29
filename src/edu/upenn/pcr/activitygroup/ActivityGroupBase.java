package edu.upenn.pcr.activitygroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.upenn.pcr.activity.CategoryActivity;
import edu.upenn.pcr.activity.CourseSectionActivity;
import edu.upenn.pcr.activity.CoursesActivity;
import edu.upenn.pcr.activity.DepartmentsActivity;
import edu.upenn.pcr.activity.DisplaySettingActivity;
import edu.upenn.pcr.activity.HelpActivity;
import edu.upenn.pcr.activity.InstructorsActivity;
import edu.upenn.pcr.activity.MainActivity;
import edu.upenn.pcr.activity.RecentActivity;
import edu.upenn.pcr.activity.SearchActivity;
import edu.upenn.pcr.activity.SectionByCourseActivity;
import edu.upenn.pcr.activity.SectionByInstrActivity;
import edu.upenn.pcr.activity.SettingsActivity;
import edu.upenn.pcr.activity.StorageSettingActivity;

public abstract class ActivityGroupBase extends ActivityGroup {

	protected Stack<View> history = new Stack<View>();
	
	protected void showContent(String id, Intent intent) {
		View newView = this.getLocalActivityManager().startActivity(id, intent).getDecorView();
		publishContent(newView);
		history.push(newView);
	}
	
	private void publishContent(View newView) {
		setContentView(newView);
		//The main tabactivity just steals focus. Need to explicitly clear it
		MainActivity mainActivity = (MainActivity) getParent();
		mainActivity.clearFocus();
	}
	

	@Override
	public void onBackPressed() {
		history.pop();
		if (history.isEmpty()) {
			super.onBackPressed();
		} else {
			View oldView = history.peek();
			publishContent(oldView);
		}
	}
	
	public void resetContent() {
		history.clear();
	}
	
	public void startCategoryActivity() {
		Intent intent = new Intent().setClass(this, CategoryActivity.class);
		showContent("activity.CategoryActivity", intent);
	}
	
	public void startSectionByInstrActivity (String instrId, String title) {
		Intent intent = new Intent().setClass(this, SectionByInstrActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("instrId", instrId);
		intent.putExtra("title", title);
		showContent("activity.SectionByIntrActivity", intent);
	}
	
	public void startSectionByCourseActivity(String courseId, String title) {
		Intent intent = new Intent().setClass(this, SectionByCourseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("courseId", courseId);
		intent.putExtra("title", title);
		showContent("activity.SectionByCourseActivity", intent);
	}
	
	public void startCoursesActivity(String deptId, String deptName) {
		Intent intent = new Intent().setClass(this, CoursesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("deptId", deptId);
		intent.putExtra("title", deptName);
		showContent("activity.CoursesActivity", intent);
	}
	
	public void startCourseSectionActivity(ArrayList<HashMap<String, Object>> data, String courseId, String instrName) {
		Bundle args = new Bundle();
		args.putSerializable("data", data);
		Intent intent = new Intent().setClass(this, CourseSectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(args);
		intent.putExtra("title", courseId +" "+ instrName);	
		showContent("activity.CourseSectionActivity", intent);
	}
	
	public void startDepartmentsActivity() {
		Intent intent = new Intent().setClass(this, DepartmentsActivity.class);
		showContent("activity.DepartmentsActivity", intent);
	}
	
	public void startDisplaySettingsActivity() {
		Intent intent = new Intent().setClass(this, DisplaySettingActivity.class);
		showContent("acitivity.DisplaySettingActivity", intent);
	}
	
	public void startHelpActivity() {
		Intent intent = new Intent().setClass(this, HelpActivity.class);
		showContent("activity.HelpActivity", intent);
	}
	public void startInstructorsActivity() {
		Intent intent = new Intent().setClass(this, InstructorsActivity.class);
		showContent("activity.InstructorsActivity", intent);
	}
	
	public void startRecentActivity() {
		Intent intent = new Intent().setClass(this, RecentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		showContent("activity.RecentActivity", intent);
	}
	
	public void startSearchActivity() {
		Intent intent = new Intent().setClass(this, SearchActivity.class);
		showContent("activity.SearchActivity", intent);
	}
	
	public void startSettingsActivity() {
		Intent intent = new Intent().setClass(this, SettingsActivity.class);
		showContent("activity.SettingsActivity", intent);
	}

	public void startStorageSettingsActivity() {
		Intent intent = new Intent().setClass(this, StorageSettingActivity.class);
		showContent("activity.StorageSettingActivity", intent);
	}
}
