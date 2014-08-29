package edu.upenn.pcr.activitygroup;

import android.os.Bundle;

public class SearchActivityGroup extends ActivityGroupBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startSearchActivity();
	}

	

	@Override
	public void resetContent() {
		super.resetContent();
		startSearchActivity();
	}
	
}
