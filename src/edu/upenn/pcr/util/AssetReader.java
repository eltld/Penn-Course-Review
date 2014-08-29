package edu.upenn.pcr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class AssetReader {
	
	private Context context;
	private BufferedReader reader;
	
	public AssetReader(Context context) {
		this.context = context;
	}
	
	private void open(String fileName) throws Exception {
		InputStream in = context.getAssets().open(fileName);
		reader = new BufferedReader(new InputStreamReader(in));
	}
	
	public void close() {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readDBSchema() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			open("schema.sql");
			String input = reader.readLine().trim();
			while (input != null) {
				input = input.trim();
				if (input.length() != 0) {
					result.add(input);
				}
				input = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.clear();
		}
		return result;
	}
	
	public List<RatingConfig> readRatingsText() {
		List<RatingConfig> result = new ArrayList<RatingConfig>();
		try {
			open("ratings.txt");
			String input = reader.readLine().trim();
			while (input != null) {
				input = input.trim();
				if (input.length() != 0) {
					String [] tokens = input.split(":");
					RatingConfig content = new RatingConfig();
					content.setKey(tokens[0]);
					content.setId(Integer.parseInt(tokens[1]));
					content.setPriority(Integer.parseInt(tokens[2]));
					content.setFullName(tokens[3]);
					content.setShortName(tokens[4]);
					content.setTitle(tokens[5]);
					content.setDescription(tokens[6]);
					result.add(content);
				}
				input = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.clear();
		}
		return result;
	}
}
