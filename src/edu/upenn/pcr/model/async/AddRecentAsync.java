package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.entity.Recent;

public class AddRecentAsync extends AsyncTaskBase<Recent, Void, Void> {

	private PennCourseReviewDBMgr dbMgr;
	
	public AddRecentAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<Void> doInBackground(Recent... arg) {
		AsyncTaskResult<Void> result = new AsyncTaskResult<Void>();
		if (arg.length < 1) {
			result.setFailureError(null);
		} else {
			dbMgr.getRecentDAO().add(arg[0]);
			result.setSuccessResult(null);
		}
		return result;
	}

}
