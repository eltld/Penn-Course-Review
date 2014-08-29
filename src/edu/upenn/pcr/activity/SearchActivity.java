package edu.upenn.pcr.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.SearchController;

public class SearchActivity extends ActivityBase {

	public static final String TEXT_ENTRY = "textEntry";
	public static final String IMAGE_ENTRY = "imageEntry";
	
	private SearchController controller;
	
	private List<HashMap<String, Object>> empty = new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter emptyAdapter;
	
	private String [] from = {TEXT_ENTRY, IMAGE_ENTRY};
	private int [] to = {R.id.search_entry_text, R.id.search_entry_icon};
	
	private TextView searchTextView;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search_layout);
		listView = (ListView) this.findViewById(R.id.search_body);
		emptyAdapter = new SimpleAdapter(this, empty, R.layout.search_entry_layout, from, to);
		listView.setAdapter(emptyAdapter);
		controller = new SearchController(this);
		Button submitButton = (Button) this.findViewById(R.id.search_submit_button);
		submitButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				controller.onSubmitButtonClicked();
			}
		});
		searchTextView = (TextView) this.findViewById(R.id.search_text);
		searchTextView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
				String term = searchTextView.getText().toString().trim().toUpperCase();
				if (term.equals("")) {
					listView.setAdapter(emptyAdapter);
					return;
				}
				controller.search(term);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onListItemClicked(position);
			}
		});
	}
	
	public void refreshList(List<HashMap<String, Object>> data) {
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.search_entry_layout, from, to);
		listView.setAdapter(adapter);
	}
}
