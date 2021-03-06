/*
 * Copyright (c) 2011-2012 CommonsWare, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.timbyr.lib.loaders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteCursorLoader extends com.commonsware.cwac.loaderex.SQLiteCursorLoader {

	SQLiteOpenHelper db = null;
	String rawQuery = null;
	private boolean distinct = false;
	private String table = null;
	private String[] columns = null;
	private String selection = null;
	private String[] selectionArgs = null;
	private String groupBy = null;
	private String having = null;
	private String orderBy = null;
	private String limit = null;

	/**
	 * Creates a fully-specified SQLiteCursorLoader. See
	 * {@link SQLiteDatabase#rawQuery(SQLiteDatabase, String, String[])
	 * SQLiteDatabase.rawQuery()} for documentation on the
	 * meaning of the parameters. These will be passed as-is
	 * to that call.
	 */

	public SQLiteCursorLoader(Context context, SQLiteOpenHelper db, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		super(context, db, null, null);
		this.db = db;
		this.distinct = distinct;
		this.table = table;
		this.columns = columns;
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		this.groupBy = groupBy;
		this.having = having;
		this.orderBy = orderBy;
		this.limit = limit;
	}
	
	public SQLiteCursorLoader(Context context, SQLiteOpenHelper db, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		this(context, db, false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	public SQLiteCursorLoader(Context context, SQLiteOpenHelper db, String rawQuery, String[] args) {
		super(context, db, rawQuery, args);
		this.rawQuery = rawQuery;
	}

	/**
	 * Runs on a worker thread and performs the actual
	 * database query to retrieve the Cursor.
	 */
	@Override
	protected Cursor buildCursor() {
		if (this.rawQuery!=null) {
			return super.buildCursor();
		}
		else {
			return(db.getReadableDatabase().query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit));
		}
	}
}