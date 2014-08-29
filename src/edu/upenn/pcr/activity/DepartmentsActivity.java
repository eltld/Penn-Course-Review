package edu.upenn.pcr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.DeptsController;

public class DepartmentsActivity extends ListActivityBase {

	public static final String ID_ENTRY = "idEntry";
	public static final String NAME_ENTRY = "nameEntry";
	
	private DeptsController controller;
	
	private List<HashMap<String, Object>> data;
	private SimpleAdapter adapter;
	private String [] from = {"idEntry", "nameEntry"};
	private int [] to = {R.id.dept_entry_id, R.id.dept_entry_name};
	
	private boolean loading = true;
	private boolean error = false;
	
	private View footer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new DeptsController(this);
		
		footer = this.getLayoutInflater().inflate(R.layout.loading_view_layout, null);
		this.getListView().addFooterView(footer, null, false);
		
		data = new ArrayList<HashMap<String, Object>>();
		adapter = new SimpleAdapter(this, data, R.layout.view_departments_entry_layout, from, to);
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
					setLoading();
					controller.onNextPageClicked();
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
	
	@Override
	public void error(String message) {
		super.error(message);
		error = true;
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
}
