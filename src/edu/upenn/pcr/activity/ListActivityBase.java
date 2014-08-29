package edu.upenn.pcr.activity;

import android.os.Bundle;
import android.widget.ListView;
import edu.upenn.pcr.R;

public class ListActivityBase extends ActivityBase {

	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_view_layout);
		listView = (ListView) this.findViewById(R.id.list_view_body);
	}

	public ListView getListView() {
		return listView;
	}
}
