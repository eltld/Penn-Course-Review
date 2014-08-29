package edu.upenn.pcr.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public abstract class DisableableAdapter extends SimpleAdapter {
	
	public DisableableAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}
	
	@Override
	public boolean isEnabled(int position) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		Object obj = map.get("isEnabled");
		if (obj == null) {
			return true;
		}
		if (!(obj instanceof Boolean)) {
			return true;
		}
		return (Boolean) obj;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = super.getView(position, convertView, parent);
		if (isEnabled(position)) {
			result = setViewEnabled(result);
		} else {
			result = setViewDisabled(result);
		}
		return result;
	}
	
	protected abstract View setViewDisabled(View view);
	
	protected abstract View setViewEnabled(View view);

}
