package edu.upenn.pcr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {

	private static SharedPreferences preferences;
	private static Map<String, ?> data;
	private static List<RatingConfig> ratingConfigs;
	
	private static final String FIRST_TIME = "edu.upenn.pcr.firstTime";
	private static final String SERIAL_NUMBER = "edu.upenn.pcr.serialNumber";
	
	public static void load(Context context) {
		preferences = context.getSharedPreferences(context.getPackageName(), 0);
		data = preferences.getAll();
		AssetReader reader = new AssetReader(context);
		ratingConfigs = reader.readRatingsText();
		reader.close();
		for (RatingConfig config : ratingConfigs) {
			config.setChecked(getBoolean(config.getKey(), false));
		}
	}
	
	public static void reset() {
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		data = preferences.getAll();
		for (RatingConfig config : ratingConfigs) {
			config.setChecked(getBoolean(config.getKey(), false));
		}
	}
	
	public static void setString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
		data = preferences.getAll();
	}
	
	public static void setBoolean(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
		data = preferences.getAll();
		for (RatingConfig config : ratingConfigs) {
			config.setChecked(getBoolean(config.getKey(), false));
		}
	}
	
	public static String getString(String key) {
		return (String) data.get(key);
	}
	
	public static boolean getBoolean(String key, boolean def) {
		Object obj = data.get(key);
		if (obj == null) {
			return def;
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			return def;
		}
	}
	
	public static List<RatingConfig> getRatingConfigById() {
		return ratingConfigs;
	}
	
	public static List<RatingConfig> getRatingConfigsByPriority() {
		List<RatingConfig> result = new ArrayList<RatingConfig>();
		for (int i = 0; i < ratingConfigs.size(); i++) {
			result.add(null);
		}
		for (int i = 0; i < ratingConfigs.size(); i++) {
			RatingConfig rating = ratingConfigs.get(i);
			result.set(rating.getPriority(), rating);
		}
		return result;
	}
	
	public static RatingConfig findRatingConfigByKey(String key) {
		for (RatingConfig config : ratingConfigs) {
			if (config.getKey().equals(key)) {
				return config;
			}
		}
		return null;
	}
	
	public static RatingConfig findRatingConfigById(int id) {
		return ratingConfigs.get(id);
	}
	
	public static void updateRatingConfig(RatingConfig config) {
		setBoolean(config.getKey(), config.isChecked());
	}
	
	public static boolean isFirstTimeLogin() {
		boolean isFirstTime = getBoolean(FIRST_TIME, true);
		return isFirstTime;
	}
	
	public static void loginForFirstTime() {
		setBoolean(FIRST_TIME, false);
	}
	
	public static void setSerialNumber(String serial) {
		setString(SERIAL_NUMBER, serial);
	}
	
	public static String getSerialNumber() {
		return getString(SERIAL_NUMBER);
	}
}
