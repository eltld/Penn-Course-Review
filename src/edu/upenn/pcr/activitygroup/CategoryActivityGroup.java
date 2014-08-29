package edu.upenn.pcr.activitygroup;

import android.os.Bundle;

public class CategoryActivityGroup extends ActivityGroupBase {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startCategoryActivity();
	}

	@Override
	public void resetContent() {
		super.resetContent();
		startCategoryActivity();
	}
}
