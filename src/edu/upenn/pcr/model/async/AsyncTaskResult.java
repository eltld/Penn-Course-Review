package edu.upenn.pcr.model.async;

public class AsyncTaskResult<T> {

	private boolean successful = true;
	private Throwable error = null;
	private T result = null;
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public Throwable getError() {
		return error;
	}
	
	public T getResult() {
		return result;
	}
	
	public void setSuccessResult(T obj) {
		this.successful = true;
		this.result = obj;
	}
	
	public void setFailureError(Throwable t) {
		this.successful = false;
		this.error = t;
	}
	
}
