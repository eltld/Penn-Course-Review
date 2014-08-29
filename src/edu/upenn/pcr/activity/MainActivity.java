package edu.upenn.pcr.activity;

import java.util.List;
import java.util.Map;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.MainController;

public class MainActivity extends TabActivity {

	private TabHost tabHost;
	private TabWidget tabWidget;
	private MainController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		controller = new MainController(this);
		tabHost = getTabHost();
		tabWidget = getTabWidget();
		List<? extends Map<String, Object>> tabs = controller.loadTabs();
		for (int i = 0; i < tabs.size(); i++) {
			tabHost.addTab(tabSpec(tabs.get(i)));
		}
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			tabWidget.getChildAt(i).setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					int index = findTabByView(view);
					if (index != -1) {
						tabHost.setCurrentTab(index);
						controller.onTabClicked(index);
					}
				}
			});
		}
		
		tabHost.setCurrentTab(0);
	}
	
	private TabSpec tabSpec(Map<String, Object> map) {
		String id = map.get("id").toString();
		String label = map.get("label").toString();
		Drawable icon = this.getResources().getDrawable((Integer) map.get("icon"));
		Class<?> clazz = (Class<?>) map.get("class");
		Intent intent = new Intent().setClass(this, clazz).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return tabHost.newTabSpec(id).setIndicator(label, icon).setContent(intent);
	}
	
	private int findTabByView(View view) {
		int index = -1;
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			if (tabWidget.getChildAt(i).equals(view)) {
				index = i;
			}
		}
		return index;
	}
	
	public void clearFocus() {
		tabHost.clearFocus();
	}
}
