package edu.upenn.pcr.activity;

import edu.upenn.pcr.activitygroup.ActivityGroupBase;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class ActivityBase extends Activity {
	
	private ProgressDialog loadingDialog = null;
	
	@Override
	public void onBackPressed() {
		Activity parent = getParent();
		if (parent == null) {
			super.onBackPressed();
		} else if (parent instanceof ActivityGroupBase) {
			ActivityGroupBase activityGroup = (ActivityGroupBase) parent;
			activityGroup.onBackPressed();
		} else {
			super.onBackPressed();
		}
	}
	
	public void showLoadingDialog() {
		if (loadingDialog == null) {
			if (this.getParent() == null) {
				loadingDialog = new ProgressDialog(this);
			} else {
				loadingDialog = new ProgressDialog(this.getParent());
			}
			loadingDialog.setMessage("Loading...");
			loadingDialog.requestWindowFeature(ProgressDialog.STYLE_SPINNER);
			loadingDialog.setIndeterminate(true);
		}
		loadingDialog.show();
	}
	
	public void dismissLoadingDialog() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
		}
	}
	
	public void error(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
}
