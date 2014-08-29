package edu.upenn.pcr.controller;

import java.util.List;

import android.content.Intent;
import edu.upenn.pcr.activity.LoginActivity;
import edu.upenn.pcr.activity.MainActivity;
import edu.upenn.pcr.model.async.AuthenticateAsync;
import edu.upenn.pcr.util.PreferenceUtil;
import edu.upenn.pcr.util.RatingConfig;

public class LoginController extends Controller {

	private static final int LOGIN = 1;
	
	private LoginActivity loginActivity;
	
	public LoginController(LoginActivity activity) {
		super(activity);
		this.loginActivity = activity;
		PreferenceUtil.load(loginActivity);
	}
	
	public void initCheck() {
		if (PreferenceUtil.isFirstTimeLogin()) {
			this.setRatingPreference();
			PreferenceUtil.loginForFirstTime();
		}
	}
	
	public String getStoredSerialNumber() {
		return PreferenceUtil.getSerialNumber();
	}
	
	private void setRatingPreference() {
		List<RatingConfig> configs = PreferenceUtil.getRatingConfigsByPriority();
		for (int i = 0; i < 4; i++) {
			RatingConfig config = configs.get(i);
			config.setChecked(true);
			PreferenceUtil.updateRatingConfig(config);
		}
	}
	
	public void onLogin(String serial) {
		loginActivity.showLoadingDialog();
		PreferenceUtil.setSerialNumber(serial);
		AuthenticateAsync task = new AuthenticateAsync(LOGIN, this);
		task.execute(serial);
	}
	
	@Override
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {
		if (what != LOGIN) {
			return;
		}
		loginActivity.dismissLoadingDialog();
		Intent intent = new Intent();
		intent.setClass(loginActivity, MainActivity.class);
		loginActivity.startActivity(intent);
	}

	@Override
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {
		loginActivity.dismissLoadingDialog();
		loginActivity.error("Incorrect serial number!");
	}

}
