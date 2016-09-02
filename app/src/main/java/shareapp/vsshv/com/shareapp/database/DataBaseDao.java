package shareapp.vsshv.com.shareapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shareapp.vsshv.com.shareapp.datasets.TwitterSet;

import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_ID;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_MESSAGE;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_PROFILEIMG;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_SCEDULEDTIME;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_USERMAIL;
import static shareapp.vsshv.com.shareapp.database.DataBaseHelper.SHARE_COLUMN_USERNAME;

public class DataBaseDao {

	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	private String[] allColumns_SYNC = { SHARE_COLUMN_ID,
			SHARE_COLUMN_USERNAME,
			SHARE_COLUMN_PROFILEIMG,
			SHARE_COLUMN_MESSAGE,
			SHARE_COLUMN_USERMAIL,
			SHARE_COLUMN_SCEDULEDTIME};

	public DataBaseDao(Context context) {
		dbHelper = new DataBaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		if (null != dbHelper)
			dbHelper.close();
	}

	public synchronized void insertTwitterMessages(TwitterSet set)
	{
		try {
			open();
			ContentValues contentValues = new ContentValues();
			//contentValues.put(SHARE_COLUMN_USERNAME, set.getUserName());
			//contentValues.put(SHARE_COLUMN_PROFILEIMG, set.getUserName());
			contentValues.put(SHARE_COLUMN_MESSAGE, set.getMessage());
			//contentValues.put(SHARE_COLUMN_USERMAIL, set.getUserName());
			contentValues.put(SHARE_COLUMN_SCEDULEDTIME, set.getScheduled());
			long row = database.insert(DataBaseHelper.TABLE_TWITTER, null, contentValues);

			Log.d("===============", "Insert:"+row);
		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}

	public synchronized void removePushData(int _id) {
		try {
			open();



		} catch (SQLException e) {
			Log.e("Error", "" + e.toString());
		} finally {
			close();
		}
	}
	/**
	 * 
	 * @return
	 */
	public List<TwitterSet> getAllTMessages() {
		List<TwitterSet> typeList = new ArrayList<TwitterSet>();

		try {
			open();
			Cursor cursor = database.query(DataBaseHelper.TABLE_TWITTER,
					allColumns_SYNC, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					TwitterSet comment = cursorToTypes(cursor);
					typeList.add(comment);
					cursor.moveToNext();
				}

			}
			cursor.close();
		} catch (Exception e) {
			Log.e("Error", "" + e.toString());
			
		}finally{
			close();
		}

		return typeList;
	}

	private TwitterSet cursorToTypes(Cursor cursor) {
		TwitterSet set = new TwitterSet();
		set.set_id(cursor.getInt(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		set.setMessage(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		//set.setUserEmail(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		//set.setUserName(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		//set.setUserProfileUrl(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		set.setScheduled(cursor.getString(cursor.getColumnIndex(SHARE_COLUMN_ID)));
		return set;
	}

}