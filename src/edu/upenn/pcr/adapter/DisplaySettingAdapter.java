package edu.upenn.pcr.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import edu.upenn.pcr.R;

public class DisplaySettingAdapter extends DisableableAdapter {

	public DisplaySettingAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	protected View setViewDisabled(View view) {
		View textTitle = view.findViewById(R.id.display_setting_title);
		textTitle.setEnabled(false);
		View textExplain = view.findViewById(R.id.display_setting_explain);
		textExplain.setEnabled(false);
		View check = view.findViewById(R.id.display_setting_check);
		check.setEnabled(false);
		view.setEnabled(false);
		return view;
	}
	
	@Override
	protected View setViewEnabled(View view) {
		View textTitle = view.findViewById(R.id.display_setting_title);
		textTitle.setEnabled(true);
		View textExplain = view.findViewById(R.id.display_setting_explain);
		textExplain.setEnabled(true);
		View check = view.findViewById(R.id.display_setting_check);
		check.setEnabled(true);
		view.setEnabled(true);
		return view;
	}

}
