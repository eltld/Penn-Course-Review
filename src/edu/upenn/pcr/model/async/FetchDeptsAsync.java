package edu.upenn.pcr.model.async;

import java.util.List;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.DepartmentDAO;
import edu.upenn.pcr.model.db.entity.Department;

public class FetchDeptsAsync extends AsyncTaskBase<Integer, Void, List<Department>> {

	private PennCourseReviewDBMgr dbMgr;
	
	public FetchDeptsAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<List<Department>> doInBackground(Integer... arg0) {
		AsyncTaskResult<List<Department>> result = new AsyncTaskResult<List<Department>>();
		if (arg0.length < 2) {
			result.setFailureError(null);
		} else {
			DepartmentDAO deptDAO = dbMgr.getDepartmentDAO();
			result.setSuccessResult(deptDAO.findAll(arg0[0], arg0[1]));
		}
		return result;
	}

}
