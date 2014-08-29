package edu.upenn.pcr.model.async;

import java.util.List;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.RecentDAO;
import edu.upenn.pcr.model.db.entity.Recent;

public class LoadRecentAsync extends AsyncTaskBase<Object, Void, List<Recent>> {

	private PennCourseReviewDBMgr dbMgr;
	
	public LoadRecentAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<List<Recent>> doInBackground(Object... args) {
		AsyncTaskResult<List<Recent>> result = new AsyncTaskResult<List<Recent>>();
		if (args.length < 2) {
			result.setFailureError(null);
		} else {
			try {
				RecentDAO dao = dbMgr.getRecentDAO();
				int begin = (Integer) args[0];
				int count = (Integer) args[1];
				result.setSuccessResult(dao.findAll(begin, count));
			} catch (Exception e) {
				result.setFailureError(null);
			}
		}
		return result;
	}

}
