package edu.upenn.pcr.controller;

import android.content.Context;

public abstract class Controller {
	
	private Context context;
	
	public Controller (Context context) {
		this.context = context;
	}
	
	public void onTaskSuccess(int what, int sequenceNumber, Object result) {};
	
	public void onTaskFailure(int what, int sequenceNumber, Throwable error) {};
	
	public void onTaskProgress(int what, int sequenceNumber, Object progress) {};
	
	public Context getContext() {
		return this.context;
	}
	
}
