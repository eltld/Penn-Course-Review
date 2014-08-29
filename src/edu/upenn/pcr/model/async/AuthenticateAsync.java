package edu.upenn.pcr.model.async;

import org.apache.http.HttpResponse;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;

public class AuthenticateAsync extends AsyncTaskBase<String, Void, Boolean> {
	
	public AuthenticateAsync(Integer what, Controller controller) {
		super(what, controller);
	}
	
	@Override
	protected AsyncTaskResult<Boolean> doInBackground(String... params) {
		
		AsyncTaskResult<Boolean> result = new AsyncTaskResult<Boolean>();
		String url = "http://www.penncoursereview.com/androidapp/auth?serial=" + params[0];
		try {
			HttpResponse response = HttpClientUtil.getFromAbsoluteUrl(url);
			String content = HttpResponseUtil.getContent(response);
			if (content.equals("!INVALID")) {
				result.setFailureError(null);
			} else {
				result.setSuccessResult(true);
			}
		} catch (Exception e) {
			result.setFailureError(e);
		}
		return result;
	}

}
