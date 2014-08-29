package edu.upenn.pcr.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.upenn.pcr.R;
import edu.upenn.pcr.adapter.DisplaySettingAdapter;
import edu.upenn.pcr.controller.DisplaySettingController;

public class DisplaySettingActivity extends ListActivityBase {

	public static final String TITLE_ENTRY = "titleEntry";
	public static final String EXPLAIN_ENTRY = "explainEntry";
	public static final String CHECK_ENTRY = "checkEntry";
	
	private DisplaySettingController controller;
	
	private DisplaySettingAdapter adapter;
	private String [] from = {"titleEntry", "explainEntry", "checkEntry"};
	private int [] to = {R.id.display_setting_title, R.id.display_setting_explain, R.id.display_setting_check};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = new DisplaySettingController(this);
		
		List<? extends Map<String, Object>> data = controller.loadData();
		adapter = new DisplaySettingAdapter(this, data, R.layout.display_setting_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {	
				controller.onListItemClicked(position);
			}
		});
	}
	
	public void refreshList() {
		adapter.notifyDataSetChanged();
	}
}
