package edu.upenn.pcr.activitygroup;

import android.os.Bundle;

public class SettingsActivityGroup extends ActivityGroupBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startSettingsActivity();
	}
	
	@Override
	public void resetContent() {
		super.resetContent();
		startSettingsActivity();
	}
	
}
