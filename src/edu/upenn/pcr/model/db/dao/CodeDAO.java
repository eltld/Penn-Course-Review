package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.pcr.model.db.entity.Code;
import edu.upenn.pcr.model.db.entity.Department;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CodeDAO extends BaseDAO {

	public CodeDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public Integer count() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Code.TABLE_NAME, new String [] {"count(*)"}, null, null, null, null, null);
		int result = 0;
		if (!cursor.moveToFirst()) {
			result = 0;
		} else {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
	public void add(Code code) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(Code.CODE, code.getCode());
		value.put(Code.DEPT_ID, code.getDepartment().getId());
		db.insertWithOnConflict(Code.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
	}

	public List<Code> findAll() {
		SQLiteDatabase db = this.getDatabase();
		Cursor cursor = db.query(Code.TABLE_NAME, null, null, null, null, null, null);
		List<Code> result = new ArrayList<Code>();
		int codeIndex = cursor.getColumnIndex(Code.CODE);
		int deptIdIndex = cursor.getColumnIndex(Code.DEPT_ID);
		while (cursor.moveToNext()) {
			Code code = new Code();
			code.setCode(cursor.getString(codeIndex));
			Department d = new Department();
			d.setId(cursor.getString(deptIdIndex));
			code.setDepartment(d);
			result.add(code);
		}
		return result;
	}
}
