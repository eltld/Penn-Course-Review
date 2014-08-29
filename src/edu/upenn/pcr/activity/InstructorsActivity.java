package edu.upenn.pcr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.InstrsController;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class InstructorsActivity extends ListActivityBase {

	public static final String NAME_ENTRY = "nameEntry";
	
	private InstrsController controller;
	
	private List<HashMap<String, Object>> data;
	private SimpleAdapter adapter;
	private String [] from = {NAME_ENTRY};
	private int [] to = {R.id.instructors_entry_text};
	
	private View footer;
	
	boolean loading = true;
	boolean error = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = new InstrsController(this);
		
		footer = this.getLayoutInflater().inflate(R.layout.loading_view_layout, null);
		this.getListView().addFooterView(footer);
		
		data = new ArrayList<HashMap<String, Object>>();
		adapter = new SimpleAdapter(this, data, R.layout.view_instructors_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onListItemClicked(position);
			}
		});
		
		this.getListView().setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (loading || error) {
					return;
				}
				if ((firstVisibleItem + visibleItemCount >= totalItemCount) && controller.hasNext()) {
					controller.onNextPageClicked();
					setLoading();
				} 
			}
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
		});
		controller.loadInitialPage();
	}

	public void nextPage(List<HashMap<String, Object>> list) {
		data.addAll(list);
		adapter.notifyDataSetChanged();
		clearLoading();
	}
	
	private void setLoading() {
		loading = true;
		this.getListView().addFooterView(footer);
	}
	
	private void clearLoading() {
		loading = false;
		this.getListView().removeFooterView(footer);
	}
	
	@Override
	public void error(String message) {
		super.error(message);
		error = true;
	}
}
