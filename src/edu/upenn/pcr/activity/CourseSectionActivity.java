package edu.upenn.pcr.activity;

import java.util.HashMap;
import java.util.List;

import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.CourseSectionController;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class CourseSectionActivity extends HeaderListActivityBase {

	public static final String SEMESTER_ENTRY = "semesterEntry";
	public static final String RATING_ENTRY_1 = "ratingEntry1";
	public static final String RATING_ENTRY_2 = "ratingEntry2";
	public static final String RATING_ENTRY_3 = "ratingEntry3";
	public static final String RATING_ENTRY_4 = "ratingEntry4";
	public static final String LABEL_ENTRY_1 = "labelEntry1";
	public static final String LABEL_ENTRY_2 = "labelEntry2";
	public static final String LABEL_ENTRY_3 = "labelEntry3";
	public static final String LABEL_ENTRY_4 = "labelEntry4";
	
	private CourseSectionController controller;
	
	private String [] from = {SEMESTER_ENTRY, 
							  RATING_ENTRY_1, RATING_ENTRY_2, RATING_ENTRY_3, RATING_ENTRY_4, 
							  LABEL_ENTRY_1, LABEL_ENTRY_2, LABEL_ENTRY_3, LABEL_ENTRY_4
	};
	private int [] to = {R.id.course_section_entry_semester, 
						 R.id.course_section_entry_rating1, R.id.course_section_entry_rating2, R.id.course_section_entry_rating3, R.id.course_section_entry_rating4, 
						 R.id.course_section_entry_label1, R.id.course_section_entry_label2, R.id.course_section_entry_label3, R.id.course_section_entry_label4
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = this.getIntent().getExtras().getString("title");
		this.setHeaderText(title);
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) this.getIntent().getExtras().get("data");
		controller = new CourseSectionController(this, data);
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.course_section_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onCourseSectionClicked(position);
			}
		});
	}
	
}
