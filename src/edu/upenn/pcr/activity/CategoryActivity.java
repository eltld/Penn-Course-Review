package edu.upenn.pcr.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.CategoryController;

public class CategoryActivity extends ListActivityBase {
	
	public static final String IMAGE_ENTRY = "imageEntry";
	public static final String TEXT_ENTRY = "textEntry";
	
	private CategoryController controller;
	
	private SimpleAdapter adapter;
	
	private String [] from = {IMAGE_ENTRY, TEXT_ENTRY};
	private int [] to = {R.id.catetgory_entry_image, R.id.category_entry_text};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = new CategoryController(this);
		List<HashMap<String, Object>> data = controller.loadData();
		
		adapter = new SimpleAdapter(this, data, R.layout.category_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){	
				controller.onListItemClicked(position);
			}
		});
	}
}
