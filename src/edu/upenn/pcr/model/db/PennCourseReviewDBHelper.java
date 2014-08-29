package edu.upenn.pcr.model.db;

import java.io.File;
import java.util.ArrayList;

import edu.upenn.pcr.util.AssetReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PennCourseReviewDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "PennCourseReview";
	
	private static PennCourseReviewDBHelper instance = null;
	private Context context;
	
	private PennCourseReviewDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		AssetReader reader = new AssetReader(context);
		ArrayList<String> commands = reader.readDBSchema();
		reader.close();
		for (int i = 0; i < commands.size(); i++) {
			db.execSQL(commands.get(i));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);
	}
	
	public static PennCourseReviewDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new PennCourseReviewDBHelper(context);
		}
		return instance;
	}
	
	public long getDatabaseSize(Context context) {
		File f = context.getDatabasePath(DB_NAME);
		return f.length();
	}
	
	public void clearDatabase(SQLiteDatabase db) {
		onCreate(db);
	}
}
