package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.InstructorDAO;
import edu.upenn.pcr.model.db.entity.Instructor;
import java.util.List;

public class FetchInstrsAsync extends AsyncTaskBase<Integer, Void, List<Instructor>> {

	private PennCourseReviewDBMgr dbMgr;
	
	public FetchInstrsAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}
	
	@Override
	protected AsyncTaskResult<List<Instructor>> doInBackground(Integer... params) {
        AsyncTaskResult<List<Instructor>> result = new AsyncTaskResult<List<Instructor>>();
        if(params.length < 2){
        	result.setFailureError(null);
        }else{
        	InstructorDAO instrDAO = dbMgr.getInstructorDAO();
        	result.setSuccessResult(instrDAO.findAll(params[0],params[1]));
        }
		return result;
	}

}
