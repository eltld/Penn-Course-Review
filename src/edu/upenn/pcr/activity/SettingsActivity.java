package edu.upenn.pcr.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.SettingsController;

public class SettingsActivity extends ListActivityBase {

	public static final String IMAGE_ENTRY = "imageEntry";
	public static final String TEXT_ENTRY = "textEntry";
	private SettingsController controller;
	
	private String [] from = {IMAGE_ENTRY, TEXT_ENTRY};
	private int [] to = {R.id.settings_entry_image, R.id.settings_entry_text};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = new SettingsController(this);
		List<? extends Map<String, Object>> data = controller.loadData();
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.settings_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				controller.onListItemClicked(position);
			}
		});
	}
}
