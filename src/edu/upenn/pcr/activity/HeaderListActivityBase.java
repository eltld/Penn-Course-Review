package edu.upenn.pcr.activity;

import edu.upenn.pcr.R;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class HeaderListActivityBase extends ActivityBase {

	private ListView listView;
	private TextView header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.header_list_layout);
		header = (TextView) this.findViewById(R.id.header_list_header);
		listView = (ListView) this.findViewById(R.id.header_list_body);
	}
	
	public ListView getListView() {
		return this.listView;
	}
	
	public void setHeaderText(String title) {
		header.setText(title);
	}
}
