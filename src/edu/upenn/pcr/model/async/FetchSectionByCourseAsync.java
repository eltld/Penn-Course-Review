package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.CourseDAO;
import edu.upenn.pcr.model.db.dao.CourseDetailDAO;
import edu.upenn.pcr.model.db.dao.CourseSectionDAO;
import edu.upenn.pcr.model.db.entity.Course;
import edu.upenn.pcr.model.db.entity.CourseDetail;
import edu.upenn.pcr.model.db.entity.CourseSection;
import edu.upenn.pcr.model.db.entity.Instructor;
import edu.upenn.pcr.model.db.entity.Semester;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;
import edu.upenn.pcr.model.util.JsonUtil;
import edu.upenn.pcr.util.PreferenceUtil;
import edu.upenn.pcr.util.RatingConfig;

/*
 * view-source:http://api.penncoursereview.com/v1/coursehistories/265/reviews?token=573-2_4ykZdNLiHFfc9rehZ9c8fCNg
 * 
 * view-source:http://api.penncoursereview.com/v1/courses/6304/?token=573-2_4ykZdNLiHFfc9rehZ9c8fCNg
 * 
 */

public class FetchSectionByCourseAsync extends AsyncTaskBase<String, Void, List<CourseSection>> {

	private PennCourseReviewDBMgr dbMgr;
	private String courseID;
	
	public FetchSectionByCourseAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}

	@Override
	protected AsyncTaskResult<List<CourseSection>> doInBackground(String... params) {
		AsyncTaskResult<List<CourseSection>> result = new AsyncTaskResult<List<CourseSection>>();
		if(params.length == 0) {
			result.setFailureError(null);
			return result;
		}
		CourseDAO cDAO = dbMgr.getCourseDAO();
		courseID = params[0];
		Course seleCourse = cDAO.findById(courseID);
		Integer viewed = seleCourse.getViewed();
		CourseSectionDAO csDAO = dbMgr.getCourseSectionDAO();
		
		if(viewed == 1) {
			result.setSuccessResult(csDAO.findByCourseId(courseID));
			return result;
		}else{
			try {
				HttpResponse response = HttpClientUtil.getFromPath("/coursehistories/" + courseID + "/reviews");
				this.parseHistory(HttpResponseUtil.getInputStream(response));
				seleCourse.setViewed(1);
				cDAO.update(seleCourse);
				result.setSuccessResult(csDAO.findByCourseId(courseID));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		}
		return result;
	}
	
	private void parseHistory(InputStream in) throws Exception {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try{
			reader.beginObject();
			while(reader.hasNext()) {				
				String name = reader.nextName();
				if("result".equals(name)) {
					this.parseResults(reader);
				}else{
					reader.skipValue();
				}
			}
			reader.endObject();
		}finally{
			reader.close();
		}
	}

	private void parseResults(JsonReader reader) throws Exception {
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			if("values".equals(name)) {
				this.parseValues(reader);
			}else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	private void parseValues(JsonReader reader) throws Exception {
		reader.beginArray();
		dbMgr.beginTransaction();
		while(reader.hasNext()) {
			this.parseCourse(reader);
		}
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		reader.endArray();
	}

	private void parseCourse(JsonReader reader) throws Exception{
		reader.beginObject();
		CourseSectionDAO csDAO = dbMgr.getCourseSectionDAO();
		CourseDetailDAO cdDAO = dbMgr.getCourseDetailDAO();
		CourseSection cs = new CourseSection();
		cs.setCourseDetail(new CourseDetail());
		cs.setInstructor(new Instructor());
		Course course = new Course();
		course.setId(courseID);
		cs.getCourseDetail().setCourse(course);
		
		while(reader.hasNext()) {
			String name = reader.nextName();
			if("instructor".equals(name)) {
				this.parseInstructor(reader, cs);			
			}
			else if("ratings".equals(name)) {
				this.parseRatings(reader, cs);
			}
			else if("section".equals(name)) {
				this.parseSection(reader, cs);
			}
			else {
				reader.skipValue();
			}
		}
		
		if (cs.getCourseDetail() != null) {
			csDAO.add(cs);
			cdDAO.add(cs.getCourseDetail());
		}
		reader.endObject();
	}
	
	private void parseInstructor(JsonReader reader, CourseSection cs) throws Exception {
		reader.beginObject();
		while (reader.hasNext()) {
			if ("id".equals(reader.nextName())) {
				String intrId = JsonUtil.readNullOrString(reader).trim();
				cs.getInstructor().setId(intrId);		
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	private void parseRatings(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			RatingConfig config = PreferenceUtil.findRatingConfigByKey(name);
			if (config == null) {
				reader.skipValue();
			} else {
				cs.setRating(config.getId(), Double.parseDouble(JsonUtil.readNullOrString(reader).trim()));
			}
		}
		reader.endObject();
	}
	
	private void parseSection(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			if("primary_alias".equals(name)) {
				String s = JsonUtil.readNullOrString(reader).trim().toUpperCase();
				cs.setPrimaryAlias(s.substring(0, s.lastIndexOf('-')));
			}
			else if("id".equals(name)) {
				parseSectionID(JsonUtil.readNullOrString(reader).trim(), cs);
			}
			else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	private void parseSectionID(String sectionID, CourseSection cs) {
		String[] ids = sectionID.split("-");
		cs.getCourseDetail().setId(ids[0]);
		cs.setSection(ids[1]);
		try {
			HttpResponse response = HttpClientUtil.getFromPath("courses/" + ids[0]);
			this.parseCourseDetailResult(HttpResponseUtil.getInputStream(response), cs);
		}catch(Exception e) {
			e.printStackTrace();
			cs.setCourseDetail(null);
		}
	}
	
	private void parseCourseDetailResult(InputStream inputStream, CourseSection cs) throws Exception{
		JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if ("result".equals(name)) {
					this.parseCourseDetails(reader, cs);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}
	}
		
	private void parseCourseDetails(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			if("description".equals(name)) {
				cs.getCourseDetail().setDescription(JsonUtil.readNullOrString(reader).trim());
			}else if("id".equals(name)) {
				cs.getCourseDetail().setId(JsonUtil.readNullOrString(reader).trim());
			}else if("semester".equals(name)) {
				Semester sem = new Semester();
				sem.setId(JsonUtil.readNullOrString(reader).trim());
				cs.getCourseDetail().setSemester(sem);
			}else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}


}
