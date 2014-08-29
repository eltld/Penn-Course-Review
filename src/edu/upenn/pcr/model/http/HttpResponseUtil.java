package edu.upenn.pcr.model.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

public class HttpResponseUtil {

	public static InputStream getInputStream(HttpResponse response) throws IllegalStateException, IOException {
		return response.getEntity().getContent();
	}
	
	public static String getContent(HttpResponse response) throws ParseException, IOException {
		return EntityUtils.toString(response.getEntity());
	}
	
	public static long getContentLength(HttpResponse response) {
		return response.getEntity().getContentLength();
	}
}
