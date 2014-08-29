package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.CourseCodeDAO;

public class FetchCoursesCountAsync extends AsyncTaskBase<String, Void, Integer> {

	private PennCourseReviewDBMgr dbMgr;
	
	public FetchCoursesCountAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<Integer> doInBackground(String... params) {
		AsyncTaskResult<Integer> result = new AsyncTaskResult<Integer>();
		CourseCodeDAO ccDAO = dbMgr.getCourseCodeDAO();
		result.setSuccessResult(ccDAO.countByDepartment(params[0]));
		return result;
	}


}
