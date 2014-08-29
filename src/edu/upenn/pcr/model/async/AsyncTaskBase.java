package edu.upenn.pcr.model.async;

import edu.upenn.pcr.controller.Controller;
import android.os.AsyncTask;

public abstract class AsyncTaskBase<Params, Progress, Result> extends AsyncTask<Params, Progress, AsyncTaskResult<Result>> {

	private static int sequenceCounter = 0;
	private Controller controller;
	private Integer what;
	private Integer sequenceNumber;
	
	public AsyncTaskBase(Integer what, Controller controller) {
		this.controller = controller;
		this.what = what;
		this.sequenceNumber = nextSequenceNumber();
	}
	
	private static synchronized int nextSequenceNumber() {
		sequenceCounter++;
		return sequenceCounter;
	}

	@Override
	protected void onPostExecute(AsyncTaskResult<Result> result) {
		super.onPostExecute(result);
		if (controller == null) {
			return;
		}
		if (result.isSuccessful()) {
			controller.onTaskSuccess(what, sequenceNumber, result.getResult());
		} else {
			controller.onTaskFailure(what, sequenceNumber, result.getError());
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		if (controller == null) {
			return;
		}
		if (values.length != 0) {
			controller.onTaskProgress(what, sequenceNumber, values[0]);
		}
	}
	
	public Controller getController() {
		return this.controller;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void test(Params... args) {
		this.doInBackground(args);
	}
}
