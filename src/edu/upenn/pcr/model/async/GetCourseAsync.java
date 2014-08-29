package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.CodeDAO;
import edu.upenn.pcr.model.db.dao.CourseCodeDAO;
import edu.upenn.pcr.model.db.dao.CourseDAO;
import edu.upenn.pcr.model.db.dao.InvertedIndexDAO;
import edu.upenn.pcr.model.db.entity.Code;
import edu.upenn.pcr.model.db.entity.Course;
import edu.upenn.pcr.model.db.entity.CourseCode;
import edu.upenn.pcr.model.db.entity.Department;
import edu.upenn.pcr.model.db.entity.InvertedIndex;
import edu.upenn.pcr.model.db.entity.InvertedIndex.ItemType;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;
import edu.upenn.pcr.model.util.JsonUtil;

public class GetCourseAsync extends AsyncTaskBase<List<String>, Integer, Void> {

	private PennCourseReviewDBMgr dbMgr;
	
	public GetCourseAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<Void> doInBackground(List<String>... params) {
		AsyncTaskResult<Void> result = new AsyncTaskResult<Void>();
		if (params.length == 0) {
			result.setSuccessResult(null);
			return result;
		}
		List<String> deptIds = params[0];
		try {
			for (String deptId : deptIds) {
				HttpResponse response = HttpClientUtil.getFromPath("depts/"+deptId);
				Department dept = new Department();
				dept.setId(deptId);
				this.parseCourse(HttpResponseUtil.getInputStream(response), dept);
				this.publishProgress(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setFailureError(e);
		}
		return result;
	}

	private void parseCourse(InputStream in, Department dept) throws Exception {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("result")) {
					this.readCourseResult(reader, dept);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}
	}
	
	private void readCourseResult(JsonReader reader, Department dept) throws Exception {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("coursehistories")) {
				this.readCourseHistories(reader, dept);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	private void readCourseHistories(JsonReader reader, Department dept) throws Exception {
		reader.beginArray();
		dbMgr.beginTransaction();
		while (reader.hasNext()) {
			this.readCourse(reader, dept);
		}
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		reader.endArray();
	}
	
	private void readCourse(JsonReader reader, Department dept) throws Exception {
		reader.beginObject();
		CourseDAO courseDAO = dbMgr.getCourseDAO();
		CourseCodeDAO courseCodeDAO = dbMgr.getCourseCodeDAO();
		CodeDAO codeDAO = dbMgr.getCodeDAO();
		
		Course course = new Course();
		List<Code> codes = new ArrayList<Code>();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("aliases")) {
				codes = this.readCourseCode(reader, dept);
			} else if (name.equals("id")) {
				course.setId(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("name")) {
				course.setName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		course.setViewed(0);
		courseDAO.add(course);
		
		for (Code code : codes) {	
			codeDAO.add(code);
			
			CourseCode cc = new CourseCode();
			cc.setCode(code);
			cc.setCourse(course);
			courseCodeDAO.add(cc);
			this.buildIndexForCourseCode(cc);
		}
	}
	
	private List<Code> readCourseCode(JsonReader reader, Department dept) throws Exception {
		reader.beginArray();
		List<Code> result = new ArrayList<Code>();
		while (reader.hasNext()) {
			String name = reader.nextString().trim().toUpperCase();
			String [] parts = name.split("-");
			if (parts.length != 0 && parts[0].equals(dept.getId())) {
				Code code = new Code();
				code.setCode(name);
				code.setDepartment(dept);
				result.add(code);
			}
		}
		reader.endArray();
		return result;
	}
	
	private void buildIndexForCourseCode(CourseCode cc) {
		InvertedIndexDAO iiDAO = dbMgr.getInvertedIndexDAO();
		InvertedIndex ii = new InvertedIndex();
		ii.setKeyword(cc.getCode().getCode());
		ii.setFullText(cc.getCode().getCode() + "  " + cc.getCourse().getName());
		ii.setType(ItemType.COURSE);
		ii.setRefId(cc.getCourse().getId());
		iiDAO.add(ii);
		
		ii = new InvertedIndex();
		ii.setKeyword(cc.getCode().getCode().replace("-", " "));
		ii.setFullText(cc.getCode().getCode() + "  " + cc.getCourse().getName());
		ii.setType(ItemType.COURSE);
		ii.setRefId(cc.getCourse().getId());
		iiDAO.add(ii);
		
		ii = new InvertedIndex();
		ii.setKeyword(cc.getCourse().getName());
		ii.setFullText(cc.getCode().getCode() + "  " + cc.getCourse().getName());
		ii.setType(ItemType.COURSE);
		ii.setRefId(cc.getCourse().getId());
		iiDAO.add(ii);
	}
}
