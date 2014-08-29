package edu.upenn.pcr.model.async;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.stream.JsonReader;

import edu.upenn.pcr.controller.Controller;
import edu.upenn.pcr.model.db.PennCourseReviewDBMgr;
import edu.upenn.pcr.model.db.dao.DepartmentDAO;
import edu.upenn.pcr.model.db.dao.InvertedIndexDAO;
import edu.upenn.pcr.model.db.entity.Department;
import edu.upenn.pcr.model.db.entity.InvertedIndex;
import edu.upenn.pcr.model.http.HttpClientUtil;
import edu.upenn.pcr.model.http.HttpResponseUtil;
import edu.upenn.pcr.model.util.JsonUtil;

public class GetDeptsAsync extends AsyncTaskBase<Void, Void, List<String>> {
	
	private PennCourseReviewDBMgr dbMgr;
	
	public GetDeptsAsync(Integer what, Controller controller) {
		super(what, controller);
		dbMgr = PennCourseReviewDBMgr.getInstance(controller.getContext());
	}
	
	@Override
	protected AsyncTaskResult<List<String>> doInBackground(Void... params) {
		AsyncTaskResult<List<String>> result = new AsyncTaskResult<List<String>>();
		try {
			HttpResponse response = HttpClientUtil.getFromPath("/depts");
			List<String> deptIds = this.parseDepts(HttpResponseUtil.getInputStream(response));
			result.setSuccessResult(deptIds);
		} catch (Exception e) {
			e.printStackTrace();
			result.setFailureError(e);
		}
		return result;
	}
	
	private List<String> parseDepts(InputStream in) throws Exception {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		List<String> result = null;
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("result")) {
					result = this.readDeptsResult(reader);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}
		return result;
	}
	
	private List<String> readDeptsResult(JsonReader reader) throws Exception {
		reader.beginObject();
		List<String> result = null;
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("values")) {
				result = this.readDeptsValues(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return result;
	}
	
	private List<String> readDeptsValues(JsonReader reader) throws Exception {
		reader.beginArray();
		List<String> result = new ArrayList<String>();
		dbMgr.beginTransaction();
		DepartmentDAO deptDAO = dbMgr.getDepartmentDAO();
		while (reader.hasNext()) {
			Department d = readDept(reader);
			d.setViewed(1);
			deptDAO.add(d);
			result.add(d.getId());
			this.buildIndex(d);
		}
		reader.endArray();
		dbMgr.setTransactionSuccessful();
		dbMgr.endTransaction();
		return result;
	}
	
	private Department readDept(JsonReader reader) throws Exception {
		Department d = new Department();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				d.setId(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else if (name.equals("name")) {
				d.setName(JsonUtil.readNullOrString(reader).trim().toUpperCase());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return d;
	}
	
	private void buildIndex(Department dept) {
		InvertedIndexDAO iiDAO = dbMgr.getInvertedIndexDAO();
		InvertedIndex index = new InvertedIndex();
		index.setKeyword(dept.getId());
		index.setFullText(dept.getId() + " - " + dept.getName());
		index.setType(InvertedIndex.ItemType.DEPARTMENT);
		index.setRefId(dept.getId());
		iiDAO.add(index);
		
		if (dept.getName().length() != 0) {
			index = new InvertedIndex();
			index.setKeyword(dept.getName());
			index.setFullText(dept.getId() + " - " + dept.getName());
			index.setType(InvertedIndex.ItemType.DEPARTMENT);
			index.setRefId(dept.getId());
			iiDAO.add(index);
		}
	}
	
}
