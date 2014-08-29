package edu.upenn.pcr.model.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientUtil {

	private static HttpClient httpClient;
	private static final String BASE_URL = "http://api.penncoursereview.com/v1/";					
	private static final String API_KEY = "?token=573-2_4ykZdNLiHFfc9rehZ9c8fCNg";
	
	static {
		 HttpParams params = new BasicHttpParams();
	     ConnManagerParams.setMaxTotalConnections(params, 2);
	     HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
	     SchemeRegistry schemeRegistry = new SchemeRegistry();
	     schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	     schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	     ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
	     httpClient = new DefaultHttpClient(cm, params);
	}
	
	public static HttpResponse getFromPath(String path) throws Exception {
		String url = BASE_URL + path + API_KEY;
		HttpUriRequest request = new HttpGet(url);
		return httpClient.execute(request);
	}
	
	public static HttpResponse getFromAbsoluteUrl(String path) throws Exception {
		String url = path;
		HttpUriRequest request = new HttpGet(url);
		return httpClient.execute(request);
	}
}
