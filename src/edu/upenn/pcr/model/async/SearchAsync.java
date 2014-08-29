package edu.upenn.pcr.model.async;

import java.util.List;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.InvertedIndexDAO;
import edu.upenn.pcr.model.db.entity.InvertedIndex;

public class SearchAsync extends AsyncTaskBase<Object, Void, List<InvertedIndex>> {

	private PennCourseReviewDBMgr dbMgr;
	
	public SearchAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<List<InvertedIndex>> doInBackground(Object... args) {
		AsyncTaskResult<List<InvertedIndex>> result = new AsyncTaskResult<List<InvertedIndex>>();
		if (args.length < 3) {
			result.setFailureError(null);
		} else {
			InvertedIndexDAO iiDAO = dbMgr.getInvertedIndexDAO();
			String term;
			int begin;
			int count;
			try {
				term = args[0].toString();
				begin = (Integer) args[1];
				count = (Integer) args[2];
				result.setSuccessResult(iiDAO.matchAll(term, begin, count));
			} catch (Exception e) {
				result.setFailureError(e);
			}
		}
		return result;
	}
}
