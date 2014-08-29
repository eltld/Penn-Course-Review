package edu.upenn.pcr.activity;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.upenn.pcr.R;
import edu.upenn.pcr.adapter.StorageSettingAdapter;
import edu.upenn.pcr.controller.StorageSettingController;

public class StorageSettingActivity extends ListActivityBase {

	private StorageSettingController controller;
	
	private StorageSettingAdapter adapter;
	private String [] from = {"titleEntry", "explainEntry"};
	private int [] to = {R.id.storage_setting_title, R.id.storage_setting_explain};
	
	private ProgressDialog progressDlg = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = new StorageSettingController(this);
		
		List<? extends Map<String, Object>> data = controller.loadData();
		adapter = new StorageSettingAdapter(this, data, R.layout.storage_setting_entry_layout, from, to);
		this.getListView().setAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				controller.onListItemClicked(position);
			}
		});
	}
	
	public void showProgressDialog(String message) {
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this.getParent());
			progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}
		progressDlg.setMessage(message);
		progressDlg.setMax(100);
		progressDlg.setProgress(0);
		progressDlg.show();
	}
	
	public void updateProgressDlg(int percent) {
		progressDlg.setProgress(percent);
	}
	
	public void dismissProgressDialog() {
		if (progressDlg != null) {
			progressDlg.dismiss();
		}
	}
	
	public void refreshList() {
		adapter.notifyDataSetChanged();
	}
}
