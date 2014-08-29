package edu.upenn.pcr.model.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import edu.upenn.pcr.model.db.entity.InvertedIndex;
import edu.upenn.pcr.model.db.entity.InvertedIndex.ItemType;

public class InvertedIndexDAO extends BaseDAO {

	public InvertedIndexDAO(SQLiteDatabase db) {
		super(db);
	}
	
	public void add(InvertedIndex ii) {
		SQLiteDatabase db = this.getDatabase();
		ContentValues value = new ContentValues();
		value.put(InvertedIndex.KEYWORD, ii.getKeyword());
		value.put(InvertedIndex.ITEM_TYPE, ii.getType().toString());
		value.put(InvertedIndex.FULL_TEXT, ii.getFullText());
		value.put(InvertedIndex.REFERENCE_ID, ii.getRefId());
		db.insert(InvertedIndex.TABLE_NAME, null, value);
	}
	
	public List<InvertedIndex> matchAll(String keyword, int begin, int count) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(InvertedIndex.TABLE_NAME);
		String selection = InvertedIndex.KEYWORD + " LIKE ?";
		String [] selectionArgs = {keyword+"%"};
		Cursor cursor = query.query(db, null, selection, selectionArgs, null, null, InvertedIndex.KEYWORD + " ASC", begin + "," + count);
		List<InvertedIndex> result = new ArrayList<InvertedIndex>();
		int idIndex = cursor.getColumnIndex(InvertedIndex.ID);
		int keywordIndex = cursor.getColumnIndex(InvertedIndex.KEYWORD);
		int fullTextIndex = cursor.getColumnIndex(InvertedIndex.FULL_TEXT);
		int typeIndex = cursor.getColumnIndex(InvertedIndex.ITEM_TYPE);
		int refIdIndex = cursor.getColumnIndex(InvertedIndex.REFERENCE_ID);
		while (cursor.moveToNext()) {
			InvertedIndex ii = new InvertedIndex();
			ii.setId(cursor.getInt(idIndex));
			ii.setKeyword(cursor.getString(keywordIndex));
			ii.setFullText(cursor.getString(fullTextIndex));
			ii.setType(ItemType.valueOf(cursor.getString(typeIndex)));
			ii.setRefId(cursor.getString(refIdIndex));
			result.add(ii);
		}
		cursor.close();
		return result;
	}
	
	public InvertedIndex findByKeyword(String keyword) {
		SQLiteDatabase db = this.getDatabase();
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(InvertedIndex.TABLE_NAME);
		String selection = InvertedIndex.KEYWORD + " = ?";
		String [] selectionArgs = {keyword};
		Cursor cursor = query.query(db, null, selection, selectionArgs, null, null, null);
		if (!cursor.moveToFirst()) {
			return null;
		}
		int idIndex = cursor.getColumnIndex(InvertedIndex.ID);
		int keywordIndex = cursor.getColumnIndex(InvertedIndex.KEYWORD);
		int typeIndex = cursor.getColumnIndex(InvertedIndex.ITEM_TYPE);
		int refIdIndex = cursor.getColumnIndex(InvertedIndex.REFERENCE_ID);
		InvertedIndex ii = new InvertedIndex();
		ii.setId(cursor.getInt(idIndex));
		ii.setKeyword(cursor.getString(keywordIndex));
		ii.setType(ItemType.valueOf(cursor.getString(typeIndex)));
		ii.setRefId(cursor.getString(refIdIndex));
		cursor.close();
		return ii;
	}
}
