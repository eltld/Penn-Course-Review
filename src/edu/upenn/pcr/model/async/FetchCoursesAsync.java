package edu.upenn.pcr.model.async;

import java.util.List;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.CourseCodeDAO;
import edu.upenn.pcr.model.db.entity.CourseCode;

public class FetchCoursesAsync extends AsyncTaskBase<Object, Void, List<CourseCode>> {

	private PennCourseReviewDBMgr dbMgr;
	
	public FetchCoursesAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<List<CourseCode>> doInBackground(Object... params) {
		AsyncTaskResult<List<CourseCode>> result = new AsyncTaskResult<List<CourseCode>>();
		
		if(params.length < 3) {
			result.setFailureError(null);
		}else {
			CourseCodeDAO ccDAO = dbMgr.getCourseCodeDAO();
			result.setSuccessResult(ccDAO.findByDepartment((String)params[0], (Integer)params[1], (Integer)params[2]));
		}
		return result;
	}

}
