package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.dao.*;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;

public class FetchInstrsCountAsync extends AsyncTaskBase<Void, Void, Integer> {
	
	private PennCourseReviewDBMgr dbMgr;
	
	public FetchInstrsCountAsync(Integer what, Controller controller){
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}


	@Override
	protected AsyncTaskResult<Integer> doInBackground(Void... params) {
		AsyncTaskResult<Integer> result = new AsyncTaskResult<Integer>();
		InstructorDAO instrDAO = dbMgr.getInstructorDAO();
		result.setSuccessResult(instrDAO.count());
		return result;
	}

}
