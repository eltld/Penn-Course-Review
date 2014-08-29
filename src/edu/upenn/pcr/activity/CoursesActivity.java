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
import edu.upenn.pcr.controller.CoursesController;

public class CoursesActivity extends HeaderListActivityBase {

	public static final String CODE_ENTRY = "codeEntry";
	public static final String NAME_ENTRY = "nameEntry";
	
	private CoursesController controller;
	
	private List<HashMap<String, Object>> data;
	private SimpleAdapter adapter;	
	
	private String [] from = {CODE_ENTRY, NAME_ENTRY};
	private int  [] to = {R.id.dept_course_entry_code, R.id.dept_course_entry_name};

	private boolean loading = true;
	private boolean error = false;
	
	private View footer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String title = this.getIntent().getExtras().getString("title");
		this.setHeaderText(title);
		
		footer = this.getLayoutInflater().inflate(R.layout.loading_view_layout, null);
		this.getListView().addFooterView(footer, null, false);
		
		data = new ArrayList<HashMap<String, Object>>();
		adapter = new SimpleAdapter(this, data, R.layout.dept_course_entry_layout, from, to);
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

			public void onScrollStateChanged(AbsListView view, int scrollState) {	
			}
		});
		
		String deptId = this.getIntent().getExtras().getString("deptId");
		controller = new CoursesController(this, deptId);
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
