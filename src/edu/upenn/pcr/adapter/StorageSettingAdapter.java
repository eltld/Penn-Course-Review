package edu.upenn.pcr.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import edu.upenn.pcr.R;

public class StorageSettingAdapter extends DisableableAdapter {

	public StorageSettingAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	protected View setViewDisabled(View view) {
		View title = view.findViewById(R.id.storage_setting_title);
		title.setEnabled(false);
		View explain = view.findViewById(R.id.storage_setting_explain);
		explain.setEnabled(false);
		view.setEnabled(false);
		return view;
	}

	@Override
	protected View setViewEnabled(View view) {
		View title = view.findViewById(R.id.storage_setting_title);
		title.setEnabled(true);
		View explain = view.findViewById(R.id.storage_setting_explain);
		explain.setEnabled(true);
		view.setEnabled(true);
		return view;
	}

}
