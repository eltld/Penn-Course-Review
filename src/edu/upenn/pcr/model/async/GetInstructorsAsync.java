package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.InstructorDAO;
import edu.upenn.pcr.model.db.dao.InvertedIndexDAO;
import edu.upenn.pcr.model.db.entity.Instructor;
import edu.upenn.pcr.model.db.entity.InvertedIndex;
import edu.upenn.pcr.model.db.entity.InvertedIndex.ItemType;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;
import edu.upenn.pcr.model.util.JsonUtil;

public class GetInstructorsAsync extends AsyncTaskBase<Void, Void, Void> {

	private PennCourseReviewDBMgr dbMgr;
	
	public GetInstructorsAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<Void> doInBackground(Void... arg0) {
		AsyncTaskResult<Void> result = new AsyncTaskResult<Void>();
		try {
			HttpResponse response = HttpClientUtil.getFromPath("/instructors");
			this.parseInstructors(HttpResponseUtil.getInputStream(response));
			result.setSuccessResult(null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setFailureError(e);
		}
		return result;
	}

	private void parseInstructors(InputStream in) throws Exception {
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("result")) {
					this.readInstructorResult(reader);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}
	}
	
	private void readInstructorResult(JsonReader reader) throws Exception {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("values")) {
				this.readInstructorValues(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	private void readInstructorValues(JsonReader reader)  throws Exception {
		reader.beginArray();
		InstructorDAO instrDAO = dbMgr.getInstructorDAO();
		dbMgr.beginTransaction();
		while (reader.hasNext()) {
			Instructor instr = this.readInstructor(reader);
			instr.setViewed(0);
			instrDAO.add(instr);
			this.buildIndex(instr);
		}
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		reader.endArray();
	}
	
	private Instructor readInstructor(JsonReader reader) throws Exception {
		reader.beginObject();
		Instructor instr = new Instructor();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				instr.setId(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("first_name")) {
				instr.setFirstName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("last_name")) {
				instr.setLastName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return instr;
	}
	
	private void buildIndex(Instructor instr) {
		InvertedIndexDAO iiDAO = dbMgr.getInvertedIndexDAO();
		InvertedIndex ii = new InvertedIndex();
		ii.setKeyword(instr.getFirstName());
		ii.setFullText(instr.getLastName() + ", " + instr.getFirstName());
		ii.setType(ItemType.INSTRUCTOR);
		ii.setRefId(instr.getId());
		iiDAO.add(ii);
		
		ii = new InvertedIndex();
		ii.setKeyword(instr.getLastName());
		ii.setFullText(instr.getLastName() + ", " + instr.getFirstName());
		ii.setType(ItemType.INSTRUCTOR);
		ii.setRefId(instr.getId());
		iiDAO.add(ii);
	}
}
