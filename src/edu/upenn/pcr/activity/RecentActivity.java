package edu.upenn.pcr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.RecentController;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class RecentActivity extends ListActivityBase {

	public static final String TEXT_ENTRY = "textEntry";
	public static final String IMAGE_ENTRY = "imageEntry";
	
	private RecentController controller;
	
	private String [] from = {TEXT_ENTRY, IMAGE_ENTRY};
	private int [] to = {R.id.search_entry_text, R.id.search_entry_icon};
	
	private List<HashMap<String, Object>> emptyData;
	private SimpleAdapter emptyAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new RecentController(this);
		
		emptyData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> no = new HashMap<String, Object>();
		no.put(TEXT_ENTRY, "No results");
		emptyData.add(no);
		emptyAdapter = new SimpleAdapter(this, emptyData, R.layout.search_entry_layout, from, to);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onListItemClicked(position);
			}
		});
		controller.loadData();
	}

	public void refreshList(List<HashMap<String, Object>> data) {
		if (data.size() == 0) {
			System.out.println("EMPTY");
			this.getListView().setAdapter(emptyAdapter);
			this.getListView().setEnabled(true);
		} else {
			System.out.println("HAHAHAHAH");
			SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.search_entry_layout, from, to);
			this.getListView().setAdapter(adapter);
			this.getListView().setEnabled(true);
		}
	}
}
