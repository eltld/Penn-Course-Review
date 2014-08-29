package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;

import android.util.Log;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.CourseDetailDAO;
import edu.upenn.pcr.model.db.dao.CourseSectionDAO;
import edu.upenn.pcr.model.db.dao.InstructorDAO;
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

public class FetchSectionByInstrAsync extends AsyncTaskBase<String, Void, List<CourseSection>> {

	private PennCourseReviewDBMgr dbMgr;
	private String instrID;

	public FetchSectionByInstrAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
		dbMgr.getCourseDAO();
	}

	@Override
	protected AsyncTaskResult<List<CourseSection>> doInBackground(String... params) {
		//result is a list of course taught by given instructor
		AsyncTaskResult<List<CourseSection>> result = new AsyncTaskResult<List<CourseSection>>();
		
		if(params.length == 0){
			result.setSuccessResult(null);
			return result;
		}
		instrID = params[0];
		//find it from Database
		InstructorDAO instrDAO = dbMgr.getInstructorDAO();	
		Instructor instr = instrDAO.findById(instrID);
		Integer viewed = instr.getViewed();
		CourseSectionDAO csDAO = dbMgr.getCourseSectionDAO();
		if(viewed == 0) {			
			//http://api.penncoursereview.com/v1/instructors/298-sudipto-guha/reviews?token=573-2_4ykZdNLiHFfc9rehZ9c8fCNg
			try {
				HttpResponse response = HttpClientUtil.getFromPath("/instructors/"+instrID+"/reviews");
				this.parseCourses(HttpResponseUtil.getInputStream(response));
				/*Set Instructor to be viewed*/		
				instr.setViewed(1);
				instrDAO.update(instr);
			} catch (Exception e) {
				result.setFailureError(e);
				e.printStackTrace();
				return result;
			}
		}
		result.setSuccessResult(csDAO.findByInstructor(instrID));
		return result;
	}

	private void parseCourses(InputStream in) throws Exception{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try{
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if("result".equals(name)){
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

	private void parseResults(JsonReader reader) throws Exception{
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName(); 
			if("values".equals(name)){
				this.parseValues(reader);
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
	}
   /**
    * Each value closure is a CourseSection
    * @param reader
    * @throws Exception
    */
	private void parseValues(JsonReader reader) throws Exception{
		reader.beginArray();
		dbMgr.beginTransaction();
		/*Parse each CourseSection*/
		while(reader.hasNext()){
			this.parseCourseSection(reader);
		}
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		reader.endArray();
	}

	private void parseCourseSection(JsonReader reader) throws Exception{
		reader.beginObject();
		//populate CourseSection table and CourseDetail table
		CourseSectionDAO courseSectionDAO = dbMgr.getCourseSectionDAO();
		CourseDetailDAO courseDetailDAO = dbMgr.getCourseDetailDAO();
		CourseSection courseSection = new CourseSection();
		courseSection.setCourseDetail(new CourseDetail());
		courseSection.setInstructor(new Instructor());

		while(reader.hasNext()){
			String name = reader.nextName();
			if("instructor".equals(name)){
				this.parseInstructor(reader, courseSection);
			}else if("ratings".equals(name)){
				this.parseRatings(reader, courseSection);
			}else if("section".equals(name)){
				this.parseSection(reader, courseSection);
			}else{
				reader.skipValue();
			}

		}
		
		if(instrID.equals(courseSection.getInstructor().getId())){
			courseDetailDAO.add(courseSection.getCourseDetail());
			courseSectionDAO.add(courseSection);
		}
		reader.endObject();
	}

	private void parseSection(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if("primary_alias".equals(name)){
				String in = JsonUtil.readNullOrString(reader).trim().toUpperCase();
				cs.setPrimaryAlias(in.substring(0, in.lastIndexOf('-')));
			}else if("id".equals(name)){
				this.parseSectionID(JsonUtil.readNullOrString(reader).trim(), cs);
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	/**
	 * Based on courseDetailID, search for courseDetail, get Course, Semester and Description
	 * @param sectionID in the format "309-001", 309-CourseDetailID, 001-SectionID
	 */
	private void parseSectionID(String sectionID, CourseSection cs){
		String[] ids = sectionID.split("-");
		cs.getCourseDetail().setId(ids[0]);//set CourseDetailID
		cs.setSection(ids[1]);//set CourseSection section

		/*get semester, description and courseID by searching with courseDetailID*/
		try {
			HttpResponse response = HttpClientUtil.getFromPath("/courses/"+ids[0]);
			this.parseACourseDetail(HttpResponseUtil.getInputStream(response), cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseACourseDetail(InputStream in, CourseSection cs) throws Exception{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try{
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if("result".equals(name)){
					this.parseCourseDetailResults(reader, cs);
				}else{
					reader.skipValue();
				}
			}
			reader.endObject();
		}finally{
			reader.close();
		}
	}

	private void parseCourseDetailResults(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if("coursehistories".equals(name)){
				String courseID = this.parseCourseHistory(reader);
				if(courseID != null){
					Course course = new Course();
					course.setId(courseID);
					Log.d("COURSE_id", courseID);					
					cs.getCourseDetail().setCourse(course);
				}
			}else if("description".equals(name)){
				cs.getCourseDetail().setDescription(JsonUtil.readNullOrString(reader).trim());
			}else if("semester".equals(name)){
				Semester sem = new Semester();
				sem.setId(JsonUtil.readNullOrString(reader).trim());
				cs.getCourseDetail().setSemester(sem);				
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	/**
	 * From a courseDetail's path, get CourseID
	 * @param reader
	 * @throws Exception
	 */
	private String parseCourseHistory(JsonReader reader) throws Exception{
		String courseID = null;
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if("path".equals(name)){
				String path = JsonUtil.readNullOrString(reader).trim();
				String[] splittedPath = path.split("/");
				courseID = splittedPath[splittedPath.length-1];								
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return courseID;
	}

	/**
	 * Populate all kinds of ratings for courseSection
	 * @param reader
	 * @param cs
	 * @throws Exception
	 */
	private void parseRatings(JsonReader reader, CourseSection cs) throws Exception{
		reader.beginObject();

		while(reader.hasNext()){
			String name = reader.nextName();
			String value = JsonUtil.readNullOrString(reader).trim();
			if(value.length() == 0){continue;}
			double dValue = Double.parseDouble(value);
			RatingConfig config = PreferenceUtil.findRatingConfigByKey(name);
			if (config != null) {
				cs.setRating(config.getId(), dValue);
			}
		}
		reader.endObject();
	}

	private void parseInstructor(JsonReader reader, CourseSection courseSection) throws Exception{
		reader.beginObject();
		Instructor instr = courseSection.getInstructor();

		while(reader.hasNext()){
			String name = reader.nextName();

			if("first_name".equals(name)){
				instr.setFirstName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			}else if("id".equals(name)){
				instr.setId(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			}else if("last_name".equals(name)){
				instr.setLastName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			}else {
				reader.skipValue();
			}
		}

		reader.endObject();
	}

}
