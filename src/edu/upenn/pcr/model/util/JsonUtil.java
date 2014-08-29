package edu.upenn.pcr.model.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonUtil {

	public static String readNullOrString(JsonReader reader) throws Exception {
		JsonToken token = reader.peek();
		String result;
		switch (token) {
		case NULL:
			reader.nextNull();
			result = "";
			break;
		default:
			result = reader.nextString();
			break;
		}
		return result;
	}
	
	public static Integer readNullOrInteger(JsonReader reader) throws Exception {
		JsonToken token = reader.peek();
		Integer result;
		switch(token) {
		case NULL:
			reader.nextNull();
			result = Integer.MIN_VALUE;
			break;
		default:
			result = reader.nextInt();
			break;
		}
		return result;
	}
}
