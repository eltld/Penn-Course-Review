package edu.upenn.pcr.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import edu.upenn.pcr.R;
import edu.upenn.pcr.controller.SectionByCourseController;

public class SectionByCourseActivity extends HeaderListActivityBase {

	public static final String INSTR_NAME_ENTRY = "instrNameEntry";
	public static final String SEMESTER_ENTRY = "semesterEntry";
	public static final String RATING_ENTRY_1 = "ratingEntry1";
	public static final String RATING_ENTRY_2 = "ratingEntry2";
	public static final String RATING_ENTRY_3 = "ratingEntry3";
	public static final String RATING_ENTRY_4 = "ratingEntry4";
	public static final String LABEL_ENTRY_1 = "labelEntry1";
	public static final String LABEL_ENTRY_2 = "labelEntry2";
	public static final String LABEL_ENTRY_3 = "labelEntry3";
	public static final String LABEL_ENTRY_4 = "labelEntry4";
	
	private SectionByCourseController controller;
	
	private String [] from = {INSTR_NAME_ENTRY, SEMESTER_ENTRY, 
							  RATING_ENTRY_1,  RATING_ENTRY_2,  RATING_ENTRY_3,  RATING_ENTRY_4, 
							  LABEL_ENTRY_1, LABEL_ENTRY_2, LABEL_ENTRY_3, LABEL_ENTRY_4
	};
	private int [] to = {R.id.section_course_entry_name, R.id.section_course_entry_semester,
					 R.id.section_course_entry_rating1, R.id.section_course_entry_rating2, R.id.section_course_entry_rating3, R.id.section_course_entry_rating4, 
					 R.id.section_course_entry_label1, R.id.section_course_entry_label2, R.id.section_course_entry_label3, R.id.section_course_entry_label4,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String title = this.getIntent().getExtras().getString("title");
		this.setHeaderText(title);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onInstructorClicked(position);
			}
		});
		
		String instrId = this.getIntent().getExtras().getString("courseId");
		controller = new SectionByCourseController(this, instrId);
		controller.loadInitialPage();
	}
	
	public void refreshList(List<HashMap<String, Object>> data) {
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.section_course_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
	}
}
