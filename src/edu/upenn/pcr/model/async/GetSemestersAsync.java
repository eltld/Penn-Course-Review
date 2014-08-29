package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.SemesterDAO;
import edu.upenn.pcr.model.db.entity.Semester;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;
import edu.upenn.pcr.model.util.JsonUtil;

public class GetSemestersAsync extends AsyncTaskBase<Void, Void, Void> {

	private PennCourseReviewDBMgr dbMgr;
	
	public GetSemestersAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}
	
	@Override
	protected AsyncTaskResult<Void> doInBackground(Void... params) {
		AsyncTaskResult<Void> result = new AsyncTaskResult<Void>();
		try {
			HttpResponse response = HttpClientUtil.getFromPath("/semesters");
			this.parseSemesters(HttpResponseUtil.getInputStream(response));
			result.setSuccessResult(null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setFailureError(e);
		}
		return result;
	}
	
	private void parseSemesters(InputStream in) throws Exception {
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("result")) {
					this.readSemesterResult(reader);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}
	}
	
	private void readSemesterResult(JsonReader reader) throws Exception {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("values")) {
				this.readSemesterValues(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	private void readSemesterValues(JsonReader reader) throws Exception {
		reader.beginArray();
		SemesterDAO semesterDAO = dbMgr.getSemesterDAO();
		dbMgr.beginTransaction();
		while (reader.hasNext()) {
			Semester s = this.readSemester(reader);
			semesterDAO.add(s);
		}
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		reader.endArray();
	}
	
	private Semester readSemester(JsonReader reader) throws Exception {
		reader.beginObject();
		Semester s = new Semester();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				s.setId(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("name")) {
				s.setName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("year")) {
				s.setYear(JsonUtil.readNullOrInteger(reader));
			} else if (name.equals("seasoncode")) {
				s.setSeason(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return s;
	}
}
