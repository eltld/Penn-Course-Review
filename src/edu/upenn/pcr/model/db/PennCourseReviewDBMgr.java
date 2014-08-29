package edu.upenn.pcr.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import edu.upenn.pcr.model.db.dao.CodeDAO;
import edu.upenn.pcr.model.db.dao.CourseCodeDAO;
import edu.upenn.pcr.model.db.dao.CourseDAO;
import edu.upenn.pcr.model.db.dao.CourseDetailDAO;
import edu.upenn.pcr.model.db.dao.CourseSectionDAO;
import edu.upenn.pcr.model.db.dao.DepartmentDAO;
import edu.upenn.pcr.model.db.dao.InstructorDAO;
import edu.upenn.pcr.model.db.dao.InvertedIndexDAO;
import edu.upenn.pcr.model.db.dao.RecentDAO;
import edu.upenn.pcr.model.db.dao.SemesterDAO;

public class PennCourseReviewDBMgr {

	private PennCourseReviewDBHelper dbHelper;
	private SQLiteDatabase db;
	
	private CodeDAO codeDAO = null;
	private CourseCodeDAO courseCodeDAO = null;
	private CourseDAO courseDAO = null;
	private CourseDetailDAO courseDetailDAO = null;
	private CourseSectionDAO courseSectionDAO = null;
	private DepartmentDAO deptDAO = null;
	private InstructorDAO instrDAO = null;
	private InvertedIndexDAO invertedIndexDAO = null;
	private RecentDAO recentDAO = null;
	private SemesterDAO semesterDAO = null;
	
	private static PennCourseReviewDBMgr instance;
	private PennCourseReviewDBMgr(Context context) {
		dbHelper = PennCourseReviewDBHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
		
		codeDAO = new CodeDAO(db);
		courseCodeDAO = new CourseCodeDAO(db);
		courseDAO = new CourseDAO(db);
		courseDetailDAO = new CourseDetailDAO(db);
		courseSectionDAO = new CourseSectionDAO(db);
		deptDAO = new DepartmentDAO(db);
		instrDAO = new InstructorDAO(db);
		invertedIndexDAO = new InvertedIndexDAO(db);
		recentDAO = new RecentDAO(db);
		semesterDAO = new SemesterDAO(db);
	}
	
	public static PennCourseReviewDBMgr getInstance(Context context) {
		if (instance == null) {
			instance = new PennCourseReviewDBMgr(context);
		}
		return instance;
	}
	public void beginTransaction() {
		db.beginTransaction();
	}
	
	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}
	
	public void endTransaction() {
		db.endTransaction();
	}

	public CodeDAO getCodeDAO() {
		return codeDAO;
	}
	
	public CourseCodeDAO getCourseCodeDAO() {
		return courseCodeDAO;
	}

	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	public CourseDetailDAO getCourseDetailDAO() {
		return courseDetailDAO;
	}

	public CourseSectionDAO getCourseSectionDAO() {
		return courseSectionDAO;
	}

	public DepartmentDAO getDepartmentDAO() {
		return deptDAO;
	}

	public InstructorDAO getInstructorDAO() {
		return instrDAO;
	}

	public InvertedIndexDAO getInvertedIndexDAO() {
		return invertedIndexDAO;
	}

	public RecentDAO getRecentDAO() {
		return recentDAO;
	}

	public SemesterDAO getSemesterDAO() {
		return semesterDAO;
	}
	
	public long getDatabaseSize(Context context) {
		return dbHelper.getDatabaseSize(context);
	}
	
	public void clearDatabase() {
		dbHelper.clearDatabase(db);
	}
	
}
