package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.DepartmentDAO;

public class FetchDeptsCountAsync extends AsyncTaskBase<Void, Void, Integer> {

	private PennCourseReviewDBMgr dbMgr;
	
	public FetchDeptsCountAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<Integer> doInBackground(Void... params) {
		AsyncTaskResult<Integer> result = new AsyncTaskResult<Integer>();
		DepartmentDAO deptDAO = dbMgr.getDepartmentDAO();
		result.setSuccessResult(deptDAO.count());
		return result;
	}

}
